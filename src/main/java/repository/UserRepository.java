package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

/**
 * Data access object for User operations.
 * Handles creation and retrieval of user accounts.
 */
public class UserRepository {
    // private String _dbUrl;
    // private String _dbUser;
    // private String _dbPassword;

    private final Connection _connection;

    public UserRepository(Connection connection) {
        this._connection = connection;
    }

    /**
     * Create a new user with email and password hash.
     * @return the new user's ID, or -1 if creation failed
     */
    public int createUser(String email, String passwordHash) {
        String sql = "INSERT INTO users (email, password_hash) VALUES (?, ?) RETURNING id";

        try (PreparedStatement stmt = _connection.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieve a user by email.
     * @return the User object, or null if not found
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT id, email, password_hash FROM users WHERE email = ?";

        try (PreparedStatement stmt = _connection.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userId = String.valueOf(rs.getInt("id"));
                String retrievedEmail = rs.getString("email");
                String passwordHash = rs.getString("password_hash");
                return new User(userId, retrievedEmail, passwordHash);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Verify user credentials.
     * @return the user ID if credentials match, or null if not found/invalid
     */
    public String verifyUser(String email, String passwordHash) {
        User user = getUserByEmail(email);
        if (user != null && user.getPasswordHash().equals(passwordHash)) {
            return user.getUserId();
        }
        return null;
    }
}
