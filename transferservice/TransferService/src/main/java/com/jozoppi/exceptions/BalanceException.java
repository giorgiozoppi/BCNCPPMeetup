package com.jozoppi.exceptions;

/**
 *  Exception shall be used in exceptional event, in this case the exceptional event
 *  is a violation of the business contract.
 */
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Balance cannot be negative")

public class BalanceException extends RuntimeException
{
    public BalanceException(String negative_balance)
    {
        super(negative_balance);
    }
}
