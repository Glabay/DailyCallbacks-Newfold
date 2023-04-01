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
@RequestMapping("/lilblu")
public class LilBluController {

    private final CallbackService callbackService;
    private final AgentService agentService;

    @GetMapping
    public String getHome(Model model){

        return "sidebar";
    }
}
