package com.example.notesapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.dto.NotepadInfoDTO;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.NotepadsVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NotepadsActivity extends AppCompatActivity {
    private NotepadsVM notepadsVM;
    private String jwtToken;
    private RecyclerView recyclerView;
    private NotepadAdapter adapter;
    private String message;
    private FloatingActionButton addNewNotepadBtn, myUserBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notepads);

        // Кнопка пользователя
        myUserBtn = findViewById(R.id.myUserBtn);
        myUserBtn.setOnClickListener(v -> {
            goToUserActivity();
        });

        // Добавление нотпада
        addNewNotepadBtn = findViewById(R.id.addNewNotepadButton);
        addNewNotepadBtn.setOnClickListener(v -> {
            showCreateNotepadDialog();
        });

        // Проверка наличия токена
        if (AppStorage.getInstance().isJwtTokenExists()){
            jwtToken = AppStorage.getInstance().getJwtToken();
        } else {
            goToRegisterActivity();
            return;
        }

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация адаптера
        adapter = new NotepadAdapter(notepad -> {
            // Обработка клика по блокноту
            goToNotesActivity(notepad);
        });
        recyclerView.setAdapter(adapter);

        // Инициализация ViewModel и загрузка данных
        notepadsVM = new ViewModelProvider(this).get(NotepadsVM.class);
        try {
            notepadsVM.loadUser(jwtToken);
            notepadsVM.loadNotepads(jwtToken);

            // Подписка на сообщения
            notepadsVM.getUserRep().getErrorMessage().observe(this, message ->{
                if(Objects.equals(this.message, message)){
                    return;
                }
                this.message = message;
                Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
                if (message.equals(notepadsVM.getNotepadsRep().getNotAuth())){
                    goToRegisterActivity();
                }
            });

            notepadsVM.getNotepadsRep().getErrorMessage().observe(this, message ->{
                if(Objects.equals(this.message, message)){
                    return;
                }
                this.message = message;
                Toast.makeText(NotepadsActivity.this, message, Toast.LENGTH_SHORT).show();
                if (message.equals(notepadsVM.getNotepadsRep().getNotAuth())){
                    goToRegisterActivity();
                }
            });

            // Подписка на получение ответа с сервера
            notepadsVM.getUserRep().getResponseData().observe(this, user ->{
                AppStorage.getInstance().saveUser(user);
            });

            notepadsVM.getNotepadsRep().getResponseData().observe(this, notepads ->{
                if (notepads != null && !notepads.isEmpty()) {
                    adapter.setNotepads(notepads);
                } else {
                    Toast.makeText(NotepadsActivity.this, "Нет блокнотов", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JwtExeption e){
            Toast.makeText(NotepadsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            goToRegisterActivity();
        }
    }

    private void goToUserActivity() {
        Intent intent = new Intent(NotepadsActivity.this, UserActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void goToNotesActivity(NotepadInfoDTO notepad) {
//        // Переход к заметкам
//        Intent intent = new Intent(this, NotesActivity.class);
//        intent.putExtra("NOTEPAD_ID", notepad.getNotepadId());
//        intent.putExtra("NOTEPAD_NAME", notepad.getNotepadName());
//        startActivity(intent);

        // Toast заглушка для теста
        Toast.makeText(this, "Открыть блокнот: " + notepad.getNotepadName(), Toast.LENGTH_SHORT).show();
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(NotepadsActivity.this, AuthActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void showCreateNotepadDialog() {
        // Создаем AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Создать новый блокнот");

        // Создаем EditText для ввода названия
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Введите название блокнота");
        builder.setView(input);

        // Кнопка создания
        builder.setPositiveButton("Создать", (dialog, which) -> {
            String notepadName = input.getText().toString().trim();
            if (!notepadName.isEmpty()) {
                createNewNotepad(notepadName);
            } else {
                Toast.makeText(this, "Название не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка отмены
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void createNewNotepad(String name) {
        try {
            notepadsVM.createNotepad(jwtToken, name);
            // Подписка на успешное создание блокнота
            notepadsVM.getNewNotepadRep().getResponseData().observe(this, newNotepad -> {
                if (newNotepad != null) {
                    // После создания перезагружаем список
                    try {
                        notepadsVM.loadNotepads(jwtToken);
                    } catch (JwtExeption e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JwtExeption e) {
            Toast.makeText(NotepadsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            goToRegisterActivity();
        }
    }
}