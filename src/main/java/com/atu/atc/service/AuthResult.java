/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.service;

import com.atu.atc.model.User;
import java.util.Optional;

/**
 *
 * @author Xuan
 */

public class AuthResult {
    private final boolean success;
    private final Optional<User> authenticatedUser;
    private final String message;

    public AuthResult(User authenticatedUser, String message) {
        this.success = true;
        this.authenticatedUser = Optional.of(authenticatedUser);
        this.message = message;
    }

    public AuthResult(String message) {
        this.success = false;
        this.authenticatedUser = Optional.empty();
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Optional<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public String getMessage() {
        return message;
    }
}
