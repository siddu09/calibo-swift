package tests.dataProviders;

import org.testng.annotations.DataProvider;
import utils.ExcelReader;

public class LoginDataProvider {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        System.out.println(
                Thread.currentThread().threadId());
        ExcelReader reader = new ExcelReader();

        return reader.getSheetData(
                "src/test/resources/config/LoginData.xlsx",
                "LoginData");
    }
}