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
import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.repository.exeptions.IncorrectRegisterDataExeption;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.RegisterVM;

public class RegActivity extends AppCompatActivity {
    private EditText username, email, password, repeatPassword;
    private Button registerBtn, authBtn;
    private RegisterVM registerVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reg);

        // Инициализация UI
        initializeUI();

        // Инициализация ViewModel
        registerVM = new ViewModelProvider(this).get(RegisterVM.class);
        // Подписка на сообщения
    }

    private void initializeUI() {
        registerBtn = findViewById(R.id.registerBtn);
        username = findViewById(R.id.usernameReg);
        email = findViewById(R.id.emailReg);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);
        authBtn = findViewById(R.id.authBtn);

        registerBtn.setOnClickListener(v -> {
            if (password.getText().toString().equals(repeatPassword.getText().toString())) {
                // Считываем данные
                RegStructDTO registerData = new RegStructDTO();
                registerData.setEmail(email.getText().toString().trim());
                registerData.setUsername(username.getText().toString().trim());
                registerData.setPassword(password.getText().toString().trim());

                // Регистрация
                try{
                    registerVM.registerUser(registerData);
                    registerVM.getRegisterRep().getErrorMessage().observe(this, message ->{
                        Toast.makeText(RegActivity.this, message, Toast.LENGTH_SHORT).show();
                    });
                    // Подписка на получение ответа с сервера
                    registerVM.getRegisterRep().getResponseData().observe(this, jwtToken ->{
                        AppStorage.getInstance().saveJwtToken(jwtToken.getToken());
                        Intent intent = new Intent(RegActivity.this, NotepadsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    });
                } catch (IncorrectRegisterDataExeption | JwtExeption e) {
                    String message = e.getMessage();
                    Toast.makeText(RegActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegActivity.this, "Повтор пароля неверный", Toast.LENGTH_SHORT).show();
            }
        });

        authBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegActivity.this, AuthActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        });
    }
}