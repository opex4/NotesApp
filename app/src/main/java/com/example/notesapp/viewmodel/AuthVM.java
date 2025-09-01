package com.example.notesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.repository.AuthRep;

public class AuthVM extends ViewModel {
    private final AuthRep authRep;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public AuthVM() {
        this.authRep = new AuthRep();
        // Подписываемся на изменения из репозитория
        authRep.getMessageLiveData().observeForever(messageLiveData::postValue);
    }

    public void logInUser(AuthStructDTO registerData) {
        authRep.pullRegisterData(registerData);
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
