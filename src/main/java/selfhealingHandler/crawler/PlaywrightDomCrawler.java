package selfhealingHandler.crawler;

import selfhealingHandler.model.ElementSnapshot;
import selfhealingHandler.model.PageSnapshot;
import com.microsoft.playwright.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Crawls the DOM of a live Playwright {@link Page} and captures every
 * candidate locator for interactive elements into a {@link PageSnapshot}.
 *
 * <p><b>Internal collaborator:</b> most callers should use
 * {@link selfhealingHandler.pipeline.LocatorPageObjectPipeline#run} instead of
 * calling this class directly - it wires crawling, JSON persistence, and
 * Page Object generation together in one call.</p>
 */
@SuppressWarnings("unchecked")
public class PlaywrightDomCrawler {

    public PageSnapshot crawl(Page page, String pageName) {

        String script = """
            () => {
                const selector = [
                    'input',
                    'button',
                    'a',
                    'select',
                    'textarea',
                    '[role]',
                    '[aria-label]',
                    '[data-testid]',
                    '[data-test]',
                    '[data-qa]',
                    '[placeholder]'
                ].join(',');

                const elements = Array.from(document.querySelectorAll(selector));

                function clean(value) {
                    if (!value) return '';
                    return String(value).replace(/\\s+/g, ' ').trim();
                }

                function escapeValue(value) {
                    if (!value) return '';
                    return String(value).replace(/"/g, '\\\\"').replace(/'/g, "\\\\'");
                }

                function getCssSelector(el) {
                    const tag = el.tagName.toLowerCase();

                    if (el.id) {
                        return tag + '#' + CSS.escape(el.id);
                    }

                    if (el.getAttribute('data-testid')) {
                        return tag + '[data-testid="' + escapeValue(el.getAttribute('data-testid')) + '"]';
                    }

                    if (el.getAttribute('data-test')) {
                        return tag + '[data-test="' + escapeValue(el.getAttribute('data-test')) + '"]';
                    }

                    if (el.getAttribute('data-qa')) {
                        return tag + '[data-qa="' + escapeValue(el.getAttribute('data-qa')) + '"]';
                    }

                    if (el.getAttribute('name')) {
                        return tag + '[name="' + escapeValue(el.getAttribute('name')) + '"]';
                    }

                    if (el.getAttribute('placeholder')) {
                        return tag + '[placeholder="' + escapeValue(el.getAttribute('placeholder')) + '"]';
                    }

                    return tag;
                }

                function getXPath(el) {
                    if (el.id) {
                        return '//*[@id="' + escapeValue(el.id) + '"]';
                    }

                    if (el.getAttribute('data-testid')) {
                        return '//*[@data-testid="' + escapeValue(el.getAttribute('data-testid')) + '"]';
                    }

                    if (el.getAttribute('name')) {
                        return '//*[@name="' + escapeValue(el.getAttribute('name')) + '"]';
                    }

                    const parts = [];

                    while (el && el.nodeType === Node.ELEMENT_NODE) {
                        let index = 1;
                        let sibling = el.previousElementSibling;

                        while (sibling) {
                            if (sibling.tagName === el.tagName) {
                                index++;
                            }
                            sibling = sibling.previousElementSibling;
                        }

                        parts.unshift(el.tagName.toLowerCase() + '[' + index + ']');
                        el = el.parentElement;
                    }

                    return '/' + parts.join('/');
                }

                return elements.map((el, index) => {
                    const rect = el.getBoundingClientRect();

                    const tag = el.tagName.toLowerCase();
                    const text = clean(el.innerText || el.textContent || '').substring(0, 100);
                    const id = clean(el.id);
                    const name = clean(el.getAttribute('name'));
                    const className = clean(el.getAttribute('class'));
                    const placeholder = clean(el.getAttribute('placeholder'));
                    const ariaLabel = clean(el.getAttribute('aria-label'));
                    const role = clean(el.getAttribute('role'));
                    const dataTestId = clean(el.getAttribute('data-testid'));
                    const href = clean(el.getAttribute('href'));
                    const type = clean(el.getAttribute('type'));

                    const locatorCandidates = {};

                    if (dataTestId) {
                        locatorCandidates.getByTestId = dataTestId;
                    }

                    if (role && (text || ariaLabel)) {
                        locatorCandidates.getByRole = role + '=' + (ariaLabel || text);
                    }

                    if (placeholder) {
                        locatorCandidates.getByPlaceholder = placeholder;
                    }

                    if (text && text.length <= 60) {
                        locatorCandidates.getByText = text;
                    }

                    if (id) {
                        locatorCandidates.cssById = tag + '#' + id;
                    }

                    if (name) {
                        locatorCandidates.cssByName = tag + '[name="' + name + '"]';
                    }

                    locatorCandidates.css = getCssSelector(el);
                    locatorCandidates.xpath = getXPath(el);

                    return {
                        index,
                        tag,
                        text,
                        id,
                        name,
                        className,
                        placeholder,
                        ariaLabel,
                        role,
                        dataTestId,
                        href,
                        type,
                        visible: !!(el.offsetWidth || el.offsetHeight || el.getClientRects().length),
                        disabled: !!el.disabled,
                        x: Math.round(rect.x),
                        y: Math.round(rect.y),
                        width: Math.round(rect.width),
                        height: Math.round(rect.height),
                        locatorCandidates
                    };
                });
            }
            """;

        List<Map<String, Object>> rawElements =
                (List<Map<String, Object>>) page.evaluate(script);

        List<ElementSnapshot> elements = new ArrayList<>();

        for (Map<String, Object> raw : rawElements) {
            ElementSnapshot element = new ElementSnapshot();

            element.setIndex(toInt(raw.get("index")));
            element.setTag(toString(raw.get("tag")));
            element.setText(toString(raw.get("text")));
            element.setId(toString(raw.get("id")));
            element.setName(toString(raw.get("name")));
            element.setClassName(toString(raw.get("className")));
            element.setPlaceholder(toString(raw.get("placeholder")));
            element.setAriaLabel(toString(raw.get("ariaLabel")));
            element.setRole(toString(raw.get("role")));
            element.setDataTestId(toString(raw.get("dataTestId")));
            element.setHref(toString(raw.get("href")));
            element.setType(toString(raw.get("type")));

            element.setVisible(toBoolean(raw.get("visible")));
            element.setDisabled(toBoolean(raw.get("disabled")));

            element.setX(toDouble(raw.get("x")));
            element.setY(toDouble(raw.get("y")));
            element.setWidth(toDouble(raw.get("width")));
            element.setHeight(toDouble(raw.get("height")));

            Map<String, String> locatorCandidates = new LinkedHashMap<>();
            Object locatorsObject = raw.get("locatorCandidates");

            if (locatorsObject instanceof Map<?, ?> locatorsMap) {
                for (Map.Entry<?, ?> entry : locatorsMap.entrySet()) {
                    locatorCandidates.put(
                            String.valueOf(entry.getKey()),
                            String.valueOf(entry.getValue())
                    );
                }
            }

            element.setLocatorCandidates(locatorCandidates);

            elements.add(element);
        }

        PageSnapshot snapshot = new PageSnapshot();
        snapshot.setPageName(pageName);
        snapshot.setUrl(page.url());
        snapshot.setTitle(page.title());
        snapshot.setCapturedAt(LocalDateTime.now().toString());
        snapshot.setElementCount(elements.size());
        snapshot.setElements(elements);

        return snapshot;
    }

    private String toString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private int toInt(Object value) {
        if (value == null) {
            return 0;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.parseInt(String.valueOf(value));
    }

    private double toDouble(Object value) {
        if (value == null) {
            return 0;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        return Double.parseDouble(String.valueOf(value));
    }

    private boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean bool) {
            return bool;
        }

        return Boolean.parseBoolean(String.valueOf(value));
    }
}