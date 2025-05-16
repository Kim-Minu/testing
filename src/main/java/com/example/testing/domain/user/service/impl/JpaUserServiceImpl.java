package com.example.testing.domain.user.service.impl;


import com.example.testing.domain.user.dto.UserCreateRequestDto;
import com.example.testing.domain.user.entity.User;
import com.example.testing.domain.user.repository.UserRepository;
import com.example.testing.domain.user.service.UserService;
import com.example.testing.global.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaUserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public JpaUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Override
    public User createUser(UserCreateRequestDto requestDto) {
        return userRepository.save(requestDto.toEntity());
    }

}