package com.aungPyae22.demoFileHandling.controller;

import com.aungPyae22.demoFileHandling.dto.MovieDTO;
import com.aungPyae22.demoFileHandling.exceptions.PException;
import com.aungPyae22.demoFileHandling.service.IMovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/movies")
public class MovieController {

    private IMovieService iMovieService;

    @Autowired
    public MovieController(IMovieService iMovieService){
        this.iMovieService = iMovieService;
    }

    @PostMapping(path = "/addMovie")
    public ResponseEntity<MovieDTO> addMovie(@RequestPart MultipartFile file,
                                             @RequestPart String movieDTOObj) throws JsonProcessingException, IOException {

        if(file.isEmpty() || movieDTOObj.isBlank() ){
            throw new PException("Please check your form..");
        }

        MovieDTO movieDTO = stringMovieDTOToMovieDTO(movieDTOObj);
        return new ResponseEntity<>(iMovieService.addMovie(movieDTO,file), HttpStatus.CREATED);
    }

    private MovieDTO stringMovieDTOToMovieDTO(String movieDTOObj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(movieDTOObj, MovieDTO.class);
    }

    @GetMapping(path = "/{movieId}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable("movieId") int id) throws IOException {
        if(id < 0 || id >= iMovieService.getAllMovie().size()){
            throw new PException("movie not found,check you id..");
        }
        return new ResponseEntity<>(iMovieService.getMovie(id),HttpStatus.FOUND);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() throws IOException {
        List<MovieDTO> movieDTOList = iMovieService.getAllMovie();
        return new ResponseEntity<>(movieDTOList,HttpStatus.FOUND);
    }

    @PutMapping(path = "/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable("movieId") int id ,
                                                @RequestPart MultipartFile file,
                                                @RequestPart String movieDTOObj) throws IOException {
        if(movieDTOObj.isBlank()){
            throw new PException("Please check field value to update..");
        }
        MovieDTO movieDTO = stringMovieDTOToMovieDTO(movieDTOObj);
        return new ResponseEntity<>(iMovieService.updateMovie(id,movieDTO,file),HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable("movieId") int id) throws IOException {
        return new ResponseEntity<>(iMovieService.deleteMovie(id),HttpStatus.OK);
    }
}
