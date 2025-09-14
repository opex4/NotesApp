package com.example.notesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.repository.RegisterRep;

public class RegisterVM extends ViewModel {
    private RegisterRep registerRep;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public void registerUser(RegStructDTO registerData) {
        this.registerRep = new RegisterRep(registerData);
        registerRep.getMessageLiveData().observeForever(messageLiveData::postValue);
        registerRep.pullData();
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }
}