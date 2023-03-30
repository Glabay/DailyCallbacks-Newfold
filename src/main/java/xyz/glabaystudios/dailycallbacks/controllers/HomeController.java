package xyz.glabaystudios.dailycallbacks.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.glabaystudios.dailycallbacks.services.AgentService;
import xyz.glabaystudios.dailycallbacks.services.CallbackService;

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
}
