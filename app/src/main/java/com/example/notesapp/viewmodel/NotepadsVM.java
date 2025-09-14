package com.example.notesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notesapp.repository.LoadingNotepadsRep;

public class NotepadsVM extends ViewModel {
    private LoadingNotepadsRep loadingNotepadsRep;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public void loadNotepads(String jwtToken){
        this.loadingNotepadsRep = new LoadingNotepadsRep(jwtToken);
        loadingNotepadsRep.getMessageLiveData().observeForever(messageLiveData::postValue);
        loadingNotepadsRep.pullData();
    }
}
