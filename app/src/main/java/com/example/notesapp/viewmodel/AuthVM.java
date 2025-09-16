package com.example.notesapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.repository.AuthRep;
import com.example.notesapp.repository.exeptions.IncorrectLoginDataExeption;

import lombok.Getter;

@Getter
public class AuthVM extends ViewModel {
    private AuthRep authRep;
    public void loginUser(AuthStructDTO authData) throws IncorrectLoginDataExeption {
        this.authRep = new AuthRep(authData);
        authRep.pullData();
    }
}
