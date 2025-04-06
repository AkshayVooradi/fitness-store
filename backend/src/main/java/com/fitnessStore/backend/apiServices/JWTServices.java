package com.fitnessStore.backend.apiServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessStore.backend.ExceptionHandling.IncorrectToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTServices {

    private String secretKey = "";

    public JWTServices(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String email) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 10))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims =  extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (ExpiredJwtException e){
            System.out.println("JWT token is expired");
            throw new IncorrectToken("JWT token is expired");
        }
        catch (MalformedJwtException e){
            System.out.println("JWT token is malformed");
            throw new IncorrectToken("JWT token is malformed");
        }
        catch (io.jsonwebtoken.security.SignatureException e){
            System.out.println("Invalid JWT signature");
            throw new IncorrectToken("Invalid JWT signature");
        }
        catch (UnsupportedJwtException e){
            System.out.println("Unsupported JWT token");
            throw new IncorrectToken("Unsupported JWT token");
        }
        catch (IllegalArgumentException e){
            System.out.println("JWT token is empty");
            throw new IncorrectToken("JWT token is empty");
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
       final String email = extractEmail(token);
       return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String getSecretKey() {
        return secretKey;
    }
}
