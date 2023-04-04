package xyz.glabaystudios.wbc.tracker.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.AgentDetails;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Agent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long uid;

	private String agentEmail;
	private String agentUsername;
	private String agentPin;
	private Long agentAccess;

	public String getPrimaryRole() {
		return AgentDetails.AccessLevel.values()[Math.toIntExact(agentAccess)].name();
	}

	public String getAgentName() {
		return getAgentFirstName().concat(" ").concat(getAgentLastName());
	}

	public String getAgentFirstName() {
		return getStrNoEmail().split("\\.")[0];
	}

	public String getAgentLastName() {
		return getStrNoEmail().split("\\.")[1];
	}

	private String getStrNoEmail() {
		return getAgentEmail().split("@")[0];
	}

}
