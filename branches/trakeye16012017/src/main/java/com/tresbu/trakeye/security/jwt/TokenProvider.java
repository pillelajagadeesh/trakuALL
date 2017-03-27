package com.tresbu.trakeye.security.jwt;

import com.tresbu.trakeye.config.TrakEyeProperties;
import com.tresbu.trakeye.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;

    private long tokenValidityInSeconds;

    private long tokenValidityInSecondsForRememberMe;

    @Inject
    private TrakEyeProperties jHipsterProperties;

    
    @Inject
    private UserRepository userRepository;
    
    @PostConstruct
    public void init() {
        this.secretKey =
            jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();

        this.tokenValidityInSeconds =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInSecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(Authentication authentication, Boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInSecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInSeconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) throws ExpiredJwtException {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(",")).stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
        log.debug("Retriving user {} from db to verify status",claims.getSubject());
        Optional<com.tresbu.trakeye.domain.User> userFromDatabase = userRepository.findOneByLogin(claims.getSubject());
        User principal=null;
        if(userFromDatabase.isPresent()){
        	com.tresbu.trakeye.domain.User user = userFromDatabase.get();
        	principal = new TrakEyeUser(claims.getSubject(), "", authorities, user.getTenant());
        }else{
               principal = new User(claims.getSubject(), "",
               authorities);
        }
        
        
       if(userFromDatabase.isPresent() && userFromDatabase.get().getActivated()){
        	return new UsernamePasswordAuthenticationToken(principal, "", authorities);
         }
        throw new ExpiredJwtException(null,claims,"User deactivated");
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }
}
