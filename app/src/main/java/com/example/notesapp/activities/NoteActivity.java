package com.example.notesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.R;
import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.NoteVM;
import com.example.notesapp.viewmodel.NotesVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteActivity extends AppCompatActivity {
    private int noteId;
    private int notepadId;
    private FloatingActionButton returnBtn, saveBtn, deleteNoteBtn, addAccessPersonBtn;
    private TextView nameNoteTV;
    private EditText editText;
    private String jwtToken;
    private NoteVM noteVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        // Получение данных из Intent
        Intent intent = getIntent();
        if (intent.hasExtra("NOTE_ID") && intent.hasExtra("NOTEPAD_ID")) {
            notepadId = intent.getIntExtra("NOTEPAD_ID", -1); // -1 значение по умолчанию
            noteId = intent.getIntExtra("NOTE_ID", -1); // -1 значение по умолчанию
            if (noteId == -1 || notepadId == -1) {
                Toast.makeText(this, "Ошибка: ID не найден", Toast.LENGTH_LONG).show();
                goToNotesActivity();
                return;
            }
        } else {
            Toast.makeText(this, "Ошибка: ID не передан", Toast.LENGTH_LONG).show();
            goToNotesActivity();
            return;
        }

        // Проверка наличия токена
        if (AppStorage.getInstance().isJwtTokenExists()){
            jwtToken = AppStorage.getInstance().getJwtToken();
        } else {
            goToRegisterActivity();
            return;
        }

        // Инициализация UI
        initializeUI();

        // Инициализация ViewModel и загрузка данных
        noteVM = new ViewModelProvider(this).get(NoteVM.class);
        // Кнопка вернуться к блокноту
        returnBtn.setOnClickListener(v -> {
            goToNotesActivity();
        });
        try {
            noteVM.loadNote(jwtToken, noteId);
            // Подписка на сообщения
            noteVM.getTextNoteRep().getErrorMessage().observe(this, errorMessage -> {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            });
            // Подписка на загрузку заметки
            noteVM.getTextNoteRep().getResponseData().observe(this, textNote -> {
                nameNoteTV.setText(textNote.getName());
                editText.setText(textNote.getText());

                // Сохранить заметку
                saveBtn.setOnClickListener(v -> {
                    textNote.setText(editText.getText().toString());
                    noteVM.saveNote(jwtToken, textNote);
                    noteVM.getErrorSaveNote().observe(this, errorMessage -> {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    });
                    noteVM.getSuccessSaveNote().observe(this, textNoteUpdated -> {
                        restartActivity();
                    });
                });

                // Удалить заметку
                deleteNoteBtn.setOnClickListener(v -> {
                    deleteNote();
                });
            });
        } catch (JwtExeption e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote() {
        // Диалог подтверждения
        new AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы действительно хотите удалить заметку?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    // Действие при подтверждении
                    performNoteDeletion();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    // Закрыть диалог, ничего не делать
                    dialog.dismiss();
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void performNoteDeletion() {
        noteVM.deleteNote(jwtToken, noteId);
        noteVM.getDeleteTextNoteRep().getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
        noteVM.getDeleteTextNoteRep().getSuccessMessage().observe(this, successMessage -> {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
        });
        goToNotesActivity();
    }

    private void restartActivity() {
        // Перезагрузка активити с анимацией
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void initializeUI() {
        saveBtn = findViewById(R.id.saveBtnNote);
        returnBtn = findViewById(R.id.returnBtnNote);
        addAccessPersonBtn = findViewById(R.id.addAccessPersonBtnNote);
        deleteNoteBtn = findViewById(R.id.deleteBtnNote);
        editText= findViewById(R.id.editTextNote);
        nameNoteTV = findViewById(R.id.nameNote);
    }

    private void goToNotesActivity() {
        Intent intent = new Intent(NoteActivity.this, NotesActivity.class);
        intent.putExtra("NOTEPAD_ID", notepadId);
        intent.putExtra("NOTE_ID", noteId);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(NoteActivity.this, AuthActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}