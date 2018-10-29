package com.jozoppi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  The user validation was not correct since the user exists.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "User already exists")
public class MalformedUserException extends RuntimeException {
}
