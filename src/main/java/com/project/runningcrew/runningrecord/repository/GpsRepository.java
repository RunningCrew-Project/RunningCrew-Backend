package com.project.runningcrew.runningrecord.repository;

import com.project.runningcrew.runningrecord.entity.Gps;
import com.project.runningcrew.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsRepository extends JpaRepository<Gps, Long> {

    /**
     * user 에 포함된 모든 GPS 를 삭제한다.
     *
     * @param user
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Gps g1 where g1 in " +
            "(select g2 from Gps g2 where g2.runningRecord.user = :user)")
    void deleteAllByUser(@Param("user") User user);

}
