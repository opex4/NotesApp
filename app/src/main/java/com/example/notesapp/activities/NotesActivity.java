package com.example.notesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.notesapp.dto.TextNoteDTO;
import com.example.notesapp.repository.exeptions.JwtExeption;
import com.example.notesapp.viewmodel.NotesVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesActivity extends AppCompatActivity {
    private NotesVM notesVM;
    private String jwtToken;
    private int notepadId;
    private TextView notes;
    private FloatingActionButton deleteNotepadBtn, addNoteBtn, addAccessPersonBtn, returnBtn;
    private RecyclerView recyclerViewAccess, recyclerViewNotes;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);

        // Получение данных из Intent
        Intent intent = getIntent();
        if (intent.hasExtra("NOTEPAD_ID")) {
            notepadId = intent.getIntExtra("NOTEPAD_ID", -1); // -1 значение по умолчанию
            if (notepadId == -1) {
                Toast.makeText(this, "Ошибка: ID не найден", Toast.LENGTH_LONG).show();
                goToNotepadsActivity();
                return;
            }
        } else {
            Toast.makeText(this, "Ошибка: ID не передан", Toast.LENGTH_LONG).show();
            goToNotepadsActivity();
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
        notesVM = new ViewModelProvider(this).get(NotesVM.class);
        try {
            notesVM.loadNotepad(jwtToken, notepadId);
            // Подписка на сообщения
            notesVM.getLoadNotepadRep().getErrorMessage().observe(this, message ->{
                Toast.makeText(NotesActivity.this, message, Toast.LENGTH_SHORT).show();
                if (message.equals(notesVM.getLoadNotepadRep().getNotAuth())){
                    goToRegisterActivity();
                }
            });
            // Подписка на получение ответа
            notesVM.getLoadNotepadRep().getResponseData().observe(this, notepad -> {
                // Отображение зазвания блокнотов
                notes.setText(notepad.getNotepadName());

                // Удалить блокнот
                deleteNotepadBtn.setOnClickListener(v -> {
                    deleteNotepad();
                });

                // Загрузка заметок
                notesVM.loadNotes(jwtToken, notepad.getNote_ids());
                // Подписка на получение заметок
                notesVM.getIsNotesLoaded().observe(this, isLoaded -> {
                    if(isLoaded){
                        noteAdapter.setNotes(notesVM.getTextNotes());
                    }
                });

                // Создать заметку
                addNoteBtn.setOnClickListener(v -> {
                    addNote();
                });
            });

            // Вернуться к списку блокнотов
            returnBtn.setOnClickListener(v -> {
                goToNotepadsActivity();
            });
        } catch (JwtExeption e) {
            Toast.makeText(NotesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            goToRegisterActivity();
        }
    }

    private void initializeUI() {
        notes = findViewById(R.id.notes_notes);
        deleteNotepadBtn = findViewById(R.id.deleteNotepadBtnNotes);
        addNoteBtn = findViewById(R.id.addNoteBtnNotes);
        addAccessPersonBtn = findViewById(R.id.addAccessPersonBtnNotes);
        returnBtn = findViewById(R.id.returnBtnNotes);

        recyclerViewAccess = findViewById(R.id.recyclerViewAccessNotepad);

        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this::goToNoteDetailActivity);
        recyclerViewNotes.setAdapter(noteAdapter);
    }

    private void goToNoteDetailActivity(TextNoteDTO textNote) {
//        // Переход к деталям заметки
//        Intent intent = new Intent(this, NoteActivity.class);
//        intent.putExtra("NOTE_ID", textNote.getId());
//        startActivity(intent);

        // Заглушка  на обработку события
        Toast.makeText(NotesActivity.this, "Переход на заметку успешен", Toast.LENGTH_SHORT).show();
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(NotesActivity.this, AuthActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
    private void goToNotepadsActivity() {
        Intent intent = new Intent(NotesActivity.this, NotepadsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    private void deleteNotepad() {
        // Диалог подтверждения
        new AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы действительно хотите удалить блокнот?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    // Действие при подтверждении
                    performNotepadDeletion();
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    // Закрыть диалог, ничего не делать
                    dialog.dismiss();
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void performNotepadDeletion() {
        notesVM.deleteNotepad(jwtToken, notepadId);
        notesVM.getDeleteNotepadRep().getSuccessMessage().observe(this, successMessage -> {
            Toast.makeText(NotesActivity.this, successMessage, Toast.LENGTH_SHORT).show();
            goToNotepadsActivity();
        });
        notesVM.getDeleteNotepadRep().getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(NotesActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            goToNotepadsActivity();
        });
    }

    private void addNote() {
        // Создаем AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Создать новую заметку");

        // Создаем EditText для ввода названия
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Введите название заметки");
        builder.setView(input);

        // Кнопка создания
        builder.setPositiveButton("Создать", (dialog, which) -> {
            String noteName = input.getText().toString().trim();
            if (!noteName.isEmpty()) {
                createNewNote(noteName);
            } else {
                Toast.makeText(this, "Название не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });

        // Кнопка отмены
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void createNewNote(String noteName) {
        notesVM.createTextNote(jwtToken, noteName);
        notesVM.getCreateTextNoteRep().getSuccessMessage().observe(this, successMessage -> {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

            // Перезагрузка активити с анимацией
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        });
        notesVM.getCreateTextNoteRep().getErrorMessage().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }
}