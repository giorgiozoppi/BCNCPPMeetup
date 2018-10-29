package com.jozoppi;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication

/**
 * Main starter of the  Spring 2 application.
 */
public class TransferServiceApplication 
{

    
	public static void main(String[] args) 
	{
        
		SpringApplication.run(TransferServiceApplication.class, args);
	}

	/**
	 *  We register a bean for Niels Provos Blowfish Crypt.
	 * @return Encoder for the crypt.
	 */

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
