package com.project.runningcrew.repository.images;

import com.project.runningcrew.entity.images.RunningRecordImage;
import com.project.runningcrew.entity.runningrecords.PersonalRunningRecord;
import com.project.runningcrew.entity.runningrecords.RunningRecord;
import com.project.runningcrew.entity.users.LoginType;
import com.project.runningcrew.entity.users.Sex;
import com.project.runningcrew.entity.users.User;
import com.project.runningcrew.repository.CrewRepository;
import com.project.runningcrew.repository.MemberRepository;
import com.project.runningcrew.repository.UserRepository;
import com.project.runningcrew.repository.runningrecords.PersonalRunningRecordRepository;
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
class RunningRecordImageRepositoryTest {

    @Autowired
    RunningRecordImageRepository runningRecordImageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PersonalRunningRecordRepository personalRunningRecordRepository;

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

    public RunningRecord testPersonalRunningRecord() {
        PersonalRunningRecord personalRunningRecord = PersonalRunningRecord.builder()
                .startDateTime(LocalDateTime.of(2023, 2, 11, 15, 0))
                .runningDistance(3.1)
                .runningTime(1000)
                .runningFace(1000)
                .calories(300)
                .running_detail("")
                .user(testUser())
                .build();
        return personalRunningRecordRepository.save(personalRunningRecord);
    }


    @Test
    public void save() {
        //given
        RunningRecordImage runningRecordImage = new RunningRecordImage("fileName", testPersonalRunningRecord());

        ///when
        RunningRecordImage savedRunningRecordImage = runningRecordImageRepository.save(runningRecordImage);

        //then
        assertThat(savedRunningRecordImage).isEqualTo(runningRecordImage);
    }

    @Test
    public void findById() {
        //given
        RunningRecordImage runningRecordImage = new RunningRecordImage("fileName", testPersonalRunningRecord());
        runningRecordImageRepository.save(runningRecordImage);

        ///when
        Optional<RunningRecordImage> optRunningRecordImage = runningRecordImageRepository.findById(runningRecordImage.getId());

        //then
        assertThat(optRunningRecordImage).isNotEmpty();
        assertThat(optRunningRecordImage).hasValue(runningRecordImage);
    }

    @Test
    public void delete() {
        //given
        RunningRecordImage runningRecordImage = new RunningRecordImage("fileName", testPersonalRunningRecord());
        runningRecordImageRepository.save(runningRecordImage);

        ///when
        runningRecordImageRepository.delete(runningRecordImage);

        //then
        Optional<RunningRecordImage> optRunningRecordImage = runningRecordImageRepository.findById(runningRecordImage.getId());
        assertThat(optRunningRecordImage).isEmpty();
    }

    @Test
    public void findAllByRunningRecordTest() {
        //given
        RunningRecord runningRecord = testPersonalRunningRecord();
        for (int i = 0; i < 10; i++) {
            RunningRecordImage runningRecordImage = new RunningRecordImage("fileName" + i, runningRecord);
            runningRecordImageRepository.save(runningRecordImage);
        }

        ///when
        List<RunningRecordImage> runningRecordImages = runningRecordImageRepository.findAllByRunningRecord(runningRecord);

        //then
        assertThat(runningRecordImages.size()).isSameAs(10);
        for (RunningRecordImage runningRecordImage : runningRecordImages) {
            assertThat(runningRecordImage.getRunningRecord()).isEqualTo(runningRecord);
        }
    }

}