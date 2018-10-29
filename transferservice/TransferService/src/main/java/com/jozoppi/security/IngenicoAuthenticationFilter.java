
package com.jozoppi.security;

/**
 *  This is the package related to the authentication JWT token support.
 *  We extend the Spring Authentication filter for allowing a JWT Token.
 *  we use the credentials of the model class User and we remap
 *  to Spring Security credentials.
 */

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class IngenicoAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    /**
     * Constructor
     * @param authenticationManager Spring Security authenticationManager
     */
    public IngenicoAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * We override authentication for the allowing JWT Tokens.
     * @param req Input request
     * @param res Output response
     * @return An autentication context.
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            com.jozoppi.models.User credentials = new ObjectMapper()
                    .readValue(req.getInputStream(), com.jozoppi.models.User.class);



            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  We override to create a token for the authentication
     * @param req    Request to be used
     * @param res    Response to be use
     * @param chain  Chain to be used.
     * @param auth   Authentication context.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConst.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConst.SECRET.getBytes()));
        res.addHeader(SecurityConst.HEADER_STRING, SecurityConst.TOKEN_PREFIX + token);
    }
}
