package xyz.glabaystudios.dailycallbacks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.dailycallbacks.data.model.Agent;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.CallbackDetails;
import xyz.glabaystudios.dailycallbacks.data.repo.AgentRepository;
import xyz.glabaystudios.dailycallbacks.data.repo.CallbackDetailsRepository;
import xyz.glabaystudios.dailycallbacks.data.repo.CallbackRepository;

import java.util.List;
import java.util.Objects;

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

}
