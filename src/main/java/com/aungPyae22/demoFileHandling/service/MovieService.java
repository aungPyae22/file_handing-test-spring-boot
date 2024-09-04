package com.aungPyae22.demoFileHandling.service;

import com.aungPyae22.demoFileHandling.dto.MovieDTO;
import com.aungPyae22.demoFileHandling.entity.Movie;
import com.aungPyae22.demoFileHandling.exceptions.PException;
import com.aungPyae22.demoFileHandling.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService implements IMovieService{

    private IFileService iFileService;
    private MovieRepository movieRepository;

    @Autowired
    public MovieService(IFileService iFileService, MovieRepository movieRepository){
        this.iFileService = iFileService;
        this.movieRepository = movieRepository;
    }

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException {
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new PException("Filename is already exist please use another file name");
        }

        String uploadFileName = iFileService.uploadFile(path, file);

        movieDTO.setPoster(uploadFileName);

        Movie movie = new Movie(
                movieDTO.getTitle(),
                movieDTO.getDirector(),
                movieDTO.getStudio(),
                movieDTO.getMovieCast(),
                movieDTO.getReleaseYear(),
                movieDTO.getPoster()
        );

        Movie uploadMovie = movieRepository.save(movie);

        String posterUrl = baseUrl + "/file/"+uploadFileName;

        MovieDTO returnMovieDTO = new MovieDTO(
                uploadMovie.getId(),
                uploadMovie.getTitle(),
                uploadMovie.getDirector(),
                uploadMovie.getStudio(),
                uploadMovie.getMovieCast(),
                uploadMovie.getReleaseYear(),
                uploadMovie.getPoster(),
                posterUrl
        );

        return returnMovieDTO;
    }

    @Override
    public MovieDTO getMovie(Integer movieId) throws FileNotFoundException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new PException("user is not found in sever.."));

        //generate posterUrl
        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        MovieDTO movieDTO = new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return movieDTO;
    }

    @Override
    public List<MovieDTO> getAllMovie() throws IOException {
        List<Movie> movieList = movieRepository.findAll();
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for(Movie movie : movieList){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDTO movieDTO = new MovieDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            movieDTOList.add(movieDTO);
        }
        return movieDTOList;
    }

    @Override
    public MovieDTO updateMovie(Integer id, MovieDTO movieDTO, MultipartFile file) throws IOException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new PException("cannot found movie in sever.."));

        String oldFileName = movie.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + oldFileName));
            oldFileName = iFileService.uploadFile(path, file);
        }

        movieDTO.setPoster(oldFileName);

        String uploadUrl = baseUrl + "/file/"+oldFileName;

        //update movie
        movie.setId(id);
        movie.setTitle(movieDTO.getTitle());
        movie.setDirector(movieDTO.getDirector());
        movie.setStudio(movieDTO.getStudio());
        movie.setMovieCast(movieDTO.getMovieCast());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setPoster(movieDTO.getPoster());
        movieRepository.save(movie);


        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                uploadUrl
        );


    }

    @Override
    public String deleteMovie(Integer id) throws IOException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new PException("movie is not found in sever.."));
        String fileName = movie.getPoster();

        Files.deleteIfExists(Paths.get(path + File.separator + fileName));

        movieRepository.delete(movie);
        return "Successfully deleted";
    }


}
