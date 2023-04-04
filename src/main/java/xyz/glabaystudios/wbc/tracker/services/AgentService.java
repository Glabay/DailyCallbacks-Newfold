package xyz.glabaystudios.wbc.tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;
import xyz.glabaystudios.wbc.tracker.data.repo.AgentRepository;

import java.util.List;

@Service
public class AgentService {

	private final AgentRepository agentRepository;


	@Autowired
	public AgentService(AgentRepository agentRepository) {
		this.agentRepository = agentRepository;
	}

	public List<Agent> findAll() {
		return agentRepository.findAll();
	}

	public void save(Agent agent) {
		agentRepository.save(agent);
	}

	public Agent findAgentByUsername(String username) {
		return agentRepository.findByAgentUsernameIgnoreCase(username)
				.orElse(null);
	}
}
