import java.io.IOException;
import java.sql.*;

public class SQL {
    public static void createBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                System.out.println("Connection to Store DB succesfull!");
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        return DriverManager.getConnection("jdbc:mysql://localhost/data", "user","password");
    }

    public static void upDate(String dateCurrent, int sumSale, int sumOrder, int sumSaleMoney, String s) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                boolean check = false;
                String cdate = "";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM saleorderstat");
                while (resultSet.next()) {
                    cdate = resultSet.getString("cdate");
                    if (cdate.equals(dateCurrent)) {
                        check = true;
                        statement.executeUpdate("UPDATE saleorderstat SET sumSale = " +  sumSale + ", sumOrder = " + sumOrder + ", sumSaleMoney = " + sumSaleMoney + ", popItem = '" + s + "' WHERE Id = " + resultSet.getInt("Id"));
                        break;
                    }
                }
                if (!check) {
                    statement.executeUpdate("INSERT saleorderstat(cdate, sumSale, sumOrder, sumSaleMoney, popItem) VALUES ('" + dateCurrent + "', " + sumSale + ", " + sumOrder + ", " + sumSaleMoney + ", '" + s + "')");
                }

            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
