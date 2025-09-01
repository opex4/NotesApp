package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.viewmodel.RegisterVM;

public class RegActivity extends AppCompatActivity {
    private EditText username, email, password, repeatPassword;
    private Button registerBtn, authBtn;
    private RegisterVM registerVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // Инициализация ViewModel
        registerVM = new ViewModelProvider(this).get(RegisterVM.class);

        // Инициализация UI
        initializeUI();

        // Наблюдаем за сообщениями
        registerVM.getMessageLiveData().observe(this, message -> {
            if (message != null) {
                Toast.makeText(RegActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
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
                RegStructDTO registerData = new RegStructDTO();
                registerData.setEmail(email.getText().toString().trim());
                registerData.setUsername(username.getText().toString().trim());
                registerData.setPassword(password.getText().toString().trim());

                registerVM.registerUser(registerData);
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