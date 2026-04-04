package com.example.autenticacion;

import com.example.autenticacion.auth.AuthController;
import com.example.autenticacion.auth.dto.RegisterRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import com.example.autenticacion.auth.AuthService;
import com.example.autenticacion.auth.dto.AuthResponse;
import com.example.autenticacion.auth.dto.LoginRequest;
import com.example.autenticacion.auth.dto.RegisterRequest;
import com.example.autenticacion.auth.dto.UserProfileResponse;
import com.example.autenticacion.jwt.JwtService;
import com.example.autenticacion.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    

    // Probamos el login con datos validos
    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest("user@tienda.com", "123456");

                UserProfileResponse mockUser = UserProfileResponse.builder()
                .email("user@tienda.com")
                .name("user")
                .build();

                AuthResponse mockResponse = AuthResponse.builder()
                .token("mi.token.falso")
                .refreshToken("mi.refresh.token")
                .expiresIn(3600L)
                .user(mockUser) 
                .build();
        
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mi.token.falso"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.user.email").value("user@tienda.com"));
    }

    // Probamos el login con datos invalidos
    @Test
    public void testloginFail() throws Exception {
        
        LoginRequest request = new LoginRequest("user@tienda.com", "clave_equivocada_123");

       
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                
 
                .andExpect(status().isBadRequest());
                
 
    }


    // Probamos el registro con datos validos
    @Test
    void testRegister() throws Exception {
       
        RegisterRequest request = new RegisterRequest("nuevoAdmin", "secreta123", "Juan Perez", "juan@tienda.com");
        
   
        UserProfileResponse mockUser = UserProfileResponse.builder()
                .email("juan@tienda.com")
                .name("Juan Perez")
                .username("nuevoAdmin")
                .build();

       
        AuthResponse mockResponse = AuthResponse.builder()
                .token("token.de.registro")
                .refreshToken("refresh.de.registro")
                .expiresIn(3600L)
                .user(mockUser)
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);

      
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                
                .andDo(print()) 
                
                
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.token").value("token.de.registro"))
                .andExpect(jsonPath("$.user.username").value("nuevoAdmin"));
    }

    // Probamos el registro con datos invalidos
    @Test
    void testRegisterFail() throws Exception {
        
        RegisterRequest request = new RegisterRequest("nuevoAdmin", "123", "Juan Perez", "juan@tienda.com");

        
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                
                .andDo(print())
                
                
                .andExpect(status().isBadRequest());
    }

 
}