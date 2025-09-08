package co.com.crediya.security.helper;

import co.com.crediya.security.enums.SecurityConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String role = jwt.getClaimAsString(SecurityConstants.ROLE_CLAIM.getValue());
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(SecurityConstants.PREFIX_ROLE_AUTH.getValue() + role.toUpperCase()));
    }
}
