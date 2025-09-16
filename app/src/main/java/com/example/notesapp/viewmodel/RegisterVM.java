package com.example.notesapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.repository.RegisterRep;
import com.example.notesapp.repository.exeptions.IncorrectRegisterDataExeption;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RegisterVM extends ViewModel {
    private RegisterRep registerRep;

    public void registerUser(RegStructDTO registerData) throws IncorrectRegisterDataExeption {
        this.registerRep = new RegisterRep(registerData);
        registerRep.pullData();
    }
}