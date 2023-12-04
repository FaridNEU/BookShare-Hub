/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.sql.*;
import java.util.ArrayList;
import model.User;
import model.Book;
import model.LoanRequest;
import model.LibraryCatalog;


/**
 *
 * @author Farid
 */
public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2250";

    private DatabaseConnector() {
    }
  
    
    //addUserOwnedBook(user, book)
    //getUserOwnedBook(user)
    //getAllBook()
    //searchByTitle(title)
    //searchByAuthor(author)
    //getUserBorrowedBook(user)
    //addUserBorrowedBook(user, book)
    //removeUserBorrowedBook(user, bookID)
    //getAllLoanRequestByUser(user)
    //addLoanRequest(user, book, ownerUser)
    //recieveLoanRequest(user)
    //acceptLoanRequest(loanRequestID)
    //denyLoanRequest(loanRequestID)
    
    
    public static ArrayList<User> getAllusers() {
        // return list of users from db
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME,PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                User u = new User();
                u.setName(rs.getString("name"));
                u.setAge(rs.getInt("age"));
                u.setId(rs.getInt("id"));
                users.add(u);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public static void addUser(User user) {
        //add to database
        String query = "INSERT INTO Users(NAME,AGE) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME,PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            int rows = stmt.executeUpdate();
            System.out.println("Rows impacted : " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   

    public static void deleteUser(User u) {
        String query = "delete from Users where id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME,PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, u.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editUser(User oldUser, User newUser) {
        String query = "UPDATE Users SET name=?, age=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME,PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newUser.getName());
            stmt.setInt(2, newUser.getAge());
            stmt.setInt(3, oldUser.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
