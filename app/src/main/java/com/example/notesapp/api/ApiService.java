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
    Call<NotepadInfoDTO> postNotepad(
            @Header("Authorization") String jwt,
            @Body NameDTO newNotepad
    );

    // Удалить нотпад
    @DELETE("notepads/{id}")
    Call<Void> deleteNotepad(
            @Header("Authorization") String jwt,
            @Path("id") int id
    );

    // Получить инфу о себе
    @GET("users/me")
    Call<UserDTO> loadUsersMe(@Header("Authorization") String jwt);

    // Получить инфу о блокноте
    @GET("notepads/{id}")
    Call<NotepadInfoDTO> loadNotepad(
            @Header("Authorization") String jwt,
            @Path("id") int id
    );

    // Загрузить заметку
    @GET("notes/{id}")
    Call<TextNoteDTO> loadTextNote(
            @Header("Authorization") String jwt,
            @Path("id") int id
    );

    // Создать заметку
    @POST("notepads/{id}/notes")
    Call<Void> createNote(
            @Header("Authorization") String jwt,
            @Path("id") int id,
            @Body TextNoteDTO textNoteDTO
    );

    // Удалить заметку
    @DELETE("notes/{id}")
    Call<Void> deleteNote(
            @Header("Authorization") String jwt,
            @Path("id") int id
    );

    // Изменить заметку
    @PUT("notes/{id}")
    Call<TextNoteDTO> putNote(
            @Header("Authorization") String jwt,
            @Path("id") int id,
            @Body TextNoteDTO textNoteDTO
    );
}
