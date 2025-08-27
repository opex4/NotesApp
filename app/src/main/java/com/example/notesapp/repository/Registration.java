package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.JwtTokenDTO;
import com.example.notesapp.dto.RegStructDTO;

import java.io.IOException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
@NoArgsConstructor
public class Registration {
    private JwtTokenDTO jwtToken;
    private String message;

    public void pullRegisterData(RegStructDTO registerData) {
//        if (!isRegisterDataCorrect(registerData)){
//            return;
//        }

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

//    private boolean isRegisterDataCorrect(RegStructDTO registerData) {
//        if (registerData != null){
//            setMessage("Ошибка создания данных");
//            return true;
//        }
//        setMessage("Неверно введены регистрационные данные");
//        return false;
//    }

    private void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 201){
            setJwtToken(responseData);
            setMessage("Пользователь создан");
        }
    }
    private void handleErrorResponse(int code) {
        String errorMessage;

        switch (code) {
            case 401:
                errorMessage = "Пользователь с таким email уже существует";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }

        // Возвращаем ошибку
        setMessage(errorMessage);
    }

    private void handleNetworkFailure(Throwable t) {
        String errorMessage = "Нет соединения с сервером. Проверьте интернет";

        if (t instanceof IOException) {
            errorMessage = "Ошибка сети: " + t.getMessage();
        }

        // Возвращаем сетевую ошибку
        setMessage(errorMessage);
    }
}