package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.viewmodel.AuthVM;
import com.example.notesapp.viewmodel.NotepadsVM;

public class NotepadsActivity extends AppCompatActivity {
    private NotepadsVM notepadsVM;
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Проверка наличия хранилища
        AppStorage.initialize(this);
        notepadsVM = new ViewModelProvider(this).get(NotepadsVM.class);
        // Проверяем наличие токена
        if (AppStorage.getInstance().isJwtTokenExists()) {
            jwtToken = AppStorage.getInstance().getJwtToken();
            notepadsVM.loadUser(jwtToken);
            notepadsVM.loadNotepads(jwtToken);

            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_notepads);

            // Инициализация UI
            initializeUI();

            // Наблюдаем за сообщениями
            notepadsVM.getNotepadsLiveData().observe(this, message -> {
                if (message != null) {
                    Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
            notepadsVM.getUserLiveData().observe(this, message -> {
                if (message != null) {
                    Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (message.equals(notepadsVM.getUserRep().getSuccessResponse())){
                        notepadsVM.saveUserInStorage();
                    }
                }
            });
        } else {
            goToRegisterActivity();
        }
    }

    private void initializeUI() {
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(NotepadsActivity.this, RegActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}