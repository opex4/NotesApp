package com.example.notesapp.api;

import com.example.notesapp.dto.*;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // Регистрация пользователя
    @POST("register")
    Call<RegStructDTO> registerUser(@Body RegStructDTO registerUser);

    // Логин пользователя
    @POST("login")
    Call<AuthStructDTO> loginUser(@Body AuthStructDTO loginUser);
}
