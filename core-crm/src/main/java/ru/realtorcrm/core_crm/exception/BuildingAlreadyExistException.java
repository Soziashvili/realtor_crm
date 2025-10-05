package ru.realtorcrm.core_crm.exception;

public class BuildingAlreadyExistException extends RuntimeException {
    public BuildingAlreadyExistException(String cadastre) {
        super("A building with cadastral number '" + cadastre + "' already exists");
    }
}
