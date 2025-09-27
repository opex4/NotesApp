package com.example.notesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.TextNoteDTO;
import com.example.notesapp.repository.CreateTextNoteRep;
import com.example.notesapp.repository.DeleteNotepadRep;
import com.example.notesapp.repository.LoadNotepadRep;
import com.example.notesapp.repository.TextNoteRep;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

@Getter
public class NotesVM extends ViewModel {
    private DeleteNotepadRep deleteNotepadRep;
    private LoadNotepadRep loadNotepadRep;

    private ArrayList<TextNoteDTO> textNotes;
    private MutableLiveData<Boolean> isNotesLoaded = new MutableLiveData<>();

    private CreateTextNoteRep createTextNoteRep;

    public void deleteNotepad(String jwtToken, int id){
        if(deleteNotepadRep == null){
            deleteNotepadRep = new DeleteNotepadRep(jwtToken, id);
        } else {
            deleteNotepadRep.setNotepadId(id);
        }
        deleteNotepadRep.pullData();
    }

    public void loadNotepad(String jwtToken, int id){
        if(loadNotepadRep == null){
            loadNotepadRep = new LoadNotepadRep(jwtToken, id);
        } else {
            loadNotepadRep.setId(id);
        }
        loadNotepadRep.pullData();
    }

    public void loadNotes(String jwtToken, List<Integer> ids) {
        isNotesLoaded.postValue(false);
        int amountNotes = ids.size();

        if (amountNotes == 0) {
            textNotes = new ArrayList<>();
            isNotesLoaded.postValue(true); // Загрузка завершена
            return;
        }

        ArrayList<TextNoteRep> textNotesRep = new ArrayList<>(amountNotes);
        AtomicInteger loadedCount = new AtomicInteger(0); // Счетчик загруженных заметок

        ids.forEach(id -> {
            TextNoteRep noteRep = new TextNoteRep(jwtToken, id);
            textNotesRep.add(noteRep);

            noteRep.getResponseData().observeForever(note -> {
                if (note != null) {
                    int currentLoaded = loadedCount.incrementAndGet();

                    // Когда все заметки загружены
                    if (currentLoaded == amountNotes) {
                        processLoadedNotes(textNotesRep);
                    }
                }
            });
            noteRep.pullData();
        });
    }

    private void processLoadedNotes(ArrayList<TextNoteRep> textNotesRep) {
        ArrayList<TextNoteDTO> newTextNotes = new ArrayList<>();

        for (TextNoteRep noteRep : textNotesRep) {
            TextNoteDTO note = noteRep.getResponseData().getValue();
            if (note != null && "Text".equals(note.getType())) {
                newTextNotes.add(note);
            }
        }

        this.textNotes = newTextNotes;
        isNotesLoaded.postValue(true);
    }

    public void createTextNote(String jwtToken, String name){
        createTextNoteRep = new CreateTextNoteRep(jwtToken, name, loadNotepadRep.getId());
        createTextNoteRep.pullData();
    }
}
