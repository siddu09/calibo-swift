package pages.PRO.Features.Design;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import selfhealingHandler.ResilientLocator;

public class SearchDesignPage extends MultipleDesignPage {
    private final Page page;

    public SearchDesignPage(Page page) {
        super(page);
        this.page = page;
    }

    public Locator searchDesigns(String placeholder) {
        return new ResilientLocator(page, "Search Designs input")
                .byCss("#app-layout-container input#search.design-filter-search")
                .byCss("#app-layout-container input.design-filter-search")
                .byCss("#app-layout-container input[placeholder='" + placeholder + "']")
                .byXPath("//*[@id='app-layout-container']//input[@placeholder='" + placeholder + "']")
                .custom("Design search field by placeholder", () -> page.getByPlaceholder(placeholder,
                        new Page.GetByPlaceholderOptions().setExact(true)))
                .resolve();
    }

    public Locator closeDetails() {
        return new ResilientLocator(page, "Close Design Details slide")
                .byCss("#menu .sheet-body > .row:first-child .cross-pointer")
                .byXPath("//*[@id='menu']//*[contains(@class,'cross-icon-margins')]/*[contains(@class,'cross-pointer')]")
                .byCss("#menu .cross-pointer:has(svg path[d='M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z'])")
                .resolve();
    }
}
