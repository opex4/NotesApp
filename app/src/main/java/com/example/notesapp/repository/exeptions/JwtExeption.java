package com.example.notesapp.repository.exeptions;

public class JwtExeption extends RuntimeException {
    public JwtExeption(String message) {
        super(message);
    }
}
