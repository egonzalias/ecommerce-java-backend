package co.com.egonzalias.service.impl.impl;

import co.com.egonzalias.dto.JwtUserInfo;
import co.com.egonzalias.dto.LoginRequestDTO;
import co.com.egonzalias.dto.LoginResponseDTO;
import co.com.egonzalias.dto.RegisterUserDTO;
import co.com.egonzalias.entity.Admin;
import co.com.egonzalias.entity.Customer;
import co.com.egonzalias.entity.Roles;
import co.com.egonzalias.entity.Users;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.RoleRepository;
import co.com.egonzalias.repository.UserRepository;
import co.com.egonzalias.security.JwtUtil;
import co.com.egonzalias.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterUserDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomError("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomError("Email already exists");
        }

        Roles role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new CustomError("Role not found"));

        Users user;
        switch (role.getName()) {
            case "ADMIN":
                user = new Admin();
                break;
            case "CUSTOMER":
                user = new Customer();
                break;
            default:
                throw new CustomError("Unsupported role: " + role.getName());
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);

        userRepository.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        Users user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomError("Invalid username"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomError("Invalid password");
        }
        JwtUserInfo userInfo = new JwtUserInfo(user.getUsername(), user.getRole().getName());

        return new LoginResponseDTO(jwtUtil.generateJwt(userInfo));
    }
}
