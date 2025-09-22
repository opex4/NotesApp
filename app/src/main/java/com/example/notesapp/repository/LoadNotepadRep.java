package com.example.notesapp.repository;

import androidx.annotation.NonNull;

import com.example.notesapp.api.ApiService;
import com.example.notesapp.api.RetrofitClient;
import com.example.notesapp.dto.NotepadInfoDTO;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class LoadNotepadRep extends Rep<NotepadInfoDTO>{
    @Setter
    private int id;
    public LoadNotepadRep(String jwtToken, int id){
        super(jwtToken);
        this.id = id;
    }
    @Override
    public void pullData() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<NotepadInfoDTO> call = apiService.loadNotepad(super.getJwtToken(), id);

        call.enqueue(new Callback<NotepadInfoDTO>() {
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
        if(code == 201){ // todo уточнить код через постман
            setResponseData(responseData);
        }
    }
}
