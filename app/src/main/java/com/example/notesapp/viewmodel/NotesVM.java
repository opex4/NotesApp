package com.example.notesapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.notesapp.repository.DeleteNotepadRep;
import com.example.notesapp.repository.LoadNotepadRep;

import lombok.Getter;

@Getter
public class NotesVM extends ViewModel {
    private DeleteNotepadRep deleteNotepadRep;
    private LoadNotepadRep loadNotepadRep;

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
}
