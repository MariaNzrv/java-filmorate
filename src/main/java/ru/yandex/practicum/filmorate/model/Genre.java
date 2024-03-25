package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    // жанр
    private Integer id; // идентификатор
    @NonNull
    private GenreName name; //название
}
