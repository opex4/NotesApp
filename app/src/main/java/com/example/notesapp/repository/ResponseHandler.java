package com.example.notesapp.repository;

import androidx.lifecycle.MutableLiveData;

public interface ResponseHandler<T> {
    void pullRegisterData();
    void handleSuccessResponse(T responseData, int code);
    void handleErrorResponse(int code);
    void handleNetworkFailure(Throwable t);
    MutableLiveData<String> getMessageLiveData();
}
