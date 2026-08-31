package tests.selfhealing;

import org.testng.Assert;
import org.testng.annotations.Test;
import selfhealingHandler.ResilientLocator;

import java.lang.reflect.Method;

/**
 * Guards against silent drift between {@link selfhealingHandler.generator.PageObjectGenerator}
 * (which hand-writes source-code strings like {@code .byTestId("x")}) and
 * {@link ResilientLocator}'s actual fluent builder API.
 *
 * <p>{@code PageObjectGenerator.fluentChain(...)} emits calls to a fixed set of
 * {@code ResilientLocator} methods for SELF_HEALING mode. If someone renames or
 * removes one of those methods on {@code ResilientLocator} without updating the
 * generator, every future SELF_HEALING Page Object generated from a JSON
 * snapshot would fail to compile - but nothing in THIS project would catch
 * that at build time, because the emitted code lives in a separate output
 * file compiled later. This test catches that drift immediately, at the
 * source of truth, without needing to actually generate and compile a POM.</p>
 */
public class ResilientLocatorGeneratorParityTest {

    /**
     * Method names + parameter types that {@code PageObjectGenerator.fluentChain}
     * relies on existing for the SELF_HEALING fluent chain, plus the terminal
     * {@code resolve()} call.
     */
    private static final Object[][] REQUIRED_METHODS = {
            {"byTestId", new Class<?>[]{String.class}},
            {"byId", new Class<?>[]{String.class}},
            {"byName", new Class<?>[]{String.class}},
            {"byRole", new Class<?>[]{com.microsoft.playwright.options.AriaRole.class, String.class}},
            {"byPlaceholder", new Class<?>[]{String.class}},
            {"byCss", new Class<?>[]{String.class}},
            {"byXPath", new Class<?>[]{String.class}},
            {"resolve", new Class<?>[]{}},
    };

    @Test
    public void resilientLocatorExposesEveryMethodTheGeneratorEmits() {
        for (Object[] spec : REQUIRED_METHODS) {
            String methodName = (String) spec[0];
            Class<?>[] paramTypes = (Class<?>[]) spec[1];

            Method method = findMethod(methodName, paramTypes);

            Assert.assertNotNull(method,
                    "ResilientLocator is missing method '" + methodName
                            + "(" + describe(paramTypes) + ")' that PageObjectGenerator"
                            + " (SELF_HEALING mode) relies on. Generated Page Objects"
                            + " calling this method would fail to compile. Update either"
                            + " ResilientLocator or PageObjectGenerator.fluentChain() to"
                            + " keep them in sync.");
        }
    }

    private static Method findMethod(String name, Class<?>[] paramTypes) {
        try {
            return ResilientLocator.class.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static String describe(Class<?>[] types) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(types[i].getSimpleName());
        }
        return sb.toString();
    }
}
