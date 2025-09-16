package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.JwtTokenDTO;
import com.example.notesapp.dto.RegStructDTO;
import com.example.notesapp.repository.exeptions.IncorrectRegisterDataExeption;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class RegisterRep extends Rep<JwtTokenDTO> {
    private final RegStructDTO registerData;

    public RegisterRep(RegStructDTO regStructDTO) throws IncorrectRegisterDataExeption {
        super();
        registerData = regStructDTO;
    }

    @Override
    public void pullData() {
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

    @Override
    public void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 201){
            super.setResponseData(responseData);
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
        setErrorMessage(errorMessage);
    }

    private void checkRegisterData(RegStructDTO registerData) throws IncorrectRegisterDataExeption {
        if (registerData == null) {
            throw new IncorrectRegisterDataExeption("Данные регистрации не могут быть пустыми");
        }
        if (registerData.getUsername() == null || registerData.getUsername().isEmpty()) {
            throw new IncorrectRegisterDataExeption("Имя не может быть пустым");
        }
        String email = registerData.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IncorrectRegisterDataExeption("Email не может быть пустым");
        }
        if (!isValidEmailFormat(email)) {
            throw new IncorrectRegisterDataExeption("Неверный формат email");
        }
        if (registerData.getPassword() == null || registerData.getPassword().length() < 6) {
            throw new IncorrectRegisterDataExeption("Пароль должен содержать минимум 6 символов");
        }
    }
    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
}