package com.example.notesapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.appStorage.AppStorage;
import com.example.notesapp.repository.NotepadsRep;
import com.example.notesapp.repository.UserRep;

import lombok.Getter;

public class NotepadsVM extends ViewModel {
    @Getter
    private NotepadsRep notepadsRep;
    @Getter
    private UserRep userRep;
    private final MutableLiveData<String> notepadsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userLiveData = new MutableLiveData<>();

    public void loadNotepads(String jwtToken){
        this.notepadsRep = new NotepadsRep(jwtToken);
        notepadsRep.getMessageLiveData().observeForever(notepadsLiveData::postValue);
        notepadsRep.pullData();
    }

    public void loadUser(String jwtToken){
        this.userRep = new UserRep(jwtToken);
        userRep.getMessageLiveData().observeForever(userLiveData::postValue);
        userRep.pullData();
    }
    public void saveUserInStorage(){
        AppStorage.getInstance().saveUser(userRep.getUser());
    }
    public LiveData<String> getNotepadsLiveData() {
        return notepadsLiveData;
    }
    public LiveData<String> getUserLiveData() {
        return userLiveData;
    }
}
