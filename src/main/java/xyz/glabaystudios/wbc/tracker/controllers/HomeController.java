package xyz.glabaystudios.wbc.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.glabaystudios.wbc.tracker.services.AgentService;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class HomeController {

    private final CallbackService callbackService;
    private final AgentService agentService;

    @GetMapping({"", "/index", "/home"})
    public String getHome(Model model){
        int totalOpenCallbacks = callbackService.findAllOpenCallbacks().size();
        int totalAgents = agentService.findAll().size();

        model.addAttribute("totalCallbacks", totalOpenCallbacks);
        model.addAttribute("totalAgents", totalAgents);

        return "index";
    }

    @GetMapping("/login")
    public String getLogin(Model model){
        return "login";
    }
}
