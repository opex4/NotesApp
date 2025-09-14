package com.example.notesapp.repository;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Rep<T> implements ResponseHandler<T> {
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    @Override
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
        messageLiveData.postValue(errorMessage);
    }

    @Override
    public void handleNetworkFailure(Throwable t) {
        String errorMessage = "Нет соединения с сервером. Проверьте интернет";
        if (t instanceof IOException) {
            errorMessage = "Ошибка сети: " + t.getMessage();
        }
        messageLiveData.postValue(errorMessage);
    }

    @Override
    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    // Абстрактные методы для реализации в дочерних классах
    public abstract void pullData();
    public abstract void handleSuccessResponse(T responseData, int code);
}
