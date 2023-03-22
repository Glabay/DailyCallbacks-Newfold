package xyz.glabaystudios.dailycallbacks.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.glabaystudios.dailycallbacks.data.model.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {}