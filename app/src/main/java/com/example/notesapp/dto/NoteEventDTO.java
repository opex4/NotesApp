package com.example.notesapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteEventDTO {
    String title;
    LocalTime time;
    String description;
}
