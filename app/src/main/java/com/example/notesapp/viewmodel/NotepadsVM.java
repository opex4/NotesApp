package com.example.notesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.NotepadInfoDTO;
import com.example.notesapp.repository.NewNotepadRep;
import com.example.notesapp.repository.NotepadsRep;
import com.example.notesapp.repository.UserRep;
import com.example.notesapp.repository.exeptions.JwtExeption;


import lombok.Getter;

@Getter
public class NotepadsVM extends ViewModel {
    private NotepadsRep notepadsRep;
    private UserRep userRep;
    private NewNotepadRep newNotepadRep;

    public void loadNotepads(String jwtToken) throws JwtExeption {
        if(notepadsRep == null){
            this.notepadsRep = new NotepadsRep(jwtToken);
        }
        notepadsRep.pullData();
    }

    public void loadUser(String jwtToken) throws JwtExeption{
        if(userRep == null){
            this.userRep = new UserRep(jwtToken);
        }
        userRep.pullData();
    }

    public void createNotepad(String jwtToken, String newNotepad) throws JwtExeption{
        if(newNotepadRep == null){
            this.newNotepadRep = new NewNotepadRep(jwtToken, newNotepad);
        }
        newNotepadRep.pullData();
    }
}
