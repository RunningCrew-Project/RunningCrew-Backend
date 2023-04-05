package com.project.runningcrew.notification.repository;

import com.project.runningcrew.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
public class NotificationJdbcRepositoryImpl implements NotificationJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 여러개의 notification 을 벌크연산을 통해 저장한다.
     *
     * @param notifications notification 의 List
     */
    @Override
    public void saveAllCustom(List<Notification> notifications) {
        String sql = "insert into notifications (user_id, crew_id, content, type, reference_id) " +
                "values (:userId, :crewId, :content, :type, :referenceId)";

        Map<String, Object>[] valueMaps = new HashMap[notifications.size()];
        int count = 0;
        for (Notification nf : notifications) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", nf.getUser().getId());
            map.put("crewId", nf.getCrew().getId());
            map.put("content", nf.getContent());
            map.put("type", nf.getType().toString());
            map.put("referenceId", nf.getReferenceId());
            valueMaps[count++] = map;
        }
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(valueMaps);
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }

}
