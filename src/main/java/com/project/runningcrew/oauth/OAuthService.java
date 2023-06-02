package com.project.runningcrew.oauth;


import com.project.runningcrew.area.entity.DongArea;
import com.project.runningcrew.area.service.DongAreaService;
import com.project.runningcrew.image.ImageService;
import com.project.runningcrew.oauth.dto.request.OauthDto;
import com.project.runningcrew.oauth.dto.request.SignUpDto;
import com.project.runningcrew.oauth.dto.response.LoginResponse;
import com.project.runningcrew.oauth.oauth2user.Oauth2UserFactory;
import com.project.runningcrew.security.JwtProvider;
import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.user.repository.UserRepository;
import com.project.runningcrew.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {

    private final JwtProvider jwtProvider;
    private final Oauth2UserFactory oauth2UserFactory;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DongAreaService dongAreaService;
    private final ImageService imageService;

    private final String USER_IMG_DIR_NAME = "user";
    private final String DEFAULT_USER_IMG = "유저 기본 이미지.svg";
    private final String DEFAULT_USER_IMG_PATH = USER_IMG_DIR_NAME + '/' + DEFAULT_USER_IMG;

    /**
     * 소셜 로그인. [ OAuth2User, accessToken, refreshToken, initData ] 정보가 포함된다.
     * @param oauthDto 클라이언트가 제공한 로그인 정보.
     * @return 위의 정보가 포함된 LoginResponse.
     */
    public LoginResponse socialLogin(OauthDto oauthDto) {

        OAuth2User oAuth2User = oauth2UserFactory.getOauth2User(oauthDto);

        String accessToken = jwtProvider.createAccessToken(oAuth2User.getUser(), oAuth2User.getUserRole());
        String refreshToken = jwtProvider.createRefreshToken(oAuth2User.getUser());
        boolean initData = initDataInput(oAuth2User.getUser());
        //note : 소셜로그인 정보에 추가정보 기입 유무 포함.

        return new LoginResponse(oAuth2User.getUser(), accessToken, refreshToken, initData);
    }


    /**
     * 초기 데이터는 Email 뿐이다. 나머지 추가정보를 받아서 업데이트한다.
     * @param user 추가정보를 입력할 user 정보.
     * @param signUpDto 회원가입 필수 추가정보 :  [ 이름, 닉네임, 동 ] + @ - 초기 프로필 이미지는 받지 않는다.
     */
    public void signUpData(User user, SignUpDto signUpDto) {
        //note 필수 추가 정보 [ 이름, 닉네임, 동 ]
        userService.duplicateNickname(signUpDto.getNickname());
        user.updateNickname(signUpDto.getNickname());
        user.updateName(signUpDto.getName());
        DongArea dongArea = dongAreaService.findById(signUpDto.getDongId());
        user.updateDongArea(dongArea);

        //note 기본 이미지 적용
        String imgUrl = imageService.getImage(USER_IMG_DIR_NAME, DEFAULT_USER_IMG);
        user.updateImgUrl(imgUrl);


        //note 필수 X
        if (signUpDto.getSex() != null) {
            user.updateSex(signUpDto.getSex());
        }
        if (signUpDto.getBirthday() != null) {
            user.updateBirthday(signUpDto.getBirthday());
        }
        if (signUpDto.getHeight() != null) {
            user.updateHeight(signUpDto.getHeight());
        }
        if (signUpDto.getWeight() != null) {
            user.updateWeight(signUpDto.getWeight());
        }

        userRepository.save(user);
    }


    /**
     * 회원가입 직후 필수 추가 정보를 입력하였는지 확인한다. 모두 입력하였다면 true 를 반환한다.
     * @param user 추가정보를 입력할 user 정보.
     * @return 필수 추가 정보 입력 유무 : [ 이름, 닉네임, 동 ]
     */
    public boolean initDataInput(User user) {
        return (user.getName() != null && user.getNickname() != null && user.getDongArea() != null);
    }

}
