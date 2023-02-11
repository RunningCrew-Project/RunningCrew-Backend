package com.project.runningcrew.repository.runningrecords;

import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonalRunningRecordRepositoryTest {

    @Autowired
    PersonalRunningRecordRepository personalRunningRecordRepository;

    @Autowired
    UserRepository userRepository;

    public User testUser() {
        User user = User.builder().email("email@naver.com")
                .name("name")
                .nickname("nickname")
                .imgUrl("imgUrl")
                .login_type(LoginType.EMAIL)
                .phoneNumber("010-0000-0000")
                .password("123a!")
                .location("location")
                .sex(Sex.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .height(170)
                .weight(70)
                .build();
        return userRepository.save(user);
    }

    @Test
    public void saveTest() {
        //given
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(testUser())
                .build();

        ///when
        PersonalRunningRecord savedPersonalRunningRecord = personalRunningRecordRepository.save(personalRunningRecord);

        //then
        assertThat(savedPersonalRunningRecord).isEqualTo(personalRunningRecord);
    }

    @Test
    public void findById() {
        //given
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(testUser())
                .build();
        personalRunningRecordRepository.save(personalRunningRecord);

        ///when
        Optional<PersonalRunningRecord> optPersonalRunningRecord = personalRunningRecordRepository
                .findById(personalRunningRecord.getId());

        //then
        assertThat(optPersonalRunningRecord).isNotEmpty();
        assertThat(optPersonalRunningRecord).hasValue(personalRunningRecord);
    }

    @Test
    public void delete() {
        //given
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(testUser())
                .build();
        personalRunningRecordRepository.save(personalRunningRecord);

        ///when
        personalRunningRecordRepository.delete(personalRunningRecord);

        //then
        Optional<PersonalRunningRecord> optPersonalRunningRecord = personalRunningRecordRepository
                .findById(personalRunningRecord.getId());
        assertThat(optPersonalRunningRecord).isEmpty();
    }

    @Test
    public void findAllByUserTest() {
        //given
        User user = testUser();
        for (int i = 0; i < 10; i++) {
            PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                    .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                    .runningDistance(3.1)
                    .runningTime(1000)
                    .runningFace(1000)
                    .calories(300)
                    .running_detail(String.valueOf(i))
                    .user(user)
                    .build();
            personalRunningRecordRepository.save(personalRunningRecord);
        }

        ///when
        List<PersonalRunningRecord> personalRunningRecords = personalRunningRecordRepository.findAllByUser(user);

        //then
        assertThat(personalRunningRecords.size()).isSameAs(10);
        for (PersonalRunningRecord personalRunningRecord : personalRunningRecords) {
            assertThat(personalRunningRecord.getUser()).isEqualTo(user);
        }
    }

}