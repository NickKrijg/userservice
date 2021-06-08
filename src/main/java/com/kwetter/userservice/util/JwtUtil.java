package com.kwetter.userservice.util;

import com.kwetter.userservice.entity.Role;
import com.kwetter.userservice.entity.User;
import com.kwetter.userservice.models.UserDetailsImpl;
import com.kwetter.userservice.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final static String ROLES = "Roles";

    @Autowired
    private UserService userService;

    @Value("${auth.secret}")
    String SECRET_KEY = "changekeyandlocation";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token){
        return Long.parseLong(extractClaim(token, Claims::getId));
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public ArrayList<String> extractRoles(String token) {
        return (ArrayList<String>) extractClaim(token, claims -> claims.get(ROLES));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> userRoles = new HashSet<>();
        User user = userService.findUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userDetails.getUsername()));

        for(Role role:user.getRoles()){
            userRoles.add(role.getRole());
        }
        claims.put(ROLES, userRoles);

        return createToken(claims, userDetails.getUsername(), userDetails.getId());
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        Set<String> userRoles = new HashSet<>();

        for(GrantedAuthority authority:userPrincipal.getAuthorities()){
            userRoles.add(authority.getAuthority());
        }
        claims.put(ROLES, userRoles);

        return createToken(claims, userPrincipal.getUsername(), userPrincipal.getId());
    }

    private String createToken(Map<String, Object> claims, String userName, Long id) {
        return Jwts.builder().setClaims(claims).setSubject(userName).setId(id.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}
