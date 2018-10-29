/**
 * @author  Giorgio Zoppi
 * @email <giorgio.zoppi@gmail.com>
 *
 */
package com.jozoppi.controllers;
import com.jozoppi.business.AccountCheckRule;
import com.jozoppi.business.BusinessRule;
import com.jozoppi.common.AccountResponse;
import com.jozoppi.common.TransferResponse;
import com.jozoppi.exceptions.BalanceException;
import com.jozoppi.exceptions.TransferAccountNotFoundException;
import com.jozoppi.exceptions.TransferInvalidException;
import com.jozoppi.exceptions.UserNotFoundException;
import com.jozoppi.models.Account;
import com.jozoppi.models.Transfer;
import com.jozoppi.models.User;
import com.jozoppi.repositories.UsersRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.jozoppi.repositories.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 *  Controller for the transfer service in java.
 *  Its only business rule is the account check rule.
 *  The business rule will be checked at each transfer,\
 *  after account validation.
 */

@RestController
@RequestMapping("/transferservice")
public class TransferServiceController {

    private BusinessRule businessRule = new AccountCheckRule();

    @Autowired
    private AccountsRepository repository;
    @Autowired
    private UsersRepository usersRepository;

    /**
    * Returns the list of the accounts that belong to an user.
    * We suppose that in our model 1 user can have multiple accounts.
    * And 1 account belong exactly to one user.
    * @param userId user name
    * */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public List<Account> getAccountByUserName(@PathVariable("userId") String userId) throws Exception
    {
        List<Account> accountList = repository.findByUserId(userId);
        if (accountList.isEmpty())
        {
            throw new AccountNotFoundException();
        }
        return accountList;
    }

    /**
     * Creates an new account that belong to an user.
     * As result we return the account identifier to be used for transfer operations.
     * @param account
     * @return An JSON response with the identifier of the account included.
     */

    @PostMapping(value = "/create")
    public AccountResponse createAccount(@RequestBody Account account)

    {
        ValidateAccount(account);
        Account savedAccount  = repository.save(account);
        return new AccountResponse(savedAccount.getIdentifier().toHexString());
    }

    /**
     * Account validation. We found if the user is present.
     * We don't allow to operate not existing users and fake accounts.
     * @param account
     */
    private void ValidateAccount(Account account)
    {
        account.Validate();
        String userId = account.getUserId();
        Optional<User> user = usersRepository.findByUsername(userId);
        if (!user.isPresent())
        {
            throw new UserNotFoundException("Username "+ userId + " not valid");
        }
    }

    /**
     * Transfer operation:
     * 1. We validate that the user of the source account exits and it is authenticated
     * inside spring security framework.
     * 2. We rollback to previous value if the account become negative and throw exception
     * BalanceException since we violate the BusinessRule.
     *
     * @param transfer Transfer descriptor
     * @return A transfer response or an exception. The transfer response contains
     * the tuple t = (sourceAccountId, DestinationAccountId, amount)
     * @throws Exception
     */
    @PostMapping(value = "/transfer")
    public TransferResponse transferAccount(@RequestBody Transfer transfer) throws Exception  {
        // we we validate the transfer as first
        transfer.Validate();


        // i shall be sure about the user authenticated
        // and get its credentials.
        // if should be not possible to transfer arbitrary accounts.
        // the only transfer possible is from a logged user to another existing account.


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) auth.getPrincipal();
        List<Account> userAccounts = repository.findByUserId(userName);
        if (userAccounts.isEmpty())
        {
            throw new AccountNotFoundException("User not found for username: " + userName);
        }


        // Each account has an id. So if the sourceId is in the user logged account.
        if (!hasAccountId(userAccounts, transfer.getSourceId()))
        {
            throw new TransferInvalidException("Unauthorized transfer for user: "+ transfer.getSourceId() +"" + userAccounts.get(0).getIdentifier().toHexString());
        }

        // ok if no exception has been done. The transfer is a valid one.

        String sourceAccountId = transfer.getSourceId();
        String transferAccountId = transfer.getDestinationId();
        Optional<Account> sourceAccount;
        Optional<Account> transferAccount;
        ObjectId sourceId = new ObjectId(sourceAccountId);
        ObjectId destinationId = new ObjectId(transferAccountId);
        sourceAccount = repository.findByIdentifier(sourceId);
        transferAccount = repository.findByIdentifier(destinationId);

        if (!sourceAccount.isPresent())
            throw new TransferAccountNotFoundException();

        if (!transferAccount.isPresent()) {
            throw new TransferAccountNotFoundException();
        }
        Account source = sourceAccount.get();
        Account destination = transferAccount.get();
        ValidateAccount(source);
        ValidateAccount(destination);

        source.withDraw(transfer.getAmount());
        try {
            businessRule.CheckRequest(sourceAccount.get());
        } catch (Exception ex) {
            source.rollBack();
            throw new BalanceException("Account outdrawn. Cannot transfer money");
        }
        destination.add(transfer.getAmount());
        saveAccounts(source, destination);
        return new TransferResponse(source.getIdentifier().toHexString(),
                                    destination.getIdentifier().toHexString(),
                                    transfer.getAmount());
    }

    /**
     * helper method for saving accounts.
     * @param source sourceAccount
     * @param destination destinationAccount.
     */
    private void saveAccounts(Account source, Account destination)
    {
        repository.save(source);
        repository.save(destination);
    }

    /**
     * Check if the account has been in the list of account beloging
     * to a given user.
     * @param accountList List of accounts
     * @param id user identifier
     * @return true if the account belongs to an user, false otherwise
     */
    private boolean hasAccountId(List<Account> accountList, String id)
    {
        for (Account account: accountList)
        {
            if (account.getIdentifier().toHexString().contentEquals(id))
            {
                return true;
            }
        }
        return false;
    }
}
