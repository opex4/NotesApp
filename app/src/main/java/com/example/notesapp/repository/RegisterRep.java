package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.JwtTokenDTO;
import com.example.notesapp.dto.RegStructDTO;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class RegisterRep extends Rep<JwtTokenDTO> {
    private JwtTokenDTO jwtToken;
    private RegStructDTO registerData;

    public RegisterRep(RegStructDTO regStructDTO){
        registerData = regStructDTO;
    }

    @Override
    public void pullData() {
        if (!isRegisterDataCorrect(registerData)){
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<JwtTokenDTO> call = apiService.registerUser(registerData);

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

    private boolean isRegisterDataCorrect(RegStructDTO registerData) {
        if (registerData == null) {
            getMessageLiveData().postValue("Данные регистрации не могут быть пустыми");
            return false;
        }

        if (registerData.getUsername() == null || registerData.getUsername().isEmpty()) {
            getMessageLiveData().postValue("Имя не может быть пустым");
            return false;
        }

        if (registerData.getEmail() == null || registerData.getEmail().isEmpty()) {
            getMessageLiveData().postValue("Email не может быть пустым");
            return false;
        }
        if (!isValidEmailFormat(registerData.getEmail())) {
            getMessageLiveData().postValue("Неверный формат email");
            return false;
        }

        if (registerData.getPassword() == null || registerData.getPassword().length() < 6) {
            getMessageLiveData().postValue("Пароль должен содержать минимум 6 символов");
            return false;
        }

        return true;
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    @Override
    public void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 201){
            setJwtToken(responseData);
            getMessageLiveData().postValue("Пользователь создан");
        }
    }

    @Override
    public void handleErrorResponse(int code) {
        String errorMessage;

        switch (code) {
            case 401:
                errorMessage = "Пользователь с таким email или именем уже существует";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }

        getMessageLiveData().postValue(errorMessage);
    }
}