package co.com.egonzalias.controller;

import co.com.egonzalias.dto.LoginRequestDTO;
import co.com.egonzalias.dto.LoginResponseDTO;
import co.com.egonzalias.dto.RegisterUserDTO;
import co.com.egonzalias.entity.Users;
import co.com.egonzalias.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody RegisterUserDTO user){
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}


