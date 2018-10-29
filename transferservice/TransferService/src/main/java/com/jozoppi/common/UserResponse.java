package com.jozoppi.common;
/**
 * @author  Giorgio Zoppi
 * @email <giorgio.zoppi@gmail.com>
 *  Customizing the response to success queries in case of user..
 */
public class UserResponse extends  BaseResponse{

    private String userName;

    public UserResponse(String username) {
        super("User created with success!");
        this.userName = username;
    }
    public String getUserName()
    {
        return this.userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
