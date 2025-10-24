package co.com.egonzalias.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtUserInfo {
    private String username;
    private String roleName;
}
