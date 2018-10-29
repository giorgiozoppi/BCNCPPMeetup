package com.jozoppi.security;

import com.jozoppi.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static java.util.Collections.emptyList;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 *  This class is useful for remap the model user to the spring security user
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UsersRepository applicationUserRepository;

    public UserDetailsServiceImpl(UsersRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.jozoppi.models.User> applicationUser = applicationUserRepository.findByUsername(username);
        if (!applicationUser.isPresent())
        {
            throw new UsernameNotFoundException(username);
        }
        com.jozoppi.models.User userName = applicationUser.get();
        return new User(userName.getUsername(), userName.getPassword(), emptyList());
    }
}
