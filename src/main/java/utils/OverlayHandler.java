package utils;

import com.microsoft.playwright.Page;

public final class OverlayHandler {

    private OverlayHandler() {
    }

    public static void neutralizeKnownOverlays(Page page) {

        try {

            page.evaluate("""
                () => {

                    // JSD Support Widget
                    const widget = document.getElementById('jsd-widget');

                    if (widget) {
                        widget.style.pointerEvents = 'none';
                    }

                    // Any iframe version of widget
                    document
                        .querySelectorAll('iframe[id="jsd-widget"]')
                        .forEach(frame => {
                            frame.style.pointerEvents = 'none';
                        });

                    // Common launcher button
                    document
                        .querySelectorAll('[aria-label*="support"], [aria-label*="Support"]')
                        .forEach(el => {
                            el.style.pointerEvents = 'none';
                        });

                }
            """);

            System.out.println("[OVERLAY] Known overlays neutralized");

        } catch (Exception e) {

            System.out.println(
                    "[OVERLAY] Unable to neutralize overlays: "
                            + e.getMessage());
        }
    }
}