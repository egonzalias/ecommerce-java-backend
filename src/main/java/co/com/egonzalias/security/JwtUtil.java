package co.com.egonzalias.security;

import co.com.egonzalias.dto.JwtUserInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String jwtSecret = "MySecretJWTKey123456testing12346568324fgdfgdfgdfgdfgdfgd";
    private final long jwtExpirationMs = 3600000;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateJwt(JwtUserInfo user){

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRoleName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public JwtUserInfo getUsernameFromJwt(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new JwtUserInfo(
                claims.getSubject(),
                claims.get("role", String.class)
        );
    }

    public boolean validateJwt(String authToken){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e){
            return false;
        }
    }
}







