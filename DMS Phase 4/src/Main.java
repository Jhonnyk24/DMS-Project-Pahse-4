public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true"); // Allow fade effect

        java.sql.Connection connection = DBConnectionDialog.showDialog(null);

        if (connection == null) {
            System.out.println("No database connection. Program exiting.");
            return;
        }

        MovieDatabaseManager db = new MovieDatabaseManager(connection);
        new MovieGUI(db);
    }
}
