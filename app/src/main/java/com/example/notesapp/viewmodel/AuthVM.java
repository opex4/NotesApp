package com.example.notesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.repository.AuthRep;

public class AuthVM extends ViewModel {
    private AuthRep authRep;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public void logInUser(AuthStructDTO authData) {
        this.authRep = new AuthRep(authData);
        authRep.getMessageLiveData().observeForever(messageLiveData::postValue);
        authRep.pullRegisterData();
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
}
