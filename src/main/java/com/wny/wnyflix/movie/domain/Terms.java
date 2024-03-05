package com.wny.wnyflix.movie.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Terms {
    private String key;

    private long doc_count;
}
