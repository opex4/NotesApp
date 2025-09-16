package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.dto.NotepadInfoDTO;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.NotepadsVM;

import java.util.ArrayList;

public class NotepadsActivity extends AppCompatActivity {
    private NotepadsVM notepadsVM;
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notepads);

        // Проверка наличия токена
        if (AppStorage.getInstance().isJwtTokenExists()){
            jwtToken = AppStorage.getInstance().getJwtToken();
            Toast.makeText(NotepadsActivity.this, jwtToken, Toast.LENGTH_SHORT).show();
        } else {
            goToRegisterActivity();
            return;
        }

        // Инициализация ViewModel и загрузка данных
        notepadsVM = new ViewModelProvider(this).get(NotepadsVM.class);
        try {
            notepadsVM.loadUser(jwtToken);
            notepadsVM.loadNotepads(jwtToken);
        } catch (JwtExeption e){
            String message = e.getMessage();
            Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        // Подписка на сообщения
        notepadsVM.getUserRep().getErrorMessage().observe(this, message ->{
            Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
        });
        notepadsVM.getNotepadsRep().getErrorMessage().observe(this, message ->{
            Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
        });
        // Подписка на получение ответа с сервера
        notepadsVM.getUserRep().getResponseData().observe(this, user ->{
            AppStorage.getInstance().saveUser(user);
        });
        notepadsVM.getNotepadsRep().getResponseData().observe(this, notepads ->{
            // Инициализация UI
            initializeUI(notepads);
        });
    }

    private void initializeUI(ArrayList<NotepadInfoDTO> notepads) {

    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(NotepadsActivity.this, RegActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}