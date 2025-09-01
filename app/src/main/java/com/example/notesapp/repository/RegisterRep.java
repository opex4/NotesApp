package com.example.notesapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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
public class RegisterRep {
    private JwtTokenDTO jwtToken;
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    public void pullRegisterData(RegStructDTO registerData) {
        // Проверка корректности данных
        if (!isRegisterDataCorrect(registerData)){
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();

        Call<JwtTokenDTO> call = apiService.registerUser(registerData);

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

    private boolean isRegisterDataCorrect(RegStructDTO registerData) {
        if (registerData == null) {
            messageLiveData.postValue("Данные регистрации не могут быть пустыми");
            return false;
        }

        // Проверка имени пользователя
        if (registerData.getUsername() == null || registerData.getUsername().isEmpty()) {
            messageLiveData.postValue("Имя не может быть пустым");
            return false;
        }

        // Проверка email
        if (registerData.getEmail() == null || registerData.getEmail().isEmpty()) {
            messageLiveData.postValue("Email не может быть пустым");
            return false;
        }
        if (!isValidEmailFormat(registerData.getEmail())) {
            messageLiveData.postValue("Неверный формат email");
            return false;
        }

        // Проверка пароля
        if (registerData.getPassword() == null || registerData.getPassword().length() < 6) {
            messageLiveData.postValue("Пароль должен содержать минимум 6 символов");
            return false;
        }

        return true;
    }

    // Метод для проверки формата email
    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    private void handleSuccessResponse(JwtTokenDTO responseData, int code) {
        if (code == 201){
            setJwtToken(responseData);
            messageLiveData.postValue("Пользователь создан");
        }
    }
    private void handleErrorResponse(int code) {
        String errorMessage;

        switch (code) {
            case 401:
                errorMessage = "Пользователь с таким email или именем уже существует";
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