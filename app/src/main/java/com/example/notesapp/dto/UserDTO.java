package com.example.notesapp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    int id;
    String name;
    String email;
}
