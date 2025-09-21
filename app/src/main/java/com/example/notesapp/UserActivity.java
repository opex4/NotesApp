package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.appStorage.AppStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserActivity extends AppCompatActivity {

    private TextView username, email;
    private FloatingActionButton returnBtn;
    private Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        username = findViewById(R.id.usernameUser2);
        username.setText(AppStorage.getInstance().getUserName());
        email = findViewById(R.id.emailUser2);
        email.setText(AppStorage.getInstance().getUserEmail());

        returnBtn = findViewById(R.id.returnBtnUser);
        returnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, NotepadsActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });

        logoutBtn = findViewById(R.id.logoutBtnUser);
        logoutBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(UserActivity.this)
                    .setTitle("Подтверждение выхода")
                    .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        // Действие при подтверждении
                        AppStorage.getInstance().clearSession();
                        Intent intent = new Intent(UserActivity.this, AuthActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    })
                    .setNegativeButton("Отмена", (dialog, which) -> {
                        // Закрыть диалог, ничего не делать
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });
    }
}