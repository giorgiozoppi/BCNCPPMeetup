package com.jozoppi.models;

import com.jozoppi.exceptions.WeakPasswordException;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 *  Model for the users in the system.
 *  A distribute system always requires authentication.
 */
public class User implements  DomainEntity
{
    @Id
    private ObjectId identifier;
    private String username;
    private String password;

    public User() {}
    /**
     *   @param  username username.
     *   @param  password password.
     */
    public User(ObjectId identifer, String username, String password) {
        this.identifier = identifer;
        this.username = username;
        this.password = password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return username; }

    @Override
    public void Validate() throws Exception
    {
        // owasp security requirement for password
        if (!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,100}$"))
        {
            throw new WeakPasswordException();
        }
    }
}
