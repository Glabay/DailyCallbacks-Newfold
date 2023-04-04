package xyz.glabaystudios.wbc.tracker.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.glabaystudios.wbc.tracker.services.AgentService;
import xyz.glabaystudios.wbc.tracker.services.CallbackService;
import xyz.glabaystudios.wbc.tracker.services.lilblu.Whois;
import xyz.glabaystudios.wbc.tracker.services.lilblu.WhoisLookup;

import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/lilblu")
public class LilBluController {

    private final CallbackService callbackService;
    private final AgentService agentService;

    @GetMapping
    public String getHome(Model model) {
        return "lilblu";
    }

    @GetMapping("/toolbox/whois")
    public String getToolboxWhois(Model model) {
        model.addAttribute("tool", "domain_lookup");
        return "lilblu";
    }

    @GetMapping("/toolbox/whois/search")
    public String getToolboxWhoisResult(Model model, @RequestParam Map<String, String> params) {
        System.out.println("Parameters Passed: ".concat(params.get("domain")));
        model.addAttribute("tool", "domain_lookup");
        String domainToSearch = params.get("domain");
        String sanitizedDomain = domainToSearch
                // clean off the opening http element
                .replace("https://", "")
                .replace("http://", "")
                // remove the triple-W
                .replace("www.", "")
                // just in case the domain is a URL and not just the domain, drop anything after the first slash
                .split("/")[0];


        // whois lookup
        WhoisLookup lookup = new WhoisLookup(sanitizedDomain);
        lookup.filterDumpedData();
        Whois result = lookup.getResult();
        model.addAttribute("whoisResult", result);

        return "lilblu";
    }

    @GetMapping("/toolbox/exceptions")
    public String getToolboxExceptions(Model model) {
        model.addAttribute("tool", "exception_sending");
        return "lilblu";
    }


}
