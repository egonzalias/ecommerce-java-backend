package co.com.egonzalias.service;

import co.com.egonzalias.dto.LoginRequestDTO;
import co.com.egonzalias.dto.LoginResponseDTO;
import co.com.egonzalias.dto.RegisterUserDTO;


public interface UserService {
    void registerUser(RegisterUserDTO dto);
    LoginResponseDTO login(LoginRequestDTO request);
}



