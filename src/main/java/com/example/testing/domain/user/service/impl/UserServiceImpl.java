package com.example.testing.domain.user.service.impl;


import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.dto.UserListResponseDto;
import com.example.testing.domain.user.dto.UserResponseDto;
import com.example.testing.domain.user.exception.EmailDuplicateException;
import com.example.testing.domain.user.repository.UserRepository;
import com.example.testing.domain.user.service.UserService;
import com.example.testing.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserListResponseDto getAllUsers() {

        return new UserListResponseDto(
            userRepository.findAll().stream()
                    .map(UserResponseDto::new)
                    .toList()
        );
    }

    @Override
    public UserResponseDto getUserById(Long id) {

         var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        return new UserResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserCreateRequestDto requestDto) {

        String email = requestDto.email();

        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicateException(email);
        }

        var user = userRepository.save(requestDto.toEntity());

        return new UserResponseDto(user);
    }

}