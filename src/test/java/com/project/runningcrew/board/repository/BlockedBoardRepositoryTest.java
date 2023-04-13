package com.project.runningcrew.board.repository;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BlockedBoardRepositoryTest {

    /*

    @Autowired UserRepository userRepository;
    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    BlockedBoardRepository blockedBoardRepository;
    @Autowired TestEntityFactory testEntityFactory;


    @DisplayName("BlockBoard save 테스트")
    @Test
    void saveTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .dongArea(dongArea)
                        .build()
        );
        String title = "title";
        String detail = "detail";
        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        Board board = boardRepository.save(new FreeBoard(member, title, detail));
        //when
        BlockedBoard blockedBoard = new BlockedBoard(board, member);
        BlockedBoard savedBlockedBoard = blockedBoardRepository.save(blockedBoard);
        //then
        Assertions.assertThat(savedBlockedBoard).isEqualTo(blockedBoard);
    }



    @DisplayName("BlockedBoard findById 테스트")
    @Test
    void findByIdTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .dongArea(dongArea)
                        .build()
        );
        String title = "title";
        String detail = "detail";
        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        Board board = boardRepository.save(new FreeBoard(member, title, detail));
        //when
        BlockedBoard savedBlockedBoard = blockedBoardRepository.save(new BlockedBoard(board, member));
        Optional<BlockedBoard> findBlockedBoardOpt = blockedBoardRepository.findById(savedBlockedBoard.getId());
        //then
        Assertions.assertThat(findBlockedBoardOpt).isNotEmpty();
        Assertions.assertThat(findBlockedBoardOpt).hasValue(savedBlockedBoard);
    }



    @DisplayName("BlockedBoard delete 테스트")
    @Test
    void deleteTest() throws Exception {
        //given
        SidoArea sidoArea = testEntityFactory.getSidoArea(0);
        GuArea guArea = testEntityFactory.getGuArea(sidoArea, 0);
        DongArea dongArea = testEntityFactory.getDongArea(guArea, 0);
        User user = userRepository.save(
                User.builder()
                        .email("email@email.com")
                        .password("password123!")
                        .name("name")
                        .nickname("nickname")
                        .imgUrl("imgUrl")
                        .login_type(LoginType.EMAIL)
                        .phoneNumber("phoneNumber")
                        .dongArea(dongArea)
                        .sex(Sex.MAN)
                        .birthday(LocalDate.now())
                        .height(100)
                        .weight(100)
                        .build()
        );
        Crew crew = crewRepository.save(
                Crew.builder()
                        .name("name")
                        .introduction("introduction")
                        .crewImgUrl("crewImgUrl")
                        .dongArea(dongArea)
                        .build()
        );
        String title = "title";
        String detail = "detail";
        Member member = memberRepository.save(new Member(user, crew, MemberRole.ROLE_NORMAL));
        Board board = boardRepository.save(new FreeBoard(member, title, detail));
        //when
        BlockedBoard savedBlockedBoard = blockedBoardRepository.save(new BlockedBoard(board, member));
        blockedBoardRepository.delete(savedBlockedBoard);
        Optional<BlockedBoard> findBlockedBoardOpt = blockedBoardRepository.findById(savedBlockedBoard.getId());
        //then
        Assertions.assertThat(findBlockedBoardOpt).isEmpty();
    }
    **/
}