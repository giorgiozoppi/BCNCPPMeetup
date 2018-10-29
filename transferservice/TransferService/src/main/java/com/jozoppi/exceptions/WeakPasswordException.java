package com.jozoppi.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Password is insecure, we require more than 4 characters lowercase and uppercase")
public class WeakPasswordException extends RuntimeException {
}
