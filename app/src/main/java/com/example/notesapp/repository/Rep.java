package com.example.notesapp.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.repository.exeptions.JwtExeption;

import java.io.IOException;

import lombok.Getter;

public abstract class Rep<T> {
    @Getter
    private String jwtToken;
    private MutableLiveData<T> responseData = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    protected Rep(){
        this.jwtToken = null;
    }
    public Rep(String jwtToken) throws JwtExeption {
        if(isJwtCorrect(jwtToken)){
            this.jwtToken = jwtToken;
        } else {
            throw new JwtExeption("Not correct Jwt");
        }
    }
    private boolean isJwtCorrect(String jwtToken) {
        return jwtToken == null || jwtToken.isEmpty();
    }

    public void handleErrorResponse(int code) {
        String errorMessage;
        switch (code) {
            case 401:
                errorMessage = "Пользователь не авторизирован";
                break;
            case 403:
                errorMessage = "Доступ запрещен";
                break;
            case 404:
                errorMessage = "Ресурс не найден";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }
        this.errorMessage.postValue(errorMessage);
    }

    public void handleNetworkFailure(Throwable t) {
        String errorMessage = "Нет соединения с сервером. Проверьте интернет";
        if (t instanceof IOException) {
            errorMessage = "Ошибка сети: " + t.getMessage();
        }
        this.errorMessage.postValue(errorMessage);
    }

    public LiveData<T> getResponseData() {
        return responseData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    protected void setResponseData(T responseData) {
        this.responseData.postValue(responseData);
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage.postValue(errorMessage);
    }

    // Абстрактные методы для реализации в дочерних классах
    public abstract void pullData();
    public abstract void handleSuccessResponse(T responseData, int code);
}
