package com.example.notesapp.repository.exeptions;

public class IncorrectRegisterDataExeption extends RuntimeException {
    public IncorrectRegisterDataExeption(String message) {
        super(message);
    }
}
