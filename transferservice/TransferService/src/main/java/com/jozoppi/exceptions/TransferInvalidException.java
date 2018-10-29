package com.jozoppi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  The transfer has not valid so we stop the user with a bad request.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Transfer is not valid!")

public class TransferInvalidException extends RuntimeException
{
    public TransferInvalidException(String msg)
    {
        super(msg);
    }
}
