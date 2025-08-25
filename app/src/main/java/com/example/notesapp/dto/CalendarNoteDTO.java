package com.example.notesapp.dto;

import com.example.notesapp.dto.calendar.CalendarEvent;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalendarNoteDTO extends NoteDTO {
    Map<Date, CalendarEvent> calendarEvents;
}
