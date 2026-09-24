package utils;

import com.microsoft.playwright.Page;
import drivers.PlaywrightFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages browser sessions with intelligent reuse based on flags.
 *
 * Supports two modes:
 * - reuseSession=yes: Serial execution, share session across invocations (locked)
 * - reuseSession=no: Independent sessions, can run in parallel
 *
 * Thread-safe session registry with per-group locking.
 */
public class SessionManager {

    private static final ThreadLocal<Page> threadLocalPage = new ThreadLocal<>();
    private static final ThreadLocal<PlaywrightFactory> threadLocalFactory = new ThreadLocal<>();

    // Session groups: key=reuseGroupId (domain for yes flag), value={factory, page, lock}
    private static final Map<String, SessionGroup> sessionRegistry = new HashMap<>();
    private static final ReentrantLock registryLock = new ReentrantLock();

    public static class SessionGroup {
        public PlaywrightFactory factory;
        public Page page;
        public ReentrantLock lock = new ReentrantLock();
        public int usageCount = 0;
    }

    /**
     * Initialize or reuse session based on flag.
     * If reuseSessionFlag=yes: Locks group, reuses page if exists or stores provided page
     * If reuseSessionFlag=no: Creates independent page in thread-local
     */
    public static Page acquireSession(String reuseSessionFlag, String groupId) throws Exception {
        return acquireSession(reuseSessionFlag, groupId, null, null);
    }

    /**
     * Initialize or reuse session with optional existing page.
     * Used to pass already-authenticated page to SessionManager.
     */
    public static Page acquireSession(String reuseSessionFlag, String groupId, Page existingPage, PlaywrightFactory existingFactory) throws Exception {
        if ("yes".equalsIgnoreCase(reuseSessionFlag)) {
            return acquireReusableSession(groupId, existingPage, existingFactory);
        } else {
            return acquireIndependentSession(existingPage, existingFactory);
        }
    }

    /**
     * Reusable session: shared across parameterized invocations (serial, locked)
     */
    private static Page acquireReusableSession(String groupId, Page existingPage, PlaywrightFactory existingFactory) throws Exception {
        registryLock.lock();
        try {
            if (!sessionRegistry.containsKey(groupId)) {
                // A new group means every earlier group has run its last sequence,
                // so close their browsers now instead of leaving them open all suite.
                closeFinishedGroups(groupId);

                // First test in group: use existing or create and lock
                SessionGroup group = new SessionGroup();
                group.lock.lock(); // Lock immediately
                if (existingPage != null && existingFactory != null) {
                    // Reuse existing page (already authenticated)
                    group.factory = existingFactory;
                    group.page = existingPage;
                    LoggerUtil.LOGGER.info("[SESSION] ✓ Registered existing page for group: {} (already authenticated)", groupId);
                } else {
                    // Create new
                    group.factory = new PlaywrightFactory();
                    group.page = group.factory.initializeBrowser();
                    LoggerUtil.LOGGER.info("[SESSION] ✓ Created reusable session for group: {}", groupId);
                }
                group.usageCount = 1;
                sessionRegistry.put(groupId, group);
                return group.page;
            } else {
                // Subsequent test in group: acquire lock and reuse
                SessionGroup group = sessionRegistry.get(groupId);
                group.lock.lock(); // Waits until previous test releases lock

                // the page can die between the caller's hasSession() check and here
                if (!isPageAlive(group.page)) {
                    group.lock.unlock();
                    closeQuietly(group, groupId);
                    sessionRegistry.remove(groupId);
                    throw new IllegalStateException("Session for group '" + groupId
                            + "' died before it could be reused. The test skipped login on the"
                            + " assumption this session was usable - rerun it, or give the row"
                            + " sequenceNo=1 so it logs in itself.");
                }

                group.usageCount++;
                LoggerUtil.LOGGER.info("[SESSION] ✓ Reused session for group: {} (usage: {})", groupId, group.usageCount);
                return group.page;
            }
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Closes every registered group except the one starting. Caller must hold registryLock.
     * A group still holding its lock is mid-test, so it is left alone.
     */
    private static void closeFinishedGroups(String startingGroupId) {
        sessionRegistry.entrySet().removeIf(entry -> {
            String id = entry.getKey();
            SessionGroup group = entry.getValue();

            if (id.equals(startingGroupId)) {
                return false;
            }
            if (group.lock.isLocked()) {
                LoggerUtil.LOGGER.warn("[SESSION] Group '{}' still in use - leaving its browser open", id);
                return false;
            }

            if (group.factory != null) {
                try {
                    group.factory.closeBrowser();
                    LoggerUtil.LOGGER.info("[SESSION] ✓ Closed finished group '{}' before starting '{}'",
                            id, startingGroupId);
                } catch (Exception e) {
                    LoggerUtil.LOGGER.warn("[SESSION] Could not close group '{}': {}", id, e.getMessage());
                }
            }
            return true;
        });
    }

    /**
     * Independent session: per-thread, can run in parallel
     */
    private static Page acquireIndependentSession(Page existingPage, PlaywrightFactory existingFactory) throws Exception {
        if (existingPage != null && existingFactory != null) {
            threadLocalFactory.set(existingFactory);
            threadLocalPage.set(existingPage);
            LoggerUtil.LOGGER.info("[SESSION] ✓ Using existing independent session for thread: {}",
                Thread.currentThread().threadId());
            return existingPage;
        }
        PlaywrightFactory factory = new PlaywrightFactory();
        Page page = factory.initializeBrowser();
        threadLocalFactory.set(factory);
        threadLocalPage.set(page);
        LoggerUtil.LOGGER.info("[SESSION] ✓ Created independent session for thread: {}",
            Thread.currentThread().threadId());
        return page;
    }

    /**
     * Release session (called after test).
     * If reusable: unlock group (next invocation can acquire)
     * If independent: close immediately
     */
    public static void releaseSession(String reuseSessionFlag, String groupId, Page page) {
        if ("yes".equalsIgnoreCase(reuseSessionFlag)) {
            releaseReusableSession(groupId);
        } else {
            releaseIndependentSession();
        }
    }

    private static void releaseReusableSession(String groupId) {
        registryLock.lock();
        try {
            SessionGroup group = sessionRegistry.get(groupId);
            if (group != null) {
                group.lock.unlock();
                LoggerUtil.LOGGER.info("[SESSION] ✓ Released lock for group: {}", groupId);
            }
        } finally {
            registryLock.unlock();
        }
    }

    private static void releaseIndependentSession() {
        PlaywrightFactory factory = threadLocalFactory.get();
        if (factory != null) {
            factory.closeBrowser();
            threadLocalFactory.remove();
            threadLocalPage.remove();
            LoggerUtil.LOGGER.info("[SESSION] ✓ Closed independent session for thread: {}",
                Thread.currentThread().threadId());
        }
    }

    /**
     * Whether a group holds a session that is still usable.
     *
     * A Page object outliving its browser is the dangerous case: an earlier row
     * can leave a closed or hung page behind, and the next row then inherits a
     * corpse and fails on a navigation timeout rather than on its own merits.
     * A dead group is dropped here so the caller logs in fresh instead.
     */
    public static boolean hasSession(String groupId) {
        registryLock.lock();
        try {
            SessionGroup group = sessionRegistry.get(groupId);
            if (group == null || group.page == null) {
                return false;
            }
            if (isPageAlive(group.page)) {
                return true;
            }

            LoggerUtil.LOGGER.warn("[SESSION] Group '{}' holds a dead page - discarding it "
                    + "so the next test logs in fresh", groupId);
            closeQuietly(group, groupId);
            sessionRegistry.remove(groupId);
            return false;
        } finally {
            registryLock.unlock();
        }
    }

    /**
     * Cheapest liveness probe that actually touches the browser. isClosed() alone
     * returns false for a page whose connection has gone, so read from it too.
     */
    private static boolean isPageAlive(Page page) {
        try {
            if (page.isClosed()) {
                return false;
            }
            page.title();
            return true;
        } catch (Exception e) {
            LoggerUtil.LOGGER.debug("[SESSION] Liveness probe failed: {}", e.getMessage());
            return false;
        }
    }

    private static void closeQuietly(SessionGroup group, String groupId) {
        if (group.factory == null) {
            return;
        }
        try {
            group.factory.closeBrowser();
        } catch (Exception e) {
            LoggerUtil.LOGGER.debug("[SESSION] Could not close dead group '{}': {}",
                    groupId, e.getMessage());
        }
    }

    /**
     * Cleanup: Close all remaining sessions (called on suite teardown if needed)
     */
    public static void cleanupAll() {
        registryLock.lock();
        try {
            for (Map.Entry<String, SessionGroup> entry : sessionRegistry.entrySet()) {
                SessionGroup group = entry.getValue();
                if (group.factory == null) {
                    continue;
                }
                try {
                    group.factory.closeBrowser();
                    LoggerUtil.LOGGER.info("[SESSION] ✓ Closed group '{}'", entry.getKey());
                } catch (Exception e) {
                    LoggerUtil.LOGGER.warn("[SESSION] Could not close group '{}': {}",
                            entry.getKey(), e.getMessage());
                }
            }
            sessionRegistry.clear();
            LoggerUtil.LOGGER.info("[SESSION] ✓ Cleaned up all sessions");
        } finally {
            registryLock.unlock();
        }
    }
}
