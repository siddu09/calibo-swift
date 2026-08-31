package tests.dataProviders.DSO;


import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

public class DSO_UpdateRunManager {
    public static void updateCellUsingFillo(String filePath, String sheetName, String testCaseName, String targetColumn, String newValue) {
        Connection connection = null;
        try {
            Fillo fillo = new Fillo();
            connection = fillo.getConnection(filePath);

            // Assume your 1st column header name is "TestCaseID"
            // Query string structure: UPDATE SheetName SET TargetColumn='PASS' WHERE TestCaseID='Your_Test_Name'
            String query = String.format("UPDATE %s SET %s='%s' WHERE `TestCase Name`='%s'", sheetName, targetColumn, newValue, testCaseName);

            // CRITICAL: Fillo uses executeUpdate for write operations
            connection.executeUpdate(query);
            System.out.println("Fillo successfully updated row: " + testCaseName);

        } catch (Exception e) {
            System.err.println("Fillo failed to update the spreadsheet: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close(); // Cleanly close the database connection stream
            }
        }
    }
}
