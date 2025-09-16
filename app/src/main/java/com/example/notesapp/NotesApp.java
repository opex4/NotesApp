package com.example.notesapp;

import android.app.Application;

import com.example.notesapp.appStorage.AppStorage;

public class NotesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Инициализация хранилища
        AppStorage.initialize(this);
    }
}