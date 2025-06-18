/**package com.example.devopsapp.service;

import com.example.devopsapp.model.User;
import com.example.devopsapp.repository.UserRepository;
import com.example.devopsapp.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.rabbitTemplate = rabbitTemplate;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Send a message to RabbitMQ queue
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE,
                "New user registered: " + savedUser.getUsername());

        return savedUser;
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
**/

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        User savedUser = userRepository.save(user);
        return userRepository.save(user);
        // Potential exception from RabbitMQ could rollback the transaction
      //  try {
        //    rabbitTemplate.convertAndSend(RabbitMQConfig.USER_QUEUE,
         //           "New user registered: " + savedUser.getUsername());
       // } catch (Exception e) {
            // Log and don't block DB commit if RabbitMQ fails
         //   System.err.println("RabbitMQ error: " + e.getMessage());
        //}

       // return savedUser;
	}
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

