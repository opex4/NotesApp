package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.NotepadInfoDTO;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class NotepadsRep extends Rep<ArrayList<NotepadInfoDTO>>{
    public NotepadsRep(String jwtToken){
        super(jwtToken);
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<ArrayList<NotepadInfoDTO>> call = apiService.getNotepads(super.getJwtToken());

        call.enqueue(new Callback<ArrayList<NotepadInfoDTO>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<NotepadInfoDTO>> call, @NonNull Response<ArrayList<NotepadInfoDTO>> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<NotepadInfoDTO>> call, @NonNull Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    @Override
    public void handleSuccessResponse(ArrayList<NotepadInfoDTO> responseData, int code) {
        if (code == 200){
            setResponseData(responseData);
        }
    }
}
