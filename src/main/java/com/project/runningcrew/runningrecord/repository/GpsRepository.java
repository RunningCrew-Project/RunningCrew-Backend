package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.runningrecord.entity.Gps;
import com.project.runningcrew.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GpsRepository extends JpaRepository<Gps, Long> {

    /**
     * user 에 포함된 모든 GPS 를 삭제한다.
     *
     * @param user
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Gps g1 set g1.deleted = true where g1 in " +
            "(select g2 from Gps g2 " +
            "inner join g2.runningRecord r on r.deleted = false " +
            "where r.user = :user)")
    void deleteAllByUser(@Param("user") User user);

    @Query("select g.id from Gps g " +
            "inner join g.runningRecord r on r.deleted = false " +
            "where r.user = :user")
    List<Long> findIdsByUser(@Param("user") User user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Gps g set g.deleted = true where g.id in :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

}
