package com.aungPyae22.demoFileHandling.service;

import com.aungPyae22.demoFileHandling.dto.MovieDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IMovieService {

    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException;

    MovieDTO getMovie(Integer movieId) throws FileNotFoundException;

    List<MovieDTO> getAllMovie() throws IOException;

    MovieDTO updateMovie(Integer id, MovieDTO movieDTO, MultipartFile file) throws IOException;

    String deleteMovie(Integer id) throws IOException;

}
