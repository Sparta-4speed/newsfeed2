package com.sparta.newsfeed.user.service;

import com.sparta.newsfeed.config.PasswordEncoder;
import com.sparta.newsfeed.entity.UserRoleEnum;
import com.sparta.newsfeed.entity.Users;
import com.sparta.newsfeed.user.UsersUtil.UsersUtil;
import com.sparta.newsfeed.user.otherDto.MyProfileResponseDto;
import com.sparta.newsfeed.user.otherDto.ProfileResponseDto;
import com.sparta.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static com.sparta.newsfeed.entity.UserRoleEnum.ROLE_ADMIN;
import static com.sparta.newsfeed.entity.UserRoleEnum.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersUtil usersUtil;

    public void signup(String username, String pw, String email, boolean role) {
        checkUsername(username);
        String password = passwordEncoder.encode(pw);

        Optional<Users> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email이 존재합니다.");
        }

        UserRoleEnum checkedRole;
        if (role) {
            checkedRole = ROLE_USER;
        } else {
            checkedRole = ROLE_ADMIN;
        }

        Users user = new Users(username, password, email, checkedRole);
        userRepository.save(user);
    }

    public MyProfileResponseDto getMyProfile(Long userId) {
        return new MyProfileResponseDto(usersUtil.findById(userId));
    }

    public ProfileResponseDto getProfile(Long userId) {
        return new ProfileResponseDto(usersUtil.findById(userId));
    }

    public Boolean checkIdPw(String email, String password) {
        Users user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("이메일이 잘못되었습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못 되었습니다");
        }
        return true;
    }

    @Transactional
    public void updateUser(Long userId, String username, String introduction) {
        checkUsername(username);
        Users user = usersUtil.findById(userId);
        user.setUsername(username);
        user.setIntroduction(introduction);
    }

    public void delete(Long userId) {
        Users user = usersUtil.findById(userId);
        userRepository.deleteById(user.getId());
    }

    private void checkUsername(String username) {
        Optional<Users> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 이름이 존재합니다.");
        }
    }
}
