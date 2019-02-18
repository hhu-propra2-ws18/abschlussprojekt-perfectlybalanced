package de.hhu.abschlussprojektverleihplattform.security;

import de.hhu.abschlussprojektverleihplattform.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthenticatedUser extends UserEntity implements UserDetails {
    protected AuthenticatedUser(UserEntity user) {
        super(user.getFirstname(), user.getLastname(), user.getUsername(), user.getPassword(),user.getEmail(), user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRole().toString());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
