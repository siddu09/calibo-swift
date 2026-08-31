package tests.dataProviders;

import org.testng.annotations.DataProvider;
import utils.FilloUtil;

public class FilloLoginDataProvider {

    @DataProvider(name = "filloLoginData")
    public Object[][] loginData() {

        return FilloUtil.getLoginData();
    }
}