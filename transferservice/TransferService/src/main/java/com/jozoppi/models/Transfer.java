package com.jozoppi.models;

import com.jozoppi.exceptions.BalanceException;
import com.jozoppi.exceptions.TransferInvalidException;
import org.springframework.data.annotation.Id;


/**
 *  This entity models the transfer that has should be done
 *  between two different accounts.
 */
public class Transfer implements DomainEntity
{
    @Id
    private Long transactionId;
    private String sourceId;
    private String destinationId;
    private double amount;

    /**
     * Return the transaction identifier.
     * @return identifier of the transaction.
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * Set the transaction identifier
     * @param transactionId
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Set the source account from which we want to transfer money.
     * @param sourceId source Account.
     */
    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }

    /**
     * Get the source account
     * @return the account source.
     */
    public String getSourceId()
    {
        return this.sourceId;
    }

    /**
     * Set the destinationAccount, where you want to transfer the money.
     * @param destinationId
     */
    public void setDestinationId(String destinationId)
    {
        this.destinationId = destinationId;
    }

    /**
     * Get the destinationAccount.
     * @return a destination account.
     */
    public String getDestinationId()
    {
        return this.destinationId;
    }

    /**
     * Set amount to be transferred.
     * @param amount
     */
    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    /**
     * Get the amount to be transferred.
     * @return the amount to be transferred.
     */
    public double getAmount()
    {
        return this.amount;
    }

    /**
     *  Validate the account.
     */
    @Override
    public void Validate() {
        if (amount < 0) {
            throw new BalanceException("Cannot add negative value");
        }
        if (getSourceId().compareTo(destinationId) == 0)
        {
            throw new TransferInvalidException("Cannot transfer to the same account");
        }
    }
}
