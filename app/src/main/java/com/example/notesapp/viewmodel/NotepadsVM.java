package com.example.notesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.repository.NotepadsRep;
import com.example.notesapp.repository.UserRep;

import lombok.Getter;

public class NotepadsVM extends ViewModel {
    private NotepadsRep notepadsRep;
    private UserRep userRep;

    public void loadNotepads(String jwtToken) throws Exception{
        this.notepadsRep = new NotepadsRep(jwtToken);
        notepadsRep.pullData();
    }

    public void loadUser(String jwtToken) throws Exception{
        this.userRep = new UserRep(jwtToken);
        userRep.pullData();
    }
}
