package com.jozoppi.common;
/**
 * @author  Giorgio Zoppi
 * @email <giorgio.zoppi@gmail.com>
 *  Base class for the response message.
 */

public class BaseResponse {
    private String message;
    public BaseResponse(String message)
    {
        this.message = message;
    }
    protected String getMessage()
    {
        return this.message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

}
