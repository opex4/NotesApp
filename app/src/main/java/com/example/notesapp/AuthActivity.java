package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.repository.exeptions.IncorrectLoginDataExeption;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.AuthVM;

public class AuthActivity extends AppCompatActivity {

    private EditText username, password;
    private Button authBtn, registerBtn;
    private AuthVM authVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        // Инициализация ViewModel
        authVM = new ViewModelProvider(this).get(AuthVM.class);
        // Подписка на сообщения
        authVM.getAuthRep().getErrorMessage().observe(this, message ->{
            Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
        });
        // Подписка на получение ответа с сервера
        authVM.getAuthRep().getResponseData().observe(this, jwtToken ->{
            AppStorage.getInstance().saveJwtToken(jwtToken.getToken());
            Intent intent = new Intent(AuthActivity.this, NotepadsActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });

        // Инициализация UI
        initializeUI();
    }

    private void initializeUI() {
        username = findViewById(R.id.usernameAuth);
        password = findViewById(R.id.passwordAuth);
        authBtn = findViewById(R.id.authBtnAuth);
        registerBtn = findViewById(R.id.regBtnAuth);

        authBtn.setOnClickListener(v -> {
            // Считываем данные
            AuthStructDTO logInData = new AuthStructDTO();
            logInData.setUsername(username.getText().toString().trim());
            logInData.setPassword(password.getText().toString().trim());

            // Авторизация
            try {
                authVM.loginUser(logInData);
            } catch (IncorrectLoginDataExeption | JwtExeption e){
                String message = e.getMessage();
                Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AuthActivity.this, RegActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }
}