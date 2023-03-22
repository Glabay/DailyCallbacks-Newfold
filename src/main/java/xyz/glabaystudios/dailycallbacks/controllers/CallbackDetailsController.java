package xyz.glabaystudios.dailycallbacks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.CallbackDetails;
import xyz.glabaystudios.dailycallbacks.services.CallbackService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value="/callback_details")
public class CallbackDetailsController {

	private final CallbackService callbackService;

	@Autowired
	public CallbackDetailsController(CallbackService callbackService) {
		this.callbackService = callbackService;
	}

	@GetMapping(value="/{id}")
	public String getCallback(@PathVariable("id") Long callbackId, Model model) {
		Callback parentCallback = callbackService.findCallbackById(callbackId);
		// quick check if we're not null and not empty
		if (!Objects.isNull(parentCallback)) {
			model.addAttribute("callback", parentCallback);
			model.addAttribute("newCallbackDetails", new CallbackDetails());
			model.addAttribute("detailIndex", parentCallback.getUid());
			model.addAttribute("module", "callbacks");
			return "callback_details";
		}
		return "redirect:/error";
	}

	@PostMapping(value="/addNote")
	public String addSingleCallback(Model model, @ModelAttribute("newCallbackDetails") CallbackDetails newCallbackDetails, @RequestParam("id") Long callbackId) {
		if (Objects.isNull(callbackId)) return "redirect:/error";
		System.out.println("Submitted: " + newCallbackDetails);
		List<CallbackDetails> details = new ArrayList<>();
		if (!Objects.isNull(newCallbackDetails) && !missingData(newCallbackDetails)) {
			newCallbackDetails.setCallbackParentId(callbackId);
			details.add(newCallbackDetails);
			callbackService.save(details);
			return "redirect:/callback_details/" + newCallbackDetails.getCallbackParentId();
		}
		return "redirect:/error";
	}

	private boolean missingData(CallbackDetails data) {
		if (Objects.isNull(data)) return true;
		if (data.getCbDate().isBlank()) return true;
		if (data.getCbTime().isBlank()) return true;
		if (data.getCbDay().isBlank()) return true;
		if (data.getCbAttempt().isBlank()) return true;
		return (data.getCbNote().isBlank());
	}
}
