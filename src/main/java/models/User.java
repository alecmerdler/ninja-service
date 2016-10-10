package models;

import com.google.inject.Inject;

import javax.persistence.*;

/**
 * Created by alec on 10/4/16.
 */

@Entity
public class User extends Model {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Inject
    public User() {
        this.username = "";
        this.email = "";
    }

    @Inject
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
