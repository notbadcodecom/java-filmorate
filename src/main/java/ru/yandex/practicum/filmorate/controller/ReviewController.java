package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Reviews", description = "reviews API")
@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Add a film review", description = "Add a film review")
    @ApiResponse(responseCode = "201", description = "Create",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Review.class))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.add(review);
    }

    @Operation(summary = "Update review", description = "Update review by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Review.class))})
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @Operation(summary = "Delete review by id", description = "Delete review by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
    }

    @Operation(summary = "Get review by id", description = "Get review by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Review.class))})
    @GetMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public Review findById(@PathVariable("reviewId") Long reviewId) {
        return reviewService.findById(reviewId);
    }

    @Operation(summary = "Get reviews",
            description = "Get reviews for all movies by default. " +
                    "To get a review of a certain movie, " +
                    "you can specify the \"filmId\" in the query. " +
                    "If you need to limit the number of response values, " +
                    "you can specify count.")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Review.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> findAll(@RequestParam(defaultValue = "10") int count,
                                @RequestParam(required = false) Long filmId) {
        return reviewService.findAll(filmId, count);
    }

    @Operation(summary = "Add like to movie",
            description = "Add like to movie by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @Operation(summary = "Add dislike to movie",
            description = "Add dislike to movie by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PutMapping("/{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDislike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @Operation(summary = "Delete like of movie",
            description = "Delete like of movie by id")
    @DeleteMapping("/{reviewId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("reviewId") Long reviewId, @PathVariable Long userId) {
        reviewService.deleteLike(reviewId, userId);
    }

    @Operation(summary = "Delete dislike of movie",
            description = "Delete dislike of movie by id")
    @DeleteMapping("/{reviewId}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDislike(@PathVariable("reviewId") Long reviewId, @PathVariable Long userId) {
        reviewService.deleteDislike(reviewId, userId);
    }
}
