package pages.AIML;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import selfhealingHandler.ResilientLocator;
import utils.LoggerUtil;

/**
 * Page Object for the Calibo "Conversa" RAG chat page
 * (accelerate-qa.calibo.com/conversa-app/snowflake?cortexAgentName=...).
 *
 * <p>Opens in a NEW TAB when "Test Endpoint URL" is clicked in the RAG Builder side panel.
 * Use {@link #switchToConversaTab(Page, Runnable)} to capture that popup.
 *
 * <p>Confirmed selectors (from live DOM inspection):
 * <ul>
 *   <li>Chat input : {@code textarea.chat-input} (placeholder "Need more details about your data?")</li>
 *   <li>Send button: {@code button.ai-send-btn} (text "send")</li>
 *   <li>Clear chat : {@code button.conversa-app-ui-clear-chat}</li>
 *   <li>Reasoning  : {@code button.conversa-app-ui-reasoning-card__toggle} ("Show Details")</li>
 *   <li>Answer text: assistant bubble DIVs have NO stable class, so we capture the LAST
 *       substantial text block that is NOT the echoed question NOR page chrome (footer, etc.).</li>
 * </ul>
 */
public class ConversaPage {

    private final Page page;

    /** Minimum length for a block to be considered a real LLM answer (footer is ~70 chars). */
    private static final int MIN_ANSWER_LEN = 100; // lowered to accept shorter but valid answers

    public ConversaPage(Page page) {
        this.page = page;
    }

    // ======================================================================
    //  New-tab handling
    // ======================================================================

    /**
     * Runs {@code opensNewTab} (e.g. clicking "Test Endpoint URL"), captures the resulting
     * popup/new tab in the same browser context, waits for load, brings it to front, returns it.
     */
    public static Page switchToConversaTab(Page currentPage, Runnable opensNewTab) {
        Page newPage = currentPage.context().waitForPage(opensNewTab);
        newPage.waitForLoadState(LoadState.LOAD);
        newPage.bringToFront();
        LoggerUtil.LOGGER.info("[CONVERSA] Switched to Conversa tab. URL: {}", newPage.url());
        return newPage;
    }

    // ======================================================================
    //  Elements (confirmed)
    // ======================================================================

    /** The large chat input box - textarea.chat-input. */
    public Locator chatInput() {
        return new ResilientLocator(page, "Conversa chat input")
                .byCss("textarea.chat-input")
                .byXPath("//textarea[contains(@placeholder,'Need more details')]")
                .byXPath("//textarea")
                .resolve();
    }

    /** The send button - button.ai-send-btn. */
    public Locator sendButton() {
        return new ResilientLocator(page, "Conversa send button")
                .byCss("button.ai-send-btn")
                .byXPath("//button[contains(@class,'ai-send-btn')]")
                .byXPath("//button[normalize-space()='send']")
                .resolve();
    }

    /** The "Clear Chat" button - button.conversa-app-ui-clear-chat. */
    public Locator clearChatButton() {
        return new ResilientLocator(page, "Clear Chat button")
                .byCss("button.conversa-app-ui-clear-chat")
                .byXPath("//button[contains(@class,'clear-chat')]")
                .resolve();
    }

    /** The Cortex Agent Name field in the left Configuration panel. */
    public Locator cortexAgentName() {
        return new ResilientLocator(page, "Cortex Agent Name")
                .byXPath("//label[normalize-space()='Cortex Agent Name']/following::input[1]")
                .resolve();
    }

    // ======================================================================
    //  Actions
    // ======================================================================

    /** Types a question into the chat box and sends it. */
    public void askQuestion(String question) {
        Locator input = chatInput();
        input.click();
        input.fill(question);
        LoggerUtil.LOGGER.info("[CONVERSA] Entered question: {}", question);
        try {
            sendButton().click();
            LoggerUtil.LOGGER.info("[CONVERSA] Clicked send button");
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[CONVERSA] Send button click failed, pressing Enter: {}", e.getMessage());
            input.press("Enter");
        }
    }

    /**
     * Waits for the LLM (Cortex agent) answer and returns its text.
     *
     * <p>FIX 1: previous version grabbed the page footer ("©2026 Calibo Inc...") because it was
     * the last stable text block and appeared instantly. Now we:
     * <ul>
     *   <li>require the answer to be substantial (&gt; {@value #MIN_ANSWER_LEN} chars),</li>
     *   <li>exclude known page chrome (footer, policy links, config labels),</li>
     *   <li>wait for the text to STOP growing (streaming finished) before accepting it.</li>
     * </ul>
     *
     * @param question  the exact question we sent (to exclude the user echo).
     * @param timeoutMs overall timeout.
     * @return the LLM answer text, or null if none captured.
     */



    public String waitForAnswer(String question, int timeoutMs) {
        LoggerUtil.LOGGER.info("[CONVERSA] Waiting for LLM answer...");
        long deadline = System.currentTimeMillis() + timeoutMs;
        String last = null;
        int stableTicks = 0;

        while (System.currentTimeMillis() < deadline) {
            String candidate = extractLatestAnswer(question);
            if (candidate != null && candidate.length() >= MIN_ANSWER_LEN) {
                // Normalize whitespace to avoid false changes due to minor spacing/linebreak updates
                String normCandidate = candidate.replaceAll("\\s+", " ").trim();
                String normLast = last == null ? null : last.replaceAll("\\s+", " ").trim();

                if (normCandidate.equals(normLast)) {
                    stableTicks++;
                    // stableTicks == 3 -> stable for ~1.5s (500ms poll) => consider finished
                    if (stableTicks >= 3) break;
                } else {
                    stableTicks = 0;
                    last = candidate;
                }
            }

            // Also break early if streaming indicator disappeared and we have a candidate
            try {
                Object streamingObj = page.evaluate("() => { const el = document.querySelector('.ai-streaming, .is-streaming, .typing-indicator'); return !!el; }");
                boolean streaming = false;
                if (streamingObj instanceof Boolean) streaming = (Boolean) streamingObj;
                if (!streaming && last != null && last.length() >= MIN_ANSWER_LEN) {
                    // allow one extra poll for finalization then stop
                    page.waitForTimeout(500);
                    break;
                }
            } catch (Exception ignored) {
                // ignore JS evaluation errors and continue polling
            }

            page.waitForTimeout(500);
        }

        if (last != null && last.length() >= MIN_ANSWER_LEN) {
            LoggerUtil.LOGGER.info("[CONVERSA] Captured LLM answer ({} chars)", last.length());
        } else if (last != null) {
            // return whatever short answer we have as a fallback, but log it clearly
            LoggerUtil.LOGGER.warn("[CONVERSA] Captured short LLM answer ({} chars) — returning anyway", last.length());
        } else {
            LoggerUtil.LOGGER.warn("[CONVERSA] No LLM answer captured within {}ms", timeoutMs);
        }
        return last;
    }

    private String extractLatestAnswer(String question) {
        try {
            Object result = page.evaluate(
                    "(q) => {" +
                            "  const bad = ['Need more details','Thinking','Setting applied'," +
                            "               'You can start chatting','All rights reserved','Privacy Policy'," +
                            "               'Terms of Service','Clear Chat','Cortex Agent','Configuration'," +
                            "               'Validate access'];" +
                            "  const blocks = [...document.querySelectorAll('div,p')]" +
                            "    .filter(e => {" +
                            "       const t = (e.innerText || '').trim();" +
                            "       if (t.length < 150) return false;" +               // real answer is long
                            "       if (e.children.length > 30) return false;" +       // allow markdown tables
                            "       if (t.includes(q.trim())) return false;" +         // not the echo/system block
                            "       if (bad.some(b => t.includes(b))) return false;" + // skip chrome/thinking
                            "       return true;" +
                            "    });" +
                            "  return blocks.length ? blocks[blocks.length - 1].innerText.trim() : null;" +
                            "}", question);
            return result == null ? null : result.toString();
        } catch (Exception e) {
            LoggerUtil.LOGGER.warn("[CONVERSA] extractLatestAnswer failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Finds the latest substantial assistant text block via page.evaluate, excluding the
     * echoed question and page chrome (footer, policy links, config labels, buttons).
     */

    /** Convenience: ask a question and return the LLM answer text. */
    public String ask(String question, int timeoutMs) {
        askQuestion(question);
        return waitForAnswer(question, timeoutMs);
    }
}
