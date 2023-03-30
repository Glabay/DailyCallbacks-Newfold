package xyz.glabaystudios.dailycallbacks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.CallbackType;
import xyz.glabaystudios.dailycallbacks.services.CallbackService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value="/callbacks")
public class CallbacksController {

	private final CallbackService callbackService;

	@Autowired
	public CallbacksController(CallbackService callbackService) {
		this.callbackService = callbackService;
	}

	@GetMapping
	public String getDailyCallbacks(Model model) {
		List<CallbackType> types = Arrays.stream(CallbackType.values()).toList();
		// fetch the callback list
		//TODO: Find all callbacks for today's date
		List<Callback> callbacks = callbackService.findAll();
		// a blank instance of the Object so the Thymeleaf template has something to model the input to when entering a new callback
		Callback newCallback = new Callback();
		// add in default variables
		// TODO: Check the security context for the user, and get the agent's name to autofill
		model.addAttribute("callback", newCallback);
		// add in the available callback types
		model.addAttribute("callbackTypes", types);
		// quick check if we're not null and not empty
		if (!Objects.isNull(callbacks) && !callbacks.isEmpty()) {
			// TODO: Sort by time;
			model.addAttribute("callbacks", callbacks); // The callbacks
			model.addAttribute("module", "callbacks");
		}
		// else there was thing that went wrong.
		return "callbacks";
	}

	@GetMapping(path="/{id}")
	public String getCallback(@PathVariable("id") Long callbackId) {
		Callback parentCallback = callbackService.findCallbackById(callbackId);
		if (!Objects.isNull(parentCallback)) return "redirect:/callback_details";
		return "redirect:/error";
	}

	@PostMapping(value="/new")
	public String addSingleCallback(Model model, @ModelAttribute("callback") Callback callback) {
		if (!Objects.isNull(callback)) {
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
