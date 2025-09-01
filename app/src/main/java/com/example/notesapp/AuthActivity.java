package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    private EditText username, email, password;
    private Button authBtn, registerBtn;
//    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

//        // Инициализация ViewModel
//        authViewModel = new ViewModelProvider(this).get(authViewModel.class);

        // Инициализация UI
        initializeUI();

//        // Наблюдаем за сообщениями
//        authViewModel.getMessageLiveData().observe(this, message -> {
//            if (message != null) {
//                Toast.makeText(RegActivity.this, message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initializeUI() {
        username = findViewById(R.id.usernameAuth);
        email = findViewById(R.id.emailAuth);
        password = findViewById(R.id.passwordAuth);
        authBtn = findViewById(R.id.authBtnAuth);
        registerBtn = findViewById(R.id.regBtnAuth);

        authBtn.setOnClickListener(v -> {

        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AuthActivity.this, RegActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }
}