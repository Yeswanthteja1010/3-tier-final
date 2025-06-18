package com.example.devopsapp.service;

import com.example.devopsapp.model.User;
import com.example.devopsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

   @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


  //  public User saveUser(User user) {
    //    user.setPassword(passwordEncoder.encode(user.getPassword()));
     //   return userRepository.save(user);
   // }
    @Transactional
    public User saveUser(User user) {
    System.out.println("✅ Saving user: " + user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
	}
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

