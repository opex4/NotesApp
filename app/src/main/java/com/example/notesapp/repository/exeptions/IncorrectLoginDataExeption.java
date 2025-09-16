package com.example.notesapp.repository.exeptions;

public class IncorrectLoginDataExeption extends RuntimeException {
    public IncorrectLoginDataExeption(String message) {
        super(message);
    }
}
