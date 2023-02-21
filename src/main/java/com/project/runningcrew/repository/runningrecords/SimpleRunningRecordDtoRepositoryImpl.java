package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.dto.SimpleRunningRecordDto;
import com.project.runningcrew.entity.users.User;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class SimpleRunningRecordDtoRepositoryImpl implements SimpleRunningRecordDtoRepository {

    private final EntityManager em;

    @Override
    public Slice<SimpleRunningRecordDto> findSimpleRunningRecordDtoByUser(User user, Pageable pageable) {

        String sql = "select rr.running_record_id as runningRecordId, rr.running_type as runningType, " +
                "rr.start_date_time as startDateTime, rr.running_distance as runningDistance, " +
                "rr.running_time as runningTime, rr.running_face as runningFace, " +
                "c.name as crewName, rc.notice_type as noticeType " +
                "from running_records as rr " +
                "left join crews as c " +
                "on c.crew_id = rr.crew_id " +
                "left join running_notices as rc " +
                "on rc.running_notice_id = rr.running_notice_id " +
                "where rr.user_id = ?";
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        Query nativeQuery = em.createNativeQuery(sql).setParameter(1, user.getId())
                .setFirstResult(page * size)
                .setMaxResults(size + 1);
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        List<SimpleRunningRecordDto> list = jpaResultMapper.list(nativeQuery, SimpleRunningRecordDto.class);
        boolean hasNext = list.size() == size + 1;
        if (hasNext) {
            list = list.subList(0, size);
        }
        SliceImpl<SimpleRunningRecordDto> result = new SliceImpl<>(list, pageable, hasNext);
        return result;
    }

    @Override
    public List<SimpleRunningRecordDto> findSimpleRunningRecordDtoByByUserAndStartDateTimes(User user, LocalDateTime start, LocalDateTime end) {

        String sql = "select rr.running_record_id as runningRecordId, rr.running_type as runningType, " +
                "rr.start_date_time as startDateTime, rr.running_distance as runningDistance, " +
                "rr.running_time as runningTime, rr.running_face as runningFace, " +
                "c.name as crewName, rc.notice_type as noticeType " +
                "from running_records as rr " +
                "left join crews as c " +
                "on c.crew_id = rr.crew_id " +
                "left join running_notices as rc " +
                "on rc.running_notice_id = rr.running_notice_id " +
                "where rr.user_id = ? and rr.start_date_time >= ? and rr.start_date_time < ?";
        Query nativeQuery = em.createNativeQuery(sql).setParameter(1, user.getId())
                .setParameter(2, start)
                .setParameter(3, end);
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        List<SimpleRunningRecordDto> list = jpaResultMapper.list(nativeQuery, SimpleRunningRecordDto.class);
        return list;
    }
}
