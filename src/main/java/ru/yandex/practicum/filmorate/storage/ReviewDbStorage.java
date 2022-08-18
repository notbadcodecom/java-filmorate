package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Review mapRowToObject(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder().
                reviewId(rs.getLong("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getLong("user_id"))
                .filmId(rs.getLong("film_id"))
                .useful(rs.getInt("mark"))
                .build();
    }

    @Override
    public List<Review> findAll(Long filmId, int count) {
        String sqlFindAll = "select r.*, nvl(sum(mark), 0) as mark " +
                "from reviews as r " +
                "left join REVIEWS_REACTIONS as rr on r.REVIEW_ID = RR.REVIEW_ID " +
                "left join REACTIONS as RE on RR.REVIEW_ID = RE.REACTION_ID " +
                "group by R.REVIEW_ID order by MARK desc limit ?";
        String sqlFindAllByFilm = "select R.*, nvl(sum(MARK), 0) as MARK " +
                "from REVIEWS as R " +
                "left join REVIEWS_REACTIONS as RR on R.REVIEW_ID = RR.REVIEW_ID " +
                "left join REACTIONS as RE on RR.REVIEW_ID = RE.REACTION_ID " +
                "where FILM_ID = ? group by R.REVIEW_ID order by MARK desc limit ?";
        if (filmId == null) {
            return jdbcTemplate.query(sqlFindAll, this::mapRowToObject, count);
        }

        return jdbcTemplate.query(sqlFindAllByFilm, this::mapRowToObject, filmId, count);
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        String sql = "select R.*, NVL(SUM(MARK), 0) as MARK " +
                "from REVIEWS as R " +
                "left join REVIEWS_REACTIONS as RR on R.REVIEW_ID = RR.REVIEW_ID " +
                "left join REACTIONS as RE on RR.REVIEW_ID = RE.REACTION_ID " +
                "where R.REVIEW_ID = ? group by R.REVIEW_ID";
        List<Review> review = jdbcTemplate.query(sql, this::mapRowToObject, reviewId);
        if (review.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(review.get(0));
    }

    @Override
    public Review add(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into REVIEWS " +
                "(content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return review;
    }

    @Override
    public Review update(Review review) {
        String sql = "update REVIEWS " +
                "set content = ?, is_positive = ? where REVIEW_ID = ?";
        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());
        return review;
    }

    @Override
    public void delete(Long reviewId) {
        String sql = "delete from REVIEWS where REVIEW_ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        String sql = "merge into REVIEWS_REACTIONS " +
                "values (?, ?, 2)";
        jdbcTemplate.update(sql, reviewId, userId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        String sql = "merge into  REVIEWS_REACTIONS " +
                "values (?, ?, 1)";
        jdbcTemplate.update(sql, reviewId, userId);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        String sql = "delete from REVIEWS_REACTIONS " +
                "where REVIEW_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, userId);
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        String sql = "delete from REVIEWS_REACTIONS " +
                "where REVIEW_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, userId);
    }

}
