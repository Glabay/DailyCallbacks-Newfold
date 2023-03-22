package xyz.glabaystudios.dailycallbacks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.CallbackDetails;
import xyz.glabaystudios.dailycallbacks.services.CallbackService;

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
		// fetch the callback list
		List<Callback> callbacks = callbackService.findAll();
		// quick check if we're not null and not empty
		if (!Objects.isNull(callbacks) && !callbacks.isEmpty()) {
			//we have content, sort them
			// TODO; Sort by time
			model.addAttribute("callbacks", callbacks);
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

	@GetMapping(path="/all")
	public ResponseEntity<List<Callback>> getCallback() {
		List<Callback> callbacks = callbackService.findAll();
		if (!Objects.isNull(callbacks) && !callbacks.isEmpty()) {
			return new ResponseEntity<>(callbacks, HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
