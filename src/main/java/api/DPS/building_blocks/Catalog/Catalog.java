package api.DPS.building_blocks.Catalog;

import io.qameta.allure.Step;

import api.DPS.building_blocks.Crawler.Crawler;
import api.DPS.helpers.Catalogs.CatalogHelper;

public final class Catalog {
    private final CatalogHelper helper;
    private boolean created;

    public Catalog(Crawler crawler) {
        helper = new CatalogHelper(crawler.context(), crawler.helper());
    }

    @Step("Create Catalog")
    public void createCatalog() { helper.create(); created = true; }

    public void verifyCatalogDetails() {
        if (!created) throw new IllegalStateException("Catalog has not been created");
        helper.verify();
    }
}
