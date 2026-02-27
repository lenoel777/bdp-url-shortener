package com.bdpit5.bus.controller;

import com.bdpit5.bus.dto.CreateUrlRequest;
import com.bdpit5.bus.dto.UrlResponse;
import com.bdpit5.bus.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UrlService urlService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("createUrlRequest", new CreateUrlRequest());
        return "index";
    }

    @PostMapping("/")
    public String submitForm(@ModelAttribute("createUrlRequest") CreateUrlRequest createUrlRequest, Model model) {
        try {
            UrlResponse urlResponse = urlService.createShortUrl(createUrlRequest);
            var os = urlResponse.getOutputSchemas();
            model.addAttribute("shortUrl",os.get(os.size()-1).getShortUrl());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }
}
