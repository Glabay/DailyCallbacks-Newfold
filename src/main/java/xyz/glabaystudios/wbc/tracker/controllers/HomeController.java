package xyz.glabaystudios.wbc.tracker.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.glabaystudios.utils.Logger;
import xyz.glabaystudios.wbc.tracker.data.model.Agent;
import xyz.glabaystudios.wbc.tracker.services.AgentService;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;

import java.util.Objects;

@AllArgsConstructor
@Controller
@RequestMapping("/")
public class HomeController {

    private final CallbackService callbackService;
    private final AgentService agentService;

    @GetMapping({"", "/index", "/home"})
    public String getHome(HttpServletRequest request, Model model){
        Logger.getLogger().printStringMessageFormatted("Remote connection from user: %s (%s)", request.getRemoteUser(), request.getRemoteAddr());

        Agent agent = agentService.findAgentByUsername(request.getRemoteUser());
        if (Objects.nonNull(agent))
            model.addAttribute("loggedInAgent", agent);
        return "index";
    }


}
