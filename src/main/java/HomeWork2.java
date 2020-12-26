import java.sql.*;

public class HomeWork2 {
    private static Connection conn;
    private static Statement stmt;

    public static void main(String[] args) throws SQLException {

        try {
            connection();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        stmt.execute("CREATE TABLE IF NOT EXISTS goods (\n" +
                "good_id integer PRIMARY KEY,\n" +
                "good_name VARCHAR NOT NULL,\n" +
                "good_price int NOT NULL" +
                ");");

        System.out.println("Таблица создана");

        stmt.execute("Удалить из товара");

        System.out.println("Удаленные данные");

        conn.setAutoCommit(false);
        for (int i = 0; i < 10000; i++) {
            stmt.addBatch("INSERT INTO goods (good_name, good_price) VALUES ('Name" + (i+1) + "', " + (i+1) + ")");
        }
        stmt.executeBatch();
        conn.setAutoCommit(true);

        System.out.println("Обновление данных");


        findPriceForProduct("Name34");
        findPriceForProduct("Name10004");


        replacePriceForProduct("Name55", "1234");


        findGoodInPrice(50,60);

        disconnect();
    }

    private static void findGoodInPrice(int priceFrom, int priceTo) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT good_name,good_price FROM goods WHERE good_price >= '" + priceFrom + "' and good_price <= '" + priceTo + "';");

        System.out.println("good_name | good_price");
        while (rs.next()) {
            System.out.println(rs.getString("good_name") + " | " + rs.getInt(2));
        }
    }

    private static void replacePriceForProduct(String name, String price) throws SQLException {
        System.out.print("To replace ");
        findPriceForProduct(name);
        stmt.executeUpdate("UPDATE goods SET good_price = '" + price + "' WHERE good_name = '" + name + "';");
        System.out.print("После замены ");
        findPriceForProduct(name);
    }

    private static void findPriceForProduct(String s) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT good_price FROM goods WHERE good_name = '" + s + "';");

        if (rs.next()) {
            System.out.println("Цена: " + rs.getInt(1));
        }
        else {
            System.out.println("Продукт не найден!");
        }
    }

    private static void connection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/java/resources/database.db");
        stmt = conn.createStatement();
    }

    private static void disconnect() throws SQLException {
        conn.close();
    }

   }
