package xyz.glabaystudios.wbc.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;
import xyz.glabaystudios.wbc.tracker.data.model.Callback;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.CallbackFilter;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.CallbackType;
import xyz.glabaystudios.wbc.tracker.data.model.uncached.YearToDateStats;
import xyz.glabaystudios.wbc.tracker.services.AdminService;
import xyz.glabaystudios.wbc.tracker.services.AgentService;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;

import java.security.Principal;
import java.util.*;

@AllArgsConstructor
@Controller
@RequestMapping(value="/admin")
public class AdminController {

	private final CallbackService callbackService;
	private final AgentService agentService;
	private final AdminService adminService;

	@GetMapping("")
	public String getAdminLandingPage(Principal principal) {
//		if (Objects.isNull(principal)) {
//			// There is no logged in user
//			return "redirect:/login";
//		}
		return "admin_cp";
	}

	@GetMapping("/details/agents")
	public String getAgentDetailsView(Model model) {
		model.addAttribute("newAgent", new Agent());
		// fetch a list of agents
		List<Agent> agents = agentService.findAll();
		// check their not null, or empty; before adding them to the model
		if (Objects.nonNull(agents) && !agents.isEmpty())
			model.addAttribute("agents", agents);

		model.addAttribute("adminView", "details_agents");
		return "admin_cp";
	}

	@GetMapping("/details/stats")
	public String getStatsDetailsView(Model model) {
		//TODO: get the current year and pass it
		YearToDateStats YTD = callbackService.fetchCallbackStatsForYearToDate(2023);
		if (Objects.nonNull(YTD))
			model.addAttribute("ytdStats", YTD);

		model.addAttribute("adminView", "details_stats");
		return "admin_cp";
	}

	@GetMapping("/details/callbacks")
	public String getCallbacksDetailsView(Model model) {
		List<String> callbackTypes = new ArrayList<>();
		Arrays.stream(CallbackType.values()).toList().forEach(callbackType -> callbackTypes.add(callbackType.getCallbackTypeName()));
		// The empty templates for Input
		model.addAttribute("callbackTypes", callbackTypes);
		model.addAttribute("adminCallbackFilter", new CallbackFilter());
		model.addAttribute("selectedMonth", new ArrayList<>());

		// fetch a list of callbacks
		List<Callback> callbacks = callbackService.findAll();
		// Check they're not null or empty; before adding them to the model
		if (Objects.nonNull(callbacks) && !callbacks.isEmpty())
			model.addAttribute("callbacks", callbacks);

		model.addAttribute("adminView", "details_callbacks");
		return "admin_cp";
	}

	@GetMapping(path="/details/callbacks/edit/{id}")
	public String getCallback(Model model, @PathVariable("id") Long callbackId) {
		List<CallbackType> types = Arrays.stream(CallbackType.values()).toList();
		// add in the available callback types
		model.addAttribute("callbackTypes", types);
		// fetch a list of agents
		List<Agent> agents = agentService.findAll();
		// check their not null, or empty; before adding them to the model
		if (Objects.nonNull(agents) && !agents.isEmpty())
			model.addAttribute("agents", agents);

		Callback parentCallback = callbackService.findCallbackById(callbackId);
		if (Objects.nonNull(parentCallback)) {
			model.addAttribute("cbDataObject", parentCallback);
			model.addAttribute("adminView", "details_callbacks_edit");
			return "admin_cp";
		}
		return "redirect:/error";
	}

	@PostMapping(value="/add/singleton")
	public String addSingleCallback(Model model, @ModelAttribute("cbDataObject") Callback callback) {
		if (Objects.nonNull(callback)) {
			callbackService.save(callback);
			return "redirect:/admin/details/callbacks";
		}
		return "redirect:/error";
	}

	@PostMapping(value="/add/agent")
	public String addNewAgent(@ModelAttribute("newAgent") Agent newAgent) {
		if (!Objects.isNull(newAgent)) {
			Agent agent = new Agent();
			agent.setAgentAccess(newAgent.getAgentAccess());
			agent.setAgentPin(newAgent.getAgentPin());
			agent.setAgentEmail(newAgent.getAgentEmail());
			agent.setAgentUsername(newAgent.getAgentUsername());
			agentService.save(agent);
			return "redirect:/admin/details/agents";
		}
		return "redirect:/error";
	}

	@GetMapping(value="/filters/apply")
	public String applyFilter(Model model, @RequestParam Map<String, String> params) {
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
		model.addAttribute("adminCallbackFilter", new CallbackFilter());
		model.addAttribute("selectedMonth", new ArrayList<>());

		// fetch a list of callbacks
		List<Callback> callbacks = callbackService.findAll();
		// Check they're not null or empty; before adding them to the model
		if (Objects.nonNull(callbacks) && !callbacks.isEmpty())
			model.addAttribute("callbacks", adminService.getFilteredCallbacks(filter, callbacks));

		model.addAttribute("adminView", "details_callbacks");
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
