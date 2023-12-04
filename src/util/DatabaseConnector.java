/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    
    //addUser()
    //updateUser()
    //addBook(book)
    //removeBook(book)
        //getBookBy?
    
    
    //addBook   OKEY
    public static int addBook(Book book) {
        int bookId = -1;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO Books (title, author, description, availability) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setString(2, book.getAuthor());
                preparedStatement.setString(3, book.getDescription());
                preparedStatement.setBoolean(4, book.isAvailable());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    bookId = resultSet.getInt(1);
                    System.out.println("Book added successfully with ID: " + bookId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookId;
    }
    
    //addUserOwnedBook(user, book)  OKEY
    public static void addUserOwnedBook(User user, Book book) {
        int bookID = addBook(book);
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO UserBooks (user_id, book_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, user.getUserId());
                preparedStatement.setInt(2, bookID);
                preparedStatement.executeUpdate();
                System.out.println("Book added to user's owned books successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //getUserOwnedBook(user)   OKEY
    public static List<Book> getUserOwnedBooks(User user) {
        List<Book> ownedBooks = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT Books.* FROM Books JOIN UserBooks ON Books.book_id = UserBooks.book_id WHERE UserBooks.user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, user.getUserId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("book_id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        String description = resultSet.getString("description");
                        boolean availability = resultSet.getBoolean("availability");

                        Book book = new Book(bookId, title, author, description, availability);
                        ownedBooks.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ownedBooks;
    }
  
    //getAllBook()  OKEY
    public static List<Book> getAllBooks() {
        List<Book> allBooks = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Books";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("book_id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        String description = resultSet.getString("description");
                        boolean availability = resultSet.getBoolean("availability");

                        Book book = new Book(bookId, title, author, description, availability);
                        allBooks.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooks;
    }
    
    //browsBookByTitle(title)  OKEY
    public static List<Book> browseBookByTitle(String title) {
        List<Book> booksWithTitle = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Books WHERE title LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + title + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("book_id");
                        String bookTitle = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        String description = resultSet.getString("description");
                        boolean availability = resultSet.getBoolean("availability");

                        Book book = new Book(bookId, bookTitle, author, description, availability);
                        booksWithTitle.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksWithTitle;
    }    
    
    //browsBookByAuthor(author)  OKEY
    public static List<Book> browseBookByAuthor(String author) {
        List<Book> booksByAuthor = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Books WHERE author LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + author + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("book_id");
                        String title = resultSet.getString("title");
                        String bookAuthor = resultSet.getString("author");
                        String description = resultSet.getString("description");
                        boolean availability = resultSet.getBoolean("availability");

                        Book book = new Book(bookId, title, bookAuthor, description, availability);
                        booksByAuthor.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksByAuthor;
    }

    //getAllLoanRequestByUser(user)
    //addLoanRequest(user, book, ownerUser)
    //recieveLoanRequest(user)
    //acceptLoanRequest(loanRequestID)
    //denyLoanRequest(loanRequestID)
    
    //getUserBorrowedBook(user)
    //addUserBorrowedBook(user, book)
    //removeUserBorrowedBook(user, bookID)
    
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
