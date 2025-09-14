package com.example.notesapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.appStorage.AppStorage;

public class NotepadsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Проверка наличия хранилища
        AppStorage.initialize(this);

        // Проверяем наличие токена
        if (AppStorage.getInstance().isJwtTokenExists()) {
            // todo проверить состояние сессии
        } else {
            // todo начать сессию заново
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notepads);
    }
}