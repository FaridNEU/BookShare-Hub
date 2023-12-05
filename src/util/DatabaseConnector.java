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
    private static final String URL = "jdbc:mysql://localhost:3306/bookShare?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2250";

    private DatabaseConnector() {
    }
    
    //addUser(user)  OKEY
    public static void addUser(User user) {
        System.out.println("I'm Here! addUser");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("I'm Here! db");
            String sql = "INSERT INTO Users (username, email, password, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, "Normal");
                preparedStatement.executeUpdate();
                System.out.println("User added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //updateUser(oldUser, newUser)   OKEY
    public static void updateUser(User oldUser, User newUser) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE Users SET username=?, email=?, password=? WHERE user_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newUser.getUsername());
                preparedStatement.setString(2, newUser.getEmail());
                preparedStatement.setString(3, newUser.getPassword());
                preparedStatement.setInt(4, oldUser.getUserId());
                preparedStatement.executeUpdate();
                System.out.println("User updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //getAllUser() OKEY
    public static List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int userId = resultSet.getInt("user_id");
                        String username = resultSet.getString("username");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");

                        User user = new User(userId, username, email, password);
                        allUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }
    
    
    //removeBook(bookID) OKEY
    public static void removeBook(Book book) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM Books WHERE book_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, book.getBookId());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Book removed successfully!");
                } else {
                    System.out.println("Book with ID " + book.getBookId() + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    //addBook   OKEY
    public static int addBook(Book book) {
        int bookId = -1;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO Books (title, author, description, availability) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setString(2, book.getAuthor());
                preparedStatement.setString(3, book.getDescription());
                preparedStatement.setBoolean(4, book.isAvailability());
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

////////////////////////////////////////////////////////////////////////////////////////////
    //addLoanRequest(loanRequest) OKEY
    public static void addLoanRequest(LoanRequest loanRequest) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO LoanRequests (borrower_id, lender_id, book_id, request_status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, loanRequest.getBorrowerId());
                preparedStatement.setInt(2, loanRequest.getLenderId());
                preparedStatement.setInt(3, loanRequest.getBookRequestedId());
                preparedStatement.setString(4, loanRequest.getRequestStatus());
                preparedStatement.executeUpdate();
                System.out.println("Loan request added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //removeLoanRequest(requestID)  OKEY
    public static void removeLoanRequest(int requestID) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM LoanRequests WHERE request_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, requestID);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Loan request removed successfully!");
                } else {
                    System.out.println("Loan request with ID " + requestID + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //getAllLoanRequestByUser(user)   OKEY
    public static List<LoanRequest> getAllLoanRequestsByUser(User user) {
        List<LoanRequest> userLoanRequests = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM LoanRequests WHERE borrower_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, user.getUserId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int requestId = resultSet.getInt("request_id");
                        int borrowerId = resultSet.getInt("borrower_id");
                        int lenderId = resultSet.getInt("lender_id");
                        int bookId = resultSet.getInt("book_id");
                        String requestStatus = resultSet.getString("request_status");

                        LoanRequest loanRequest = new LoanRequest(requestId, borrowerId, lenderId, bookId, requestStatus);
                        userLoanRequests.add(loanRequest);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userLoanRequests;
    }
    
    //recieveAllLoanRequestFromOtherUser(user)  OKEY
    public static List<LoanRequest> recieveAllLoanRequestFromOtherUser(User user) {
        List<LoanRequest> userLoanRequests = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM LoanRequests WHERE lender_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, user.getUserId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int requestId = resultSet.getInt("request_id");
                        int borrowerId = resultSet.getInt("borrower_id");
                        int lenderId = resultSet.getInt("lender_id");
                        int bookId = resultSet.getInt("book_id");
                        String requestStatus = resultSet.getString("request_status");

                        LoanRequest loanRequest = new LoanRequest(requestId, borrowerId, lenderId, bookId, requestStatus);
                        userLoanRequests.add(loanRequest);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userLoanRequests;
    }
    
    //acceptLoanRequest(requestID)  OKEY
    public static void acceptLoanRequest(int requestID) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE LoanRequests SET request_status = 'approved' WHERE request_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, requestID);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Loan request approved successfully!");
                } else {
                    System.out.println("Loan request with ID " + requestID + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //denyLoanRequest(requestID)  OKEY
    public static void denyLoanRequest(int requestID) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE LoanRequests SET request_status = 'denied' WHERE request_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, requestID);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Loan request denied successfully!");
                } else {
                    System.out.println("Loan request with ID " + requestID + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //?
    //getUserBorrowedBook(user)
    //addUserBorrowedBook(user, book)
    //removeUserBorrowedBook(user, bookID)
    
}