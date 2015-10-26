package com.cmu.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {
 
    public String encodePassword(String rawPass, Object salt) throws DataAccessException {
        return BCrypt.hashpw(rawPass, BCrypt.gensalt());
    }
 
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
        return BCrypt.checkpw(rawPass, encPass);
    }
}
