package UI.DPS.Constants;

import java.util.List;

public class Constants {

    public static final String ORDERS_EXCEL =
            "src/test/resources/testdata/files/DPS/orders_table.xlsx";

    public static final String ORDERS_SHEET = "Orders";

    public static final List<String> ORDERS_COLUMNS = List.of(
            "ID",
            "ORDERID",
            "CUSTOMERID",
            "ORDERDATE",
            "PRODUCTNAME",
            "ORDERAMOUNT",
            "DISCOUNTPERCENTAGE",
            "ISRETURNED",
            "PAYMENTSTATUS",
            "DELIVERYSTATUS"
    );
}
