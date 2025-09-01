package com.example.notesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.repository.RegisterRep;

public class RegisterVM extends ViewModel {
    private final RegisterRep registerRep;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public RegisterVM() {
        this.registerRep = new RegisterRep();
        // Подписываемся на изменения из репозитория
        registerRep.getMessageLiveData().observeForever(messageLiveData::postValue);
    }

    public void registerUser(RegStructDTO registerData) {
        registerRep.pullRegisterData(registerData);
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Очищаем ресурсы при уничтожении ViewModel
    }
}