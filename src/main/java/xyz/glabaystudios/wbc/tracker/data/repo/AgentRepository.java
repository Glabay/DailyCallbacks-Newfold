package xyz.glabaystudios.wbc.tracker.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByAgentUsernameIgnoreCase(String agentUsername);

}