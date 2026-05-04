package co.empresa.proyecto_desarrollo3.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (realmAccess != null && realmAccess.get("roles") != null) {

            List<String> roles = (List<String>) realmAccess.get("roles");

            for (String role : roles) {
                authorities.add(
                    new SimpleGrantedAuthority(role.toUpperCase())
                );
            }
        }

        return new JwtAuthenticationToken(jwt, authorities);
    }
}