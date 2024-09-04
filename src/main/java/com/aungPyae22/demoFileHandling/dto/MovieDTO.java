package com.aungPyae22.demoFileHandling.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    private Integer id;

    @NotBlank(message = "title is required..")
    private String title;

    @NotBlank(message = "director is required..")
    private String director;

    @NotBlank(message = "studio is required..")
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "poster is required..")
    private String poster;

    @NotBlank(message = "poster url is required..")
    private String posterUrl;
}
