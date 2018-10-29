package com.jozoppi.common;

/**
 *  Customizing the response to success queries in case account.
 */
public final class AccountResponse extends BaseResponse
{
    private String identifier;
    private static final String SUCCESS_MESSAGE = "Account created with success";

    public AccountResponse(String identifier)
    {
        super(SUCCESS_MESSAGE);
        this.identifier = identifier;
    }
    public String getIdentifier()
    {
        return  this.identifier;
    }
    public  void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
}
