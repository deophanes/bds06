package com.devsuperior.movieflix.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.projection.MovieProjection;
import com.devsuperior.movieflix.entities.projection.ReviewProjection;
import com.devsuperior.movieflix.services.MovieService;

@RestController
@RequestMapping(value = "/movies")
public class MovieResource {

	@Autowired
	private MovieService movieService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Movie> findById(@PathVariable Long id) {
		Optional<MovieProjection> movieOptional = movieService.findById(id);
		if (movieOptional.isPresent()) {
			Movie movie = new Movie(Long.valueOf(movieOptional.get().getId()), movieOptional.get().getTitle(),
					movieOptional.get().getSubTitle(), movieOptional.get().getSynopsis(),
					Integer.valueOf(movieOptional.get().getYear()), movieOptional.get().getImgUrl(),
					new Genre(Long.valueOf(movieOptional.get().getIdGenre()), movieOptional.get().getName(), null),
					null);
			return new ResponseEntity<Movie>(movie, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping
	public ResponseEntity<Page<MovieDTO>> findByGenre(@RequestParam(required = false) Integer genreId,
			Pageable pageable) {
		Page<MovieDTO> lista = movieService.findByGenre(genreId, pageable);
		return ResponseEntity.ok().body(lista);
	}

	@GetMapping(value = "/{id}/reviews")
	public ResponseEntity<ReviewDTO> findByReview(@PathVariable Long id) {
		Optional<ReviewProjection> reviewOptional = movieService.findByReview(id);
		if (reviewOptional.isPresent()) {
			ReviewDTO reviewDTO = new ReviewDTO(reviewOptional.get().getId(), reviewOptional.get().getMovieId(),
					reviewOptional.get().getText(), new UserDTO(reviewOptional.get().getUserId(),
							reviewOptional.get().getName(), reviewOptional.get().getEmail()));
			return ResponseEntity.ok().body(reviewDTO);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
