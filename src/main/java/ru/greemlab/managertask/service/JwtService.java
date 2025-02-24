package ru.greemlab.managertask.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.greemlab.managertask.domain.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Сервис для работы с JWT токенами.
 * <p>
 * Этот сервис предоставляет методы для генерации токенов, извлечения информации из токенов
 * и проверки их валидности. Он используется для работы с авторизацией пользователей.
 */
@Service
public class JwtService {

    @Value("${app.jwt.token}")
    private String jwtSigningKey;

    /**
     * Извлекает email из JWT токена.
     *
     * @param token JWT токен.
     * @return email, извлеченный из токена.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерирует JWT токен для пользователя, используя его детали.
     *
     * @param userDetails Данные пользователя.
     * @return Сгенерированный JWT токен.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Проверяет, является ли указанный токен валидным для данного пользователя.
     *
     * @param token        JWT токен.
     * @param userDetails  Данные пользователя.
     * @return true, если токен валиден, иначе false.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String email = extractEmail(token);
            return (email.equals(((User) userDetails).getEmail()) && !isTokenExpired(token));
        } catch (Exception e) {
            throw new IllegalStateException("Invalid token", e);
        }
    }

    /**
     * Извлекает определенную информацию (клейм) из токена.
     *
     * @param token             JWT токен.
     * @param claimsResolvers   Функция для извлечения клейма.
     * @param <T>               Тип извлекаемого значения.
     * @return Извлеченное значение клейма.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерирует JWT токен, добавляя дополнительные данные и используя детали пользователя.
     *
     * @param extraClaims      Дополнительные данные для токена.
     * @param userDetails      Данные пользователя.
     * @return Сгенерированный JWT токен.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(((User) userDetails).getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @param token JWT токен.
     * @return true, если токен истек, иначе false.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия из токена.
     *
     * @param token JWT токен.
     * @return Дата истечения срока действия токена.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все клеймы (информацию) из токена.
     *
     * @param token JWT токен.
     * @return Все клеймы из токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Возвращает ключ для подписи токена.
     *
     * @return Ключ для подписи.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
