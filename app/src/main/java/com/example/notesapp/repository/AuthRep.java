package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.dto.JwtTokenDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@NoArgsConstructor
public class AuthRep extends Rep<JwtTokenDTO> {
    private JwtTokenDTO jwtToken;
    private AuthStructDTO logInData;

    public AuthRep(AuthStructDTO authStructDTO){
        logInData = authStructDTO;
    }

    @Override
    public void pullRegisterData() {
        // Проверка корректности данных
        if (!isLogInDataCorrect(logInData)){
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<JwtTokenDTO> call = apiService.loginUser(logInData);

        call.enqueue(new Callback<JwtTokenDTO>() {
            @Override
            public void onResponse(@NonNull Call<JwtTokenDTO> call, @NonNull Response<JwtTokenDTO> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtTokenDTO> call, @NonNull Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    private boolean isLogInDataCorrect(AuthStructDTO authData) {
        if (authData == null) {
            getMessageLiveData().postValue("Данные регистрации не могут быть пустыми");
            return false;
        }

        // Проверка имени пользователя
        if (authData.getUsername() == null || authData.getUsername().isEmpty()) {
            getMessageLiveData().postValue("Имя не может быть пустым");
            return false;
        }

        // Проверка пароля
        if (authData.getPassword() == null || authData.getPassword().isEmpty()) {
            getMessageLiveData().postValue("Пароль не может быть пустым");
            return false;
        }

        return true;
    }

    @Override
    public void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 200){
            setJwtToken(responseData);
            getMessageLiveData().postValue("Успешный вход");
        }
    }

    @Override
    public void handleErrorResponse(int code) {
        String errorMessage;

        switch (code) {
            case 401:
                errorMessage = "Неверный логин или пароль";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }

        getMessageLiveData().postValue(errorMessage);
    }
}
