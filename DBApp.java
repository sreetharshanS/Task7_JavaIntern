import java.sql.*;
import java.util.Scanner;

public class DBApp {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";   
    private static final String PASSWORD = "sree"; 

    
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to DB ");

            while (true) {
                System.out.println("\n1.Add  2.View  3.Update  4.Delete  5.Exit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();
                sc.nextLine(); 

                switch (choice) {
                    case 1:
                    addUser(conn, sc);
                    break;
                    case 2:
                    viewUsers(conn);
                    break;
                    case 3:
                    updateUser(conn, sc);
                    break;
                    case 4:
                    deleteUser(conn, sc);
                    break;
                    case 5:
                    { conn.close(); return; }
                    default:
                    System.out.println("Invalid!");
                    break; 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        String sql = "INSERT INTO users(name, email) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            System.out.println("User added.... in DB");
        }
    }

    private static void viewUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Users in DB:");
            while (rs.next()) {
                System.out.printf("%d | %s | %s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("email"));
            }
        }
    }

    private static void updateUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter user ID to update: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter new email: ");
        String email = sc.nextLine();

        String sql = "UPDATE users SET email=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Updated " : "User not found ");
        }
    }

    private static void deleteUser(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter user ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Deleted " : "User not found ");
        }
    }

    @Override
    public String toString() {
        return "DBApp []";
    }
}
