package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable("reviewId") Long reviewId) {
        reviewService.delete(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review findById(@PathVariable("reviewId") Long reviewId) {
        return reviewService.findById(reviewId);
    }

    @GetMapping
    public List<Review> findAll(@RequestParam(defaultValue = "10") int count,
                                @RequestParam(required = false) Long filmId) {
        return reviewService.findAll(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") Long reviewId, @PathVariable Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLike(@PathVariable("reviewId") Long reviewId, @PathVariable Long userId) {
        reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislike(@PathVariable("reviewId") Long reviewId, @PathVariable Long userId) {
        reviewService.deleteDislike(reviewId, userId);
    }
}
