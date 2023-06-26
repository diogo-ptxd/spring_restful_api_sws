package net.pmarques.user_authreg_restapi.controller;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.pmarques.user_authreg_restapi.dto.RegistrationDto;
import net.pmarques.user_authreg_restapi.dto.UserDto;
import net.pmarques.user_authreg_restapi.entity.Role;
import net.pmarques.user_authreg_restapi.entity.User;
import net.pmarques.user_authreg_restapi.repository.RoleRepository;
import net.pmarques.user_authreg_restapi.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/signin")
  public ResponseEntity<String> authenticateUser(@RequestBody UserDto userDto) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        userDto.getUsernameOrEmail(), userDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegistrationDto registrationDto) {// add check for username exists
                                                                                       // in a DB
    if (userRepository.existsByUsername(registrationDto.getUsername())) {
      return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
    }
    // add check for email exists in DB
    if (userRepository.existsByEmail(registrationDto.getEmail())) {
      return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
    }
    // create user object
    User user = new User();
    user.setName(registrationDto.getName());
    user.setUsername(registrationDto.getUsername());
    user.setEmail(registrationDto.getEmail());
    user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
    Role roles = roleRepository.findByName("ROLE_ADMIN").get();
    user.setRoles(Collections.singleton(roles));
    userRepository.save(user);
    return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
  }
}