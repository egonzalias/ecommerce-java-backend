package co.com.egonzalias.service;
import co.com.egonzalias.dto.JwtUserInfo;
import co.com.egonzalias.dto.LoginRequestDTO;
import co.com.egonzalias.dto.LoginResponseDTO;
import co.com.egonzalias.dto.RegisterUserDTO;
import co.com.egonzalias.entity.*;
import co.com.egonzalias.exception.CustomError;
import co.com.egonzalias.repository.RoleRepository;
import co.com.egonzalias.repository.UserRepository;
import co.com.egonzalias.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ======================================
    // registerUser tests
    // ======================================
    @Test
    void registerUser_shouldThrowError_whenUsernameExists() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setRole("CUSTOMER");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        CustomError exception = assertThrows(CustomError.class, () -> userService.registerUser(dto));
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void registerUser_shouldThrowError_whenEmailExists() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setRole("CUSTOMER");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        CustomError exception = assertThrows(CustomError.class, () -> userService.registerUser(dto));
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void registerUser_shouldSaveCustomer_whenValid() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setPassword("123456");
        dto.setRole("CUSTOMER");

        Roles role = new Roles();
        role.setName("CUSTOMER");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("123456")).thenReturn("hashed123456");

        userService.registerUser(dto);

        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(captor.capture());

        Users savedUser = captor.getValue();
        assertEquals("john", savedUser.getUsername());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("hashed123456", savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
        assertTrue(savedUser instanceof Customer);
    }

    // ======================================
    // login tests
    // ======================================
    @Test
    void login_shouldThrowError_whenUsernameInvalid() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("john");
        request.setPassword("123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        CustomError exception = assertThrows(CustomError.class, () -> userService.login(request));
        assertEquals("Invalid username", exception.getMessage());
    }

    @Test
    void login_shouldThrowError_whenPasswordInvalid() {
        Users user = new Customer();
        user.setUsername("john");
        user.setPassword("hashed123");
        Roles role = new Roles();
        role.setName("CUSTOMER");
        user.setRole(role);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("john");
        request.setPassword("wrong");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed123")).thenReturn(false);

        CustomError exception = assertThrows(CustomError.class, () -> userService.login(request));
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void login_shouldReturnJwt_whenValid() {
        Users user = new Customer();
        user.setUsername("john");
        user.setPassword("hashed123");
        Roles role = new Roles();
        role.setName("CUSTOMER");
        user.setRole(role);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("john");
        request.setPassword("123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "hashed123")).thenReturn(true);
        when(jwtUtil.generateJwt(any(JwtUserInfo.class))).thenReturn("fake-jwt");

        LoginResponseDTO response = userService.login(request);

        assertEquals("fake-jwt", response.getToken());
    }
}
