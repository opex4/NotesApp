package com.example.notesapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.notesapp.repository.DeleteNotepadRep;
import com.example.notesapp.repository.LoadNotepadRep;
import com.example.notesapp.repository.NoteRep;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class NotesVM extends ViewModel {
    private DeleteNotepadRep deleteNotepadRep;
    private LoadNotepadRep loadNotepadRep;
    private ArrayList<NoteRep> notesRep;

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
        notesRep = new ArrayList<>(ids.size());
        ids.forEach(id -> {
            notesRep.add(new NoteRep(jwtToken, id));
        });
    }
}
