package com.jozoppi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  The account has not found so we trigger an exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account is not found")
public class TransferAccountNotFoundException extends RuntimeException {
}
