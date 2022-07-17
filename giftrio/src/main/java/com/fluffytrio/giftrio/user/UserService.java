package com.fluffytrio.giftrio.user;

import com.fluffytrio.giftrio.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User addUser(UserRequestDto userRequestDto) {
        if (userRepository.getByEmail(userRequestDto.getEmail()).size() > 0) {
            throw new IllegalArgumentException("이미 가입된 메일 주소입니다.");
        }
        return userRepository.save(userRequestDto.toEntity());
    }

    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long userId, User newUserInfo) {
        return userRepository.save(newUserInfo);
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().delete();
            return userRepository.save(user.get()).isDelete();
        }

        return false;
    }
}
