package com.example.notesapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.TextNoteDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PutTextNoteRep extends Rep<TextNoteDTO>{
    private int noteId;
    private TextNoteDTO textNoteDTO;
    public PutTextNoteRep(String jwtToken, int noteId, TextNoteDTO textNote){
        super(jwtToken);
        this.noteId = noteId;
        this.textNoteDTO = textNote;
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<TextNoteDTO> call = apiService.putNote(super.getJwtToken(), noteId, textNoteDTO);

        call.enqueue(new Callback<TextNoteDTO>() {
            @Override
            public void onResponse(@NonNull Call<TextNoteDTO> call, @NonNull Response<TextNoteDTO> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TextNoteDTO> call, @NonNull Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    @Override
    public void handleSuccessResponse(TextNoteDTO responseData, int code) {
        if(code == 200){
            setResponseData(responseData);
        }
    }

    @Override
    public void handleErrorResponse(int code){
        String errorMessage;
        switch (code) {
            case 401:
                errorMessage = getNotAuth();
                break;
            case 403:
                errorMessage = "Недостаточно прав";
                break;
            case 404:
                errorMessage = "Такой заметки нет";
                break;
            case 409:
                errorMessage = "В этом блокноте уже существует заметка с таким названием";
                break;
            default:
                errorMessage = "Ошибка сервера: " + code;
        }
        setErrorMessage(errorMessage);
    }
}