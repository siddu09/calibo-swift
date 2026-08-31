package tests.api.PRO;

import base.apibase.BaseTest;
import io.qameta.allure.Step;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import api.PRO.product.building_blocks.ProductApiBuildingBlock;
import api.PRO.product.helper.ProductApiHelper;

public class ProductApiTest extends BaseTest {
    @Test(groups = "Product")
    @SuppressWarnings("unchecked")
    public void createProduct() {
        createProduct("product1");
    }
    @SuppressWarnings("unchecked")
    @Step("Create Product")
    public void createProduct(String productKey) {
        ProductApiHelper helper = new ProductApiHelper();
        ProductApiBuildingBlock product = new ProductApiBuildingBlock();
        JSONObject request = helper.loadProductTestData(productKey);
        request.put("title", helper.getUniqueProductName(request));
        product.verifyProduct(product.addNewProduct(request));
        helper.updateRuntimeData(productKey, request.get("title").toString(), product.getProductId());
        System.out.println("Product ID: " + product.getProductId());
    }
}