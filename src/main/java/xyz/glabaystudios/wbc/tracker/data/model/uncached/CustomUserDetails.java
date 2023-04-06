package xyz.glabaystudios.wbc.tracker.data.model.uncached;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Agent user;

    public CustomUserDetails(Agent user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // The empty list to stored granted access
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // The roles
        SimpleGrantedAuthority roleWbc = new SimpleGrantedAuthority("ROLE_WBC");
        SimpleGrantedAuthority roleProAm = new SimpleGrantedAuthority("ROLE_PRO_AM");
        SimpleGrantedAuthority roleSupervisor = new SimpleGrantedAuthority("ROLE_SUPERVISOR");
        SimpleGrantedAuthority roleDeveloper = new SimpleGrantedAuthority("ROLE_DEVELOPER");

        // Checking and assigning
        if (user.getAgentAccess() >= 0) authorities.add(roleWbc);
        if (user.getAgentAccess() >= 1) authorities.add(roleProAm);
        if (user.getAgentAccess() >= 2) authorities.add(roleSupervisor);
        if (user.getAgentAccess() >= 3) authorities.add(roleDeveloper);

        // return the list of access
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getAgentPin();
    }

    @Override
    public String getUsername() {
        return user.getAgentUsername();
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
