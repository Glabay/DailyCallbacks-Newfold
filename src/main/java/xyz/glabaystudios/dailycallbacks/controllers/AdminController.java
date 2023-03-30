package xyz.glabaystudios.dailycallbacks.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.dailycallbacks.data.model.Agent;
import xyz.glabaystudios.dailycallbacks.data.model.Callback;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.CallbackFilter;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.CallbackType;
import xyz.glabaystudios.dailycallbacks.data.model.uncached.YearToDateStats;
import xyz.glabaystudios.dailycallbacks.services.AdminService;
import xyz.glabaystudios.dailycallbacks.services.AgentService;
import xyz.glabaystudios.dailycallbacks.services.CallbackService;

import java.util.*;

@AllArgsConstructor
@Controller
@RequestMapping(value="/admin")
public class AdminController {

	private final CallbackService callbackService;
	private final AgentService agentService;
	private final AdminService adminService;

	@GetMapping("")
	public String getAdminDashboard(Model model) {
		List<String> callbackTypes = new ArrayList<>();
		Arrays.stream(CallbackType.values()).toList().forEach(callbackType -> callbackTypes.add(callbackType.getCallbackTypeName()));
		// The empty templates for Input
		model.addAttribute("callback", new Callback());
		model.addAttribute("callbackTypes", callbackTypes);
		model.addAttribute("newAgent", new Agent());
		model.addAttribute("adminCallbackFilter", new CallbackFilter());
		model.addAttribute("selectedMonth", new ArrayList<>());

		// fetch a list of agents
		List<Agent> agents = agentService.findAll();
		// check their not null, or empty; before adding them to the model
		if (Objects.nonNull(agents) && !agents.isEmpty())
			model.addAttribute("agents", agents);
		// fetch a list of callbacks
		List<Callback> callbacks = callbackService.findAll();
		// Check they're not null or empty; before adding them to the model
		if (Objects.nonNull(callbacks) && !callbacks.isEmpty())
			model.addAttribute("callbacks", callbacks);
		//TODO: get the current year and pass it
		YearToDateStats YTD = callbackService.fetchCallbackStatsForYearToDate(2023);
		if (Objects.nonNull(YTD))
			model.addAttribute("ytdStats", YTD);

		// return the template with the applicable modals
		return "admin_cp";
	}

	@PostMapping(value="/add/singleton")
	public String addSingleCallback(Model model, @ModelAttribute("callback") Callback callback) {
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


	@GetMapping(value="/filters/apply")
	public String applyFilter(Model model, @RequestParam Map<String, String> params) {
		System.out.println("Submitting Params: " + params.toString());
		List<String> callbackTypes = new ArrayList<>();
		Arrays.stream(CallbackType.values()).toList().forEach(callbackType -> callbackTypes.add(callbackType.getCallbackTypeName()));
		CallbackFilter filter = new CallbackFilter();
		if (params.isEmpty()) {
			// redirect to the error page with a note the filter was empty
			return "redirect:/error";
		}
		// Filter the params
		for (String filterOption : params.keySet()) {
			if (Objects.equals("specificDate", filterOption)) filter.setSpecificDate(params.get(filterOption));
			if (Objects.equals("filterStartDay", filterOption)) filter.setFilterStartDay(params.get(filterOption));
			if (Objects.equals("filterEndDay", filterOption)) filter.setFilterEndDay(params.get(filterOption));
			if (Objects.equals("filterByAgent", filterOption)) filter.setFilterByAgent(params.get(filterOption));
			if (Objects.equals("filterByStatus", filterOption)) filter.setFilterByStatus(params.get(filterOption));
		}

		model.addAttribute("callbackTypes", callbackTypes);
		model.addAttribute("newAgent", new Agent());
		model.addAttribute("adminCallbackFilter", new CallbackFilter());
		model.addAttribute("selectedMonth", new ArrayList<>());

		// fetch a list of agents
		List<Agent> agents = agentService.findAll();
		// check their not null, or empty; before adding them to the model
		if (Objects.nonNull(agents) && !agents.isEmpty())
			model.addAttribute("agents", agents);
		// fetch a list of callbacks
		List<Callback> callbacks = callbackService.findAll();
		// Check they're not null or empty; before adding them to the model
		if (Objects.nonNull(callbacks) && !callbacks.isEmpty())
			model.addAttribute("callbacks", adminService.getFilteredCallbacks(filter, callbacks));
		//TODO: get the current year and pass it
		YearToDateStats YTD = callbackService.fetchCallbackStatsForYearToDate(2023);
		if (Objects.nonNull(YTD))
			model.addAttribute("ytdStats", YTD);

		return "admin_cp";
	}

	private boolean missingData(Callback callback) {
		if (callback.getCallbackCase().isBlank()) return true;
		if (callback.getDateOfCallback().isBlank()) return true;
		if (callback.getNotes().isBlank()) return true;
		if (callback.getAgent().isBlank()) return true;
		return callback.getTime().isBlank();
	}
}
