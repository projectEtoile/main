package com.keduit.shop.service;
import com.keduit.shop.entity.User;
import com.keduit.shop.repository.UserRepository; // UserRepository를 import해야 합니다.

public class UserService {

    private final UserRepository userRepository; // UserRepository 필드 선언

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userEmailCheck(String userEmail, String userName) {
        User user = userRepository.findByUserid(userEmail); // UserRepository에서 findByUserId 메서드 사용
        return user != null && user.getName().equals(userName);
    }
}