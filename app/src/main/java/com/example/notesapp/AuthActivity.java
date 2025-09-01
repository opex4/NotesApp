package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.viewmodel.AuthVM;

public class AuthActivity extends AppCompatActivity {

    private EditText username, password;
    private Button authBtn, registerBtn;
    private AuthVM authVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Инициализация ViewModel
        authVM = new ViewModelProvider(this).get(AuthVM.class);

        // Инициализация UI
        initializeUI();

        // Наблюдаем за сообщениями
        authVM.getMessageLiveData().observe(this, message -> {
            if (message != null) {
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeUI() {
        username = findViewById(R.id.usernameAuth);
        password = findViewById(R.id.passwordAuth);
        authBtn = findViewById(R.id.authBtnAuth);
        registerBtn = findViewById(R.id.regBtnAuth);

        authBtn.setOnClickListener(v -> {
            AuthStructDTO logInData = new AuthStructDTO();
            logInData.setUsername(username.getText().toString().trim());
            logInData.setPassword(password.getText().toString().trim());

            authVM.logInUser(logInData);
        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AuthActivity.this, RegActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }
}