package com.aungPyae22.demoFileHandling.repositories;

import com.aungPyae22.demoFileHandling.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Integer> {

}
