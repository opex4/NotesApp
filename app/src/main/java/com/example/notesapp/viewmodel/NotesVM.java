package com.example.notesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.TextNoteDTO;
import com.example.notesapp.repository.DeleteNotepadRep;
import com.example.notesapp.repository.LoadNotepadRep;
import com.example.notesapp.repository.TextNoteRep;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class NotesVM extends ViewModel {
    private DeleteNotepadRep deleteNotepadRep;
    private LoadNotepadRep loadNotepadRep;
    private ArrayList<TextNoteRep> textNotesRep;
    private ArrayList<TextNoteDTO> textNotes;
    private MutableLiveData<Boolean> isNotesLoaded = new MutableLiveData<>();

    public void deleteNotepad(String jwtToken, int id){
        if(deleteNotepadRep == null){
            deleteNotepadRep = new DeleteNotepadRep(jwtToken, id);
        } else {
            deleteNotepadRep.setId(id);
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

    public void loadNotes(String jwtToken, List<Integer> ids){
        isNotesLoaded.postValue(false);
        int amountNotes = ids.size();
        if(amountNotes == 0){
            return;
        }
        textNotesRep = new ArrayList<>(amountNotes);
        ids.forEach(id -> {
            textNotesRep.add(new TextNoteRep(jwtToken, id));
            textNotesRep.get(textNotesRep.size() - 1).pullData();
        });
        // Подписка на загрузку последней записки
        textNotesRep.get(amountNotes - 1).getResponseData().observeForever(lastTextNote -> {
            if(isAllNotesLoaded()){
                textNotes = new ArrayList<>(textNotesRep.size());
                textNotesRep.forEach(textNoteRep -> {
                    textNotes.add(textNoteRep.getResponseData().getValue());
                });
                textNotes = textNotes.stream()
                        .filter(note -> note.getType().contains("Text"))
                        .collect(Collectors.toCollection(ArrayList::new));
                isNotesLoaded.postValue(true);
            }
        });
    }

    private boolean isAllNotesLoaded(){
        for (var textNoteRep: textNotesRep){
            if(textNoteRep.getResponseData().getValue() == null){
                return false;
            }
        }
        return true;
    }
}
