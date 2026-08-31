package assertionsHandler;

import org.testng.asserts.SoftAssert;

public class AssertionManager {

    private static final ThreadLocal<SoftAssert>
            SOFT_ASSERT =
            ThreadLocal.withInitial(
                    SoftAssert::new);

    private AssertionManager() {
    }

    public static void softAssertEquals(
            Object actual,
            Object expected,
            String message) {

        SOFT_ASSERT.get()
                .assertEquals(
                        actual,
                        expected,
                        message);
    }

    public static void softAssertTrue(
            boolean condition,
            String message) {

        SOFT_ASSERT.get()
                .assertTrue(
                        condition,
                        message);
    }

    public static void assertAll() {

        SOFT_ASSERT.get().assertAll();

        SOFT_ASSERT.remove();
    }
}