package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public List<Review> findAll(Long filmId, int count) throws NotFoundException {
        log.info("Load reviews by film with id={}", filmId);
        return reviewStorage.findAll(filmId, count);
    }

    public Review findById(Long reviewId) {
        Review foundReview = reviewStorage.findById(reviewId).orElseThrow(() ->
                new NotFoundException("Review #" + reviewId + " not found"));
        log.info("Load review with id={}", reviewId);
        return foundReview;
    }

    public Review add(Review review) {
        if (review.getFilmId() == null || review.getFilmId() <= 0) {
            throw new NotFoundException("Review with filmID #" + review.getFilmId() + " not found");
        } else if (review.getUserId() == null || review.getUserId() <= 0) {
            throw new NotFoundException("Review with userId #" + review.getFilmId() + " not found");
        }
        Review addedReview = reviewStorage.add(review);
        log.info("add new review with id={}", addedReview.getReviewId());
        return addedReview;
    }

    public Review update(Review review) {
        log.info("update review with id={}", review.getReviewId());
        return reviewStorage.update(review);
    }

    public void delete(Long reviewId) {
        log.info("delete review with id {}", reviewId);
        reviewStorage.delete(reviewId);
    }

    public void addLike(Long reviewId, Long userId) {
        log.info("User with id={} add like review with id={}", userId, reviewId);
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        log.info("User with id={} add dislike review with id={}", userId, reviewId);
        reviewStorage.addDislike(reviewId, userId);
    }

    public void deleteLike(Long reviewId, Long userId) {
        log.info("User with id={} delete like review with id={}", userId, reviewId);
        reviewStorage.deleteLike(reviewId, userId);
    }

    public void deleteDislike(Long reviewId, Long userId) {
        log.info("User with id={} delete dislike review with id={}", userId, reviewId);
        reviewStorage.deleteDislike(reviewId, userId);
    }

}
