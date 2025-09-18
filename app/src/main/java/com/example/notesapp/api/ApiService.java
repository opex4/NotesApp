package com.example.notesapp.api;

import com.example.notesapp.dto.*;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // Регистация пользователя
    @POST("register")
    Call<JwtTokenDTO> registerUser(@Body RegStructDTO registerUser);

    // Логин пользователя
    @POST("login")
    Call<JwtTokenDTO> loginUser(@Body AuthStructDTO loginUser);

    // Получить список нотпадов
    @GET("notepads")
    Call<ArrayList<NotepadInfoDTO>> getNotepads(@Header("Authorization") String jwt);

    // Создать новый нотпад
    @POST("notepads")
    Call<NotepadInfoDTO> postNotepad(@Header("Authorization") String jwt, @Body NameDTO newNotepad);

    // Получить инфу о себе
    @GET("users/me")
    Call<UserDTO> getUsersMe(@Header("Authorization") String jwt);
}
