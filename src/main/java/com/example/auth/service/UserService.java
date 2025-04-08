package com.example.auth.service;

import com.example.auth.exception.UsernameAlreadyExistsException;
import com.example.auth.jwt.JwtUtil;
import com.example.auth.model.dto.LoginDTO;
import com.example.auth.model.dto.TokenResponseDTO;
import com.example.auth.model.dto.UserDTO;
import com.example.auth.model.entity.UserEntity;
import com.example.auth.model.mapper.UserMapper;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public TokenResponseDTO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String token = jwtUtil.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities() // Include roles/authorities
            );

            return TokenResponseDTO
                    .builder()
                    .tokenType("Bearer")
                    .accessToken(token)
                    .expiresIn(jwtUtil.getExpirationTime())
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (InternalAuthenticationServiceException e) {
            throw new InternalAuthenticationServiceException("An error occurred during login");
        }
    }

    public UserDTO register(UserDTO userDTO) {
        boolean existsByUsername = userRepository.existsByUsername(userDTO.getUsername());
        if (existsByUsername) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }

        UserEntity entity = userMapper.toEntity(userDTO);
        // Hash the password before saving
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity savedUserEntity = userRepository.save(entity);

        return userMapper.fromEntity(savedUserEntity);
    }

    public List<UserDTO> getUserList() {
        List<UserDTO> all = userRepository.findAll().stream().map(userMapper::fromEntity).toList();
        return all;
    }
}
