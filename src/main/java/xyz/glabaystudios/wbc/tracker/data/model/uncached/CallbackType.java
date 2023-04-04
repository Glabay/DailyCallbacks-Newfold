package xyz.glabaystudios.wbc.tracker.data.model.uncached;

import lombok.Getter;

public enum CallbackType {

	// Normal WBC_AGENT Callback Types
	SCHEDULED_IN("Scheduled IN"),
	UNSCHEDULED_IN("Unscheduled IN"),
	SCHEDULED_WC("Scheduled WC"),
	UNSCHEDULED_WC("Unscheduled WC"),

	// PRO_AM Callback Types
	PRO_AM_WC("Pro-AM-WC"),
	PRO_AM_PROGRESS("Pro-AM-Progress"),
	PRO_AM_MONTHLY("Pro-AM-Monthly"),
	PRO_AM_INTRO("Pro-AM-Intro"),

	// All 'Other' types
	OTHER("Other")

	;

	@Getter
	private final String callbackTypeName;
	CallbackType(String callbackTypeName) {
		this.callbackTypeName = callbackTypeName;
	}
}
