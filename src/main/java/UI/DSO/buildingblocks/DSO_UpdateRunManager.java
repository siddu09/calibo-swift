package UI.DSO.buildingblocks;


import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DSO_UpdateRunManager {
    public static void updateCellUsingFillo(String filePath, String sheetName, String testCaseName, String targetColumn, String newValue) {
        Connection connection = null;
        try {
            Fillo fillo = new Fillo();
            connection = fillo.getConnection(filePath);

            // Assume your 1st column header name is "TestCaseID"
            // Query string structure: UPDATE SheetName SET TargetColumn='PASS' WHERE TestCaseID='Your_Test_Name'
            String query = String.format("UPDATE %s SET `%s`='%s' WHERE `TestCase Name`='%s'", sheetName, targetColumn, newValue, testCaseName);

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

    public static String getCellUsingFillo(String filePath, String sheetName, String testCaseName, String targetColumn) {
        Connection connection = null;
        Recordset recordset = null;
        try {
            Fillo fillo = new Fillo();
            connection = fillo.getConnection(filePath);

            // Assume your 1st column header name is "TestCaseID"
            // Query string structure: UPDATE SheetName SET TargetColumn='PASS' WHERE TestCaseID='Your_Test_Name'
            String query = String.format("SELECT * FROM  `%s` WHERE `TestCase Name`='%s'", sheetName, testCaseName);

            // CRITICAL: Fillo uses get for write operations
            recordset = connection.executeQuery(query);
            if (recordset.next()) {
                // Fetch the cell value by its Column Header
                String cellValue = recordset.getField(targetColumn);
                System.out.println("The cell value is: " + cellValue);
                return cellValue; // Return the cell value immediately after fetching it
            } else {
                System.out.println("No matching row found.");
            }

        } catch (Exception e) {
            System.err.println("Fillo failed to update the spreadsheet: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close(); // Cleanly close the database connection stream
            }
        }
        return null; // Return null if no matching row is found or an exception occurs
    }


}
