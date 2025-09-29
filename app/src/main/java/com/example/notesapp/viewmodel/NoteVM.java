package com.example.notesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.TextNoteDTO;
import com.example.notesapp.repository.DeleteTextNoteRep;
import com.example.notesapp.repository.LockNoteRep;
import com.example.notesapp.repository.PutTextNoteRep;
import com.example.notesapp.repository.TextNoteRep;
import com.example.notesapp.repository.UnlockNoteRep;

import lombok.Getter;

@Getter
public class NoteVM extends ViewModel {
    private TextNoteRep textNoteRep;
    private DeleteTextNoteRep deleteTextNoteRep;
    private MutableLiveData<String> errorSaveNote = new MutableLiveData<>();
    private MutableLiveData<String> successSaveNote = new MutableLiveData<>();

    public void loadNote(String jwtToken, int noteId){
        textNoteRep = new TextNoteRep(jwtToken, noteId);
        textNoteRep.pullData();
    }

    public void deleteNote(String jwtToken, int noteId){
        deleteTextNoteRep = new DeleteTextNoteRep(jwtToken, noteId);
        deleteTextNoteRep.pullData();
    }

    public void saveNote(String jwtToken, TextNoteDTO textNote){
        // Блокирую заметку для редактирования длугими пользователями
        LockNoteRep lockNoteRep = new LockNoteRep(jwtToken, textNote.getId());
        lockNoteRep.pullData();
        lockNoteRep.getSuccessMessage().observeForever(successMessage -> {
            PutTextNoteRep putTextNoteRep = new PutTextNoteRep(jwtToken, textNote);
            putTextNoteRep.pullData();
            putTextNoteRep.getErrorMessage().observeForever(errorSave -> {
                errorSaveNote.postValue(errorSave);
            });
            putTextNoteRep.getResponseData().observeForever(updatedTextNote -> {
                UnlockNoteRep unlockNoteRep = new UnlockNoteRep(jwtToken, updatedTextNote.getId());
                unlockNoteRep.pullData();
                unlockNoteRep.getSuccessMessage().observeForever(successUnlock -> {
                    successSaveNote.postValue(successUnlock);
                });
                unlockNoteRep.getErrorMessage().observeForever(errorUnlock -> {
                    errorSaveNote.postValue(errorUnlock);
                });
            });
        });
        lockNoteRep.getErrorMessage().observeForever(errorLock -> {
            errorSaveNote.postValue(errorLock);
        });
    }
}
