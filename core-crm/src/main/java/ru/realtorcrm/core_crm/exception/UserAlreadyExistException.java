package ru.realtorcrm.core_crm.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super("User with " + message + " already exists");
    }
}
