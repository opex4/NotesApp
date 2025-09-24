package com.example.notesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.R;
import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.NotepadsVM;
import com.example.notesapp.viewmodel.NotesVM;

import java.util.Objects;

public class NotesActivity extends AppCompatActivity {
    private NotesVM notesVM;
    private String jwtToken;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        // Получение данных из Intent
        Intent intent = getIntent();
        id = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("NOTEPAD_ID")));

        // Проверка наличия токена
        if (AppStorage.getInstance().isJwtTokenExists()){
            jwtToken = AppStorage.getInstance().getJwtToken();
        } else {
            goToRegisterActivity();
            return;
        }

        // Инициализация ViewModel и загрузка данных
        notesVM = new ViewModelProvider(this).get(NotesVM.class);
        try {
            notesVM.loadNotepad(jwtToken, id);
            // Подписка на сообщения
            notesVM.getLoadNotepadRep().getErrorMessage().observe(this, message ->{
                Toast.makeText(NotesActivity.this, message, Toast.LENGTH_SHORT).show();
                if (message.equals(notesVM.getLoadNotepadRep().getNotAuth())){
                    goToRegisterActivity();
                }
            });
            // Подписка на получение ответа
            notesVM.getLoadNotepadRep().getResponseData().observe(this, notepad -> {

            });
        } catch (JwtExeption e) {
            Toast.makeText(NotesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(NotesActivity.this, AuthActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}