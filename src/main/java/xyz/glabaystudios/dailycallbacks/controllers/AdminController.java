package xyz.glabaystudios.dailycallbacks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.dailycallbacks.data.model.Agent;
import xyz.glabaystudios.dailycallbacks.data.model.BulkObject;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.services.AgentService;
import xyz.glabaystudios.dailycallbacks.services.CallbackService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value="/admin")
public class AdminController {

	private final CallbackService callbackService;

	private final AgentService agentService;

	@Autowired
	public AdminController(CallbackService callbackService, AgentService agentService) {
		this.callbackService = callbackService;
		this.agentService = agentService;
	}


	@GetMapping("")
	public String getAdminDashboard(Model model, Callback callback) {
		List<Callback> callbacks = callbackService.findAll();
		List<Agent> agents = agentService.findAll();
		// quick check if we're not null and not empty
		if (!Objects.isNull(callbacks) && !callbacks.isEmpty()) {
			model.addAttribute("callbacks", callbacks);
			model.addAttribute("callback", new Callback());
			model.addAttribute("bulkObject", new BulkObject());
			model.addAttribute("agents", agents);
			model.addAttribute("newAgent", new Agent());
			model.addAttribute("module", "callbacks");
		}
		return "admin_cp";
	}

	@PostMapping(value="/add/singleton")
	public String addSingleCallback(Model model, @ModelAttribute("callback") Callback callback) {
		System.out.println("Submitted: " + callback);
		if (!Objects.isNull(callback) && !missingData(callback)) {
			callbackService.save(callback);
			return "redirect:/admin";
		}
		return "redirect:/error";
	}

	@PostMapping(value="/add/agent")
	public String addNewAgent(Model model, @ModelAttribute("newAgent") Agent newAgent) {
		System.out.println("Submitted: " + newAgent);
		if (!Objects.isNull(newAgent)) {
			agentService.save(newAgent);
			return "redirect:/admin";
		}
		return "redirect:/error";
	}

	@PostMapping(value="/add/bulk")
	public String addBulkCallbacks(Model model, @ModelAttribute("bulkObject") BulkObject bulkObject) {
		List<String> callbackObjects = List.of(bulkObject.getDataToParse().split("\n"));
		List<Callback> callbackList = new ArrayList<>();

		for (String stringData : callbackObjects) {
			if (stringData.length() < 5) {
				System.out.println("Oops.. Something went wrong here...");
				System.out.println(stringData);
				continue;
			}
			// break it down
			String[] data = stringData.split("\t");
			// parse it
			String cbDate = data[0];
			String cbTime = data[1];
			String cbCase = data[2];
			String cbOwner = data[3];
			String cbPegaNote = data[4];

			Callback temp = new Callback();
			temp.setDateOfCallback(cbDate);
			temp.setTime(cbTime);
			temp.setCallbackCase(cbCase);
			temp.setAgent(cbOwner);
			temp.setNotes(cbPegaNote);

			callbackList.add(temp);
		}

		if (!callbackList.isEmpty()) {
			callbackList.forEach(callbackService::save);
			return "redirect:/admin";
		}

		return "redirect:/error";
	}


	private boolean missingData(Callback callback) {
		if (callback.getCallbackCase().isBlank()) return true;
		if (callback.getDateOfCallback().isBlank()) return true;
		if (callback.getNotes().isBlank()) return true;
		if (callback.getAgent().isBlank()) return true;
		return callback.getTime().isBlank();
	}
}
