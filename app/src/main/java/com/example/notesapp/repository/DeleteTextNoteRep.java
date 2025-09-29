package com.example.notesapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class DeleteTextNoteRep extends Rep<Void>{
    private int noteId;
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    public DeleteTextNoteRep(String jwtToken, int noteId){
        super(jwtToken);
        this.noteId = noteId;
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.deleteNote(super.getJwtToken(), noteId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response.body(), response.code());
                } else {
                    handleErrorResponse(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                handleNetworkFailure(t);
            }
        });
    }

    @Override
    public void handleSuccessResponse(Void responseData, int code) {
        if(code == 200){
            successMessage.postValue("Заметка удалена");
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
            default:
                errorMessage = "Ошибка сервера: " + code;
        }
        setErrorMessage(errorMessage);
    }
}
