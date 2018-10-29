package com.jozoppi.models;

import com.jozoppi.exceptions.BalanceException;
import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.locks.ReentrantLock;

/**
 *  Entity for the payment account.
 *  Each entity implements a DomainEntity,
 *  as common interface for validation. We label each account with a name.
 *  I have decided to not use Data Transfer Object for simplicity.
 *  Usually you don't send your entity around, but you map them
 *  to the data transfer object.
 *  Each user can have multiple accounts.
 *  Each account contains as foreign key, the user identifier.
 *
 */

@Document(collection = "accounts")
public class Account implements DomainEntity, Comparable<Account>
{
    @Id
    public ObjectId identifier;
    private String name;
    private String userId;
    private double balance;
    private double lastBalance;
    private ReentrantLock lock = new ReentrantLock();


    /**
    * Accessor for the identifier.
    * */
    public void setIdentifier(ObjectId identifier)
    {
        this.identifier = identifier;
    }

    /**
     *  Return the identifier.
     * @return identifier for the account.
     */
    public  ObjectId getIdentifier()
    {
        return this.identifier;
    }

    /**
     *  Set the balance
     * @param balance value to set the account
     */
    public void setBalance(double balance)
    {

        this.balance = balance;
    }

    /**
     * Retrieve the balance
     * @return return the balance
     */
    public double getBalance() {

        return this.balance;
    }

    /**
     * Set the name to the account.
     * @param name label of the account.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the name of the account
     * @return label the account
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * The user identifier. The user identifier is the hexadecimal of mongodb ObjectId.
     * @param userId  Value of the user identifier
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * Accessor method for the UserId.
     * @return the user identifier.
     */
    public String getUserId()
    {
        return this.userId;
    }

    /**
     * Add an amount to the balance.
     * The amount shall be not negative.
     * @param amount value to be added.
     */
    public void add(double amount)
    {
        lock.lock();
        if (amount > 0)
        {
            this.balance+= amount;
        }
        lock.unlock();
    }

    /**
     * Withdraw an amount from th account
     * @param amount value to be withdrawn.
     */
    public void withDraw(double amount)
    {
        lock.lock();
        if (this.balance > 0) {
            lastBalance = this.balance;
        }
        this.balance-=amount;
        lock.unlock();
    }

    public void rollBack()
    {
        lock.lock();
        this.balance = lastBalance;
        lock.unlock();
    }

    /**
     * Validate the entity and you can detect overdrawn
     * and send a balance exception.
     *
     *   @throws BalanceException  Exception when account < 0
     */
    @Override
    public void Validate() throws BalanceException {
        lock.lock();
        if (balance < 0)
        {
            lock.unlock();
            throw new BalanceException("Negative Balance");
        }
        lock.unlock();
    }

    /**
     * Compare two accounts following the account identifier.
     * */
    @Override
    public int compareTo(Account o)
    {
        return (o.getIdentifier().compareTo(o.getIdentifier()));
    }
}
