package com.project.runningcrew.user.repository;

import com.project.runningcrew.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserJdbcRepositoryImpl implements UserJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .nickname(rs.getString("nickname"))
                .imgUrl(rs.getString("img_url"))
                .build();
        user.updateDeleted(rs.getBoolean("deleted"));

        return user;
    }

    @Override
    public Optional<User> findByIdForAdmin(Long id) {
        try {
            String sql = "select * from users where user_id = :userId";
            User user = namedParameterJdbcTemplate.queryForObject(sql, Map.of("userId", id), this::mapRow);
            return Optional.of(user);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailForAdmin(String email) {
        try {
            String sql = "select * from users where email = :email";
            User user = namedParameterJdbcTemplate.queryForObject(sql, Map.of("email", email), this::mapRow);
            return Optional.of(user);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void rollbackUser(Long id) {
        String sql = "update users set name = null, nickname = null, dong_area_id = null, " +
                "img_url = null, sex = null, birthday = null, height = null, weight = null, " +
                "password = null, phone_number = null, deleted = false  where user_id = :userId";
        namedParameterJdbcTemplate.update(sql, Map.of("userId", id));
    }

}
