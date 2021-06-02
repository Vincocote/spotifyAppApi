package com.examplespotify.SpotifyTest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping()
    public String getLoginPage(Model model) {
        model.addAttribute("something", "this is a test");
        return "loginPage";
    }

    @GetMapping("top-artists")
    public String getTopArtistsPage(Model model) {
        model.addAttribute("something", "Here are the top artists");
        return "topArtists";
    }

}
