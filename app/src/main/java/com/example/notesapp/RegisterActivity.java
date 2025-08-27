package com.example.notesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.repository.Registration;

public class RegisterActivity extends AppCompatActivity {
    private TextView textRegister;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private Button registerBtn;
    private RegStructDTO registerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textRegister = findViewById(R.id.textRegister);
        registerBtn = findViewById(R.id.registerBtn);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeatPassword);
        registerData = new RegStructDTO();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(repeatPassword.getText().toString())){
                    registerData.setEmail(email.getText().toString().trim());
                    registerData.setUsername(username.getText().toString().trim());
                    registerData.setPassword(password.getText().toString().trim());
                    Registration registration = new Registration();
                    registration.pullRegisterData(registerData);

                    Toast.makeText(RegisterActivity.this, "Вроде норм всё", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Повтор пароля неверный", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}