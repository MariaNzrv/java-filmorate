package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
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
    private Set<Integer> likes; // список лайков (состоит из Id пользоваелей, которым понравился фильм)


}
