package xyz.glabaystudios.wbc.tracker.services.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.CustomUserDetails;
import xyz.glabaystudios.wbc.tracker.data.repo.AgentRepository;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Agent> agent = agentRepository.findByAgentUsernameIgnoreCase(username);
        return agent.map(CustomUserDetails::new).orElse(null);
    }
}
