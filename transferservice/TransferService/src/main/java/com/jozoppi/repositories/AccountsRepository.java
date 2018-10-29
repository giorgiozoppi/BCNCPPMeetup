package com.jozoppi.repositories;

import com.jozoppi.models.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the payment accounts.
 * It uses the mongodb backend as data store. This makes a lot of sense for
 * both perfomance and the fact that the use case is simple and
 * we do not have complex queries.
 */
public interface AccountsRepository extends MongoRepository<Account, String>
{
    /**
     * Find an account from the identifier.
     * @param sourceAccountId
     * @return An account.
     */
    Optional<Account> findByIdentifier(ObjectId sourceAccountId);

    /**
     * Find an account related to a name.
     * @param userId
     * @return An account.
     */
    List<Account> findByUserId(String userId);
}
