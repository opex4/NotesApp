package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.dto.JwtTokenDTO;
import com.example.notesapp.repository.exeptions.IncorrectLoginDataExeption;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRep extends Rep<JwtTokenDTO> {
    private final AuthStructDTO loginData;

    public AuthRep(AuthStructDTO authStructDTO) throws IncorrectLoginDataExeption{
        super();
        // Проверка корректности данных
        checkLoginData(authStructDTO);
        loginData = authStructDTO;
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<JwtTokenDTO> call = apiService.loginUser(loginData);

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

    @Override
    public void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 200){
            super.setResponseData(responseData);
        }
    }

    @Override
    public void handleErrorResponse(int code) throws HandleExeption {
        String errorMessage;

        switch (code) {
            case 401:
                errorMessage = "Неверный логин или пароль";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }
        throw new HandleExeption(errorMessage);
    }

    private void checkLoginData(AuthStructDTO authData) throws IncorrectLoginDataExeption{
        if (authData == null) {
            throw new IncorrectLoginDataExeption("Данные регистрации не могут быть пустыми");
        }

        // Проверка имени пользователя
        if (authData.getUsername() == null || authData.getUsername().isEmpty()) {
            throw new IncorrectLoginDataExeption("Имя не может быть пустым");
        }

        // Проверка пароля
        if (authData.getPassword() == null || authData.getPassword().isEmpty()) {
            throw new IncorrectLoginDataExeption("Пароль не может быть пустым");
        }
    }
}
