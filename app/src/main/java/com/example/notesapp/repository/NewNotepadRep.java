package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.NameDTO;
import com.example.notesapp.dto.NotepadInfoDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewNotepadRep extends Rep<NotepadInfoDTO>{
    private NameDTO newNotepad;
    public NewNotepadRep(String jwtToken, String newNotepad){
        super(jwtToken);
        this.newNotepad = new NameDTO();
        this.newNotepad.setName(newNotepad);
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<NotepadInfoDTO> call = apiService.postNotepad(super.getJwtToken(), newNotepad);

        call.enqueue(new Callback<NotepadInfoDTO>(){

            @Override
            public void onResponse(@NonNull Call<NotepadInfoDTO> call, @NonNull Response<NotepadInfoDTO> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotepadInfoDTO> call, @NonNull Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    @Override
    public void handleSuccessResponse(NotepadInfoDTO responseData, int code) {
        if (code == 201){
            setResponseData(responseData);
        }
    }

    @Override
    public void handleErrorResponse(int code){
        String errorMessage;
        switch (code) {
            case 401:
                errorMessage = super.getNotAuth();
                break;
            case 409:
                errorMessage = "Блоктон с таким названием уже существует";
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
        super.setErrorMessage(errorMessage);
    }
}
