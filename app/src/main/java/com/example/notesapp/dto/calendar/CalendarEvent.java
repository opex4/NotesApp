package com.example.notesapp.dto.calendar;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CalendarEvent {
    private final List<Event> events = new ArrayList<>();
}
