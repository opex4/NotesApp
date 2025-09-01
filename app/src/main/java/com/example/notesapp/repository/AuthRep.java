package com.example.notesapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.AuthStructDTO;
import com.example.notesapp.dto.JwtTokenDTO;

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
public class AuthRep {
    private JwtTokenDTO jwtToken;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();


    public void pullRegisterData(AuthStructDTO logInData) {
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
                    // Обработка успешного запроса
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    // Обработка ошибок запроса
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JwtTokenDTO> call, @NonNull Throwable t) {
                // Обработка ошибок сети
                handleNetworkFailure(t);
            }
        });
    }

    private boolean isLogInDataCorrect(AuthStructDTO registerData) {
        if (registerData == null) {
            messageLiveData.postValue("Данные регистрации не могут быть пустыми");
            return false;
        }

        // Проверка имени пользователя
        if (registerData.getUsername() == null || registerData.getUsername().isEmpty()) {
            messageLiveData.postValue("Имя не может быть пустым");
            return false;
        }

        // Проверка пароля
        if (registerData.getPassword() == null || registerData.getPassword().isEmpty()) {
            messageLiveData.postValue("Пароль не может быть пустым");
            return false;
        }

        return true;
    }

    private void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 200){
            setJwtToken(responseData);
            messageLiveData.postValue("Успешный вход");
        }
    }
    private void handleErrorResponse(int code) {
        String errorMessage;

        switch (code) {
            case 401:
                errorMessage = "Неправильный логин или пароль";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }

        // Возвращаем ошибку
        messageLiveData.postValue(errorMessage);
    }

    private void handleNetworkFailure(Throwable t) {
        String errorMessage = "Нет соединения с сервером. Проверьте интернет";

        if (t instanceof IOException) {
            errorMessage = "Ошибка сети: " + t.getMessage();
        }

        // Возвращаем сетевую ошибку
        messageLiveData.postValue(errorMessage);
    }
}
