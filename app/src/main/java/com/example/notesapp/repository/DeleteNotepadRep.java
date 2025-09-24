package com.example.notesapp.repository;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class DeleteNotepadRep extends Rep<Void>{
    @Setter
    private int id;
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    public DeleteNotepadRep(String jwtToken, int id){
        super(jwtToken);
        this.id = id;
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.deleteNotepad(super.getJwtToken(), id);

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
            successMessage.postValue("Блокнот удалён");
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
            default:
                errorMessage = "Ошибка сервера: " + code;
        }
        setErrorMessage(errorMessage);
    }
}
