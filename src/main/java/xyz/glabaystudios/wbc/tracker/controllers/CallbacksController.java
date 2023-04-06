package xyz.glabaystudios.wbc.tracker.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.utils.Logger;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;
import xyz.glabaystudios.wbc.tracker.data.model.Callback;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.CallbackType;
import xyz.glabaystudios.wbc.tracker.services.AgentService;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping(value="/callbacks")
public class CallbacksController {

	private final CallbackService callbackService;
	private final AgentService agentService;

	@GetMapping
	public String getDailyCallbacks(HttpServletRequest request, Model model) {
		Logger.getLogger().printStringMessageFormatted("Remote connection from user: %s (%s)", request.getRemoteUser(), request.getRemoteAddr());
		// Check if we have a logged-in user
		if (Objects.isNull(request.getRemoteUser()))
			return "redirect:/login";

		List<CallbackType> types = Arrays.stream(CallbackType.values()).toList();
		// add in the available callback types
		model.addAttribute("callbackTypes", types);

		// parse todays date
		Date todaysDate = Date.from(Instant.now());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		String todaysDateFormatted = dateFormat.format(todaysDate);
		// fetch the callback list for today
		List<Callback> callbacks = callbackService.findOpenCallbacksUpUntilProvidedDate(todaysDateFormatted);
		// quick check if we're not null and not empty
		if (!Objects.isNull(callbacks) && !callbacks.isEmpty()) {
			// TODO: Fetch all Callback Details, with
			// TODO: Sort by time;
			model.addAttribute("callbacks", callbacks); // The callbacks
		}

		// a blank instance of the Object so the Thymeleaf template has something to model the input to when entering a new callback
		Callback newCallback = new Callback();
		// add in default variables
		newCallback.setAgent(request.getRemoteUser());
		// add the object to the model
		model.addAttribute("cbDataObject", newCallback);

		// fetch a list of agents
		List<Agent> agents = agentService.findAll();
		// check their not null, or empty; before adding them to the model
		if (Objects.nonNull(agents) && !agents.isEmpty())
			model.addAttribute("agents", agents);

		// else there was thing that went wrong.
		return "callbacks";
	}

	@GetMapping(path="/{id}")
	public String getCallback(@PathVariable("id") Long callbackId) {
		Callback parentCallback = callbackService.findCallbackById(callbackId);
		if (!Objects.isNull(parentCallback)) return "redirect:/callback_details";
		return "redirect:/error";
	}

	@GetMapping(path="/claim/{id}/{agentName}")
	public String claimCallback(@PathVariable("id") Long callbackId, @PathVariable("agentName") String agentClaiming) {
		Callback parentCallback = callbackService.findCallbackById(callbackId);
		if (Objects.nonNull(parentCallback)) {
			// replace the Ownership of this case, and redirect back to the callbacks page
			parentCallback.setAssigned(agentClaiming);
			parentCallback.setVolunteered(agentClaiming);
			callbackService.save(parentCallback);
			return "redirect:/callbacks";
		}
		return "redirect:/error";
	}

	@PostMapping(value="/new")
	public String addSingleCallback(@ModelAttribute("callback") Callback callback) {
		if (!Objects.isNull(callback)) {
			// check and format the time for display
			String[] timeData = callback.getTime().split(":");
			int hours = Integer.parseInt(timeData[0]);
			boolean meridiem = hours >= 12;
			String callbackTime = "%d:%s %s".formatted(((hours > 12) ? (hours - 12) : hours), timeData[1], (meridiem ? "PM" : "AM"));
			callback.setTime(callbackTime);
			// make the assigned the owner
			callback.setAssigned(callback.getAgent());
			callbackService.save(callback);
			return "redirect:/callbacks";
		}
		return "redirect:/error";
	}


	@GetMapping(path="/all")
	public ResponseEntity<List<Callback>> getCallback() {
		List<Callback> callbacks = callbackService.findAll();
		if (!Objects.isNull(callbacks) && !callbacks.isEmpty()) {
			return new ResponseEntity<>(callbacks, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
