package com.example.notesapp.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.TextNoteDTO;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class CreateTextNoteRep extends Rep<Void>{
    private TextNoteDTO textNoteDTO;
    private int notepadId;
    private MutableLiveData<String> successMessage = new MutableLiveData<>();

    public CreateTextNoteRep(String jwtToken, String name, int notepadId){
        super(jwtToken);
        textNoteDTO = new TextNoteDTO();
        textNoteDTO.setName(name);
        textNoteDTO.setType("Text");
        textNoteDTO.setText("");
        this.notepadId = notepadId;
    }

    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.createNote(super.getJwtToken(), notepadId, textNoteDTO);

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
        if(code == 201){
            successMessage.postValue("Записка создана");
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
                errorMessage = "Блокнот не найден";
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
