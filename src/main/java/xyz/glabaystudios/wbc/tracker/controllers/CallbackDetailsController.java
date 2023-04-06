package xyz.glabaystudios.wbc.tracker.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.wbc.tracker.data.model.Callback;
import xyz.glabaystudios.wbc.tracker.data.model.CallbackDetails;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
		List<CallbackDetails> details = new ArrayList<>();
		if (!Objects.isNull(newCallbackDetails) && !missingData(newCallbackDetails)) {
			newCallbackDetails.setCallbackParentId(callbackId);
			details.add(newCallbackDetails);
			callbackService.save(details);
			return "redirect:/callback_details/" + newCallbackDetails.getCallbackParentId();
		}
		return "redirect:/error";
	}

	@PostMapping(value="/complete")
	public String completeCallbackDetails(HttpServletRequest request, @RequestParam("id") Long callbackId) {
		if (Objects.isNull(callbackId))
			return "redirect:/error";
		if (Objects.isNull(request.getRemoteUser()))
			return "redirect:/login";
		Callback callback = callbackService.findCallbackById(callbackId);
		if (Objects.nonNull(callback)) {
			Date todaysDate = Date.from(Instant.now());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
			String todaysDateFormatted = dateFormat.format(todaysDate);

			callback.setCompleted(true);
			callback.setDateCompleted(todaysDateFormatted);
			callback.setCompletedBy(request.getRemoteUser());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			callback.setTimeCompleted(dtf.format(now));
			callbackService.save(callback);
			return "redirect:/callbacks";
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
