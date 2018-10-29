package com.jozoppi.controllers;
import com.jozoppi.common.UserResponse;
import com.jozoppi.exceptions.MalformedUserException;
import com.jozoppi.exceptions.UserNotFoundException;
import com.jozoppi.models.User;
import com.jozoppi.repositories.UsersRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 *  This controller has the responsibility to sign up new users.
 *  It uses Niels Provos, blowfish crypt. The blowfish crypt has been designed
 *  as alternative to normal unix crypt by Provos in 1999 and it protects against
 *  rainbow attacks. It has been ported into Spring frameworkto BCryptPasswordEncoder.
 *  I will check if the user name exists before the sign up.
 *  @see https://www.usenix.org/event/usenix99/provos.html
 */
@RestController
@RequestMapping("/users")
public class UsersController {


    @Autowired
    private UsersRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Constructor
     * @param applicationUserRepository Repository for retrieving the users.
     * @param bCryptPasswordEncoder Encoder for the bcrypt.
     */
    public UsersController(UsersRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Retrieve the user information except the password.
     * @param name username to look for.
     * @return JSON of the user or UserNotFoundException.
     */
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public User getUserByName(@PathVariable("name") String name)
    {
        Optional<User> exists = applicationUserRepository.findByUsername(name);
        if (!exists.isPresent())
        {
            throw new UserNotFoundException("User not found");
        }
        User value = exists.get();
        value.setPassword(null);
        return exists.get();
    }
    /**
     * Create a new user.
     * @param user user description.
     */
    @PostMapping("/sign-up")
    public UserResponse signUp(@RequestBody User user) throws Exception {
            // first validate.
            user.Validate();
            Optional<User> exists = applicationUserRepository.findByUsername(user.getUsername());
            if (exists.isPresent())
            {
                throw new MalformedUserException();
            }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = applicationUserRepository.save(user);
        return new UserResponse(savedUser.getUsername());
    }
}
