package com.project.runningcrew.userrole.repository;

import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.Role;
import com.project.runningcrew.userrole.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRoleJdbcRepositoryImpl implements UserRoleJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRole userRole = UserRole.builder()
                .id(rs.getLong("user_role_id"))
                .role(Role.getRole(rs.getString("role")))
                .build();
        userRole.updateDeleted(rs.getBoolean("deleted"));

        return userRole;
    }

    @Override
    public Optional<UserRole> findByUserForAdmin(User user) {
        try {
            String sql = "select * from user_roles where user_id = :userId";
            UserRole userRole = namedParameterJdbcTemplate.queryForObject(sql, Map.of("userId", user.getId()), this::mapRow);
            return Optional.of(userRole);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void deleteForAdmin(UserRole userRole) {
        String sql = "delete from user_roles where user_role_id = :userRoleId";
        namedParameterJdbcTemplate.update(sql, Map.of("userRoleId", userRole.getId()));
    }

}
