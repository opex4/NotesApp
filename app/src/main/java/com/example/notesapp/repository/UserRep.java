package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.UserDTO;

import lombok.Setter;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRep extends Rep<UserDTO> {
    public UserRep(String jwtToken){
        super(jwtToken);
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<UserDTO> call = apiService.getUsersMe(super.getJwtToken());

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    @Override
    public void handleSuccessResponse(UserDTO responseData, int code) {
        if (code == 200){
            setResponseData(responseData);
        }
    }
}
