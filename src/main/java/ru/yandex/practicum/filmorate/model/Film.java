package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Фильм.
 */
@Data
@NoArgsConstructor
public class Film {
    private Integer id; //идентификатор
    @NonNull
    private String name; //название
    private String description; //описание
    private LocalDate releaseDate; // дата релиза
    private Long duration; // продолжительность фильма
    private Set<Integer> likes = new HashSet<>(); // список лайков (состоит из Id пользоваелей, которым понравился фильм)
    private List<Genre> genres = new ArrayList<>(); // список жанров
}
