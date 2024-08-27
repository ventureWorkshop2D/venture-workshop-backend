package com.project.homeListener.login.user.service;

import com.project.homeListener.login.user.dto.request.RegisterRequestDTO;
import com.project.homeListener.login.user.entity.CustomUserDetails;
import com.project.homeListener.login.user.entity.User;
import com.project.homeListener.login.user.repository.UserRepository;
import com.project.homeListener.login.user.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDTO registerRequestDTO) {
        String newPassword = passwordEncoder.encode(registerRequestDTO.password());

        userRepository.save(UserMapper.INSTANCE.toEntity(registerRequestDTO, newPassword));
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(user);
    }

    public String findEmailByUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")).getEmail();
    }
}
