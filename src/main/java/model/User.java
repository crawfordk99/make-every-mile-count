package model;

import java.util.Date;

public class User {
    private String _userId;
    private String _email;
    private String _passwordHash;
    private Date _createdAt;


    public User(String userId, String email, String passwordHash) {
        this._userId = userId;
        this._email = email;
        this._passwordHash = passwordHash;
        Date now = new Date();
        this._createdAt = now;
    }

    public String getUserId() {
        return _userId;
    }

    public String getEmail() {
        return _email;
    }

    public String getPasswordHash() {
        return _passwordHash;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }
}
