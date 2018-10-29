package com.jozoppi.repositories;

import com.jozoppi.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * UsersRepository. Repository associated to mongodb.
 */
public interface UsersRepository extends MongoRepository<User, Long>
{
    /**
     *
     * @param userName Username to be user.
     * @return The user related to an userName.
     */
    Optional<User> findByUsername(String userName);

}

