package com.bdpit5.bus.controller;

import com.bdpit5.bus.dto.CreateUrlRequest;
import com.bdpit5.bus.dto.QrRequest;
import com.bdpit5.bus.dto.UrlResponse;
import com.bdpit5.bus.service.QrService;
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
    private final QrService qrService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("createUrlRequest", new CreateUrlRequest());
        model.addAttribute("createQrRequest", new QrRequest());
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
        model.addAttribute("createQrRequest", new QrRequest());
        return "index";
    }

    @PostMapping("/qr")
    public String generateQr(@ModelAttribute("createQrRequest") QrRequest createQrRequest, Model model) {
        try {
            var qrResponse = qrService.generate(createQrRequest);
            var os = qrResponse.getOutputSchemas();
            model.addAttribute("qrCodeBase64", os.get(os.size() - 1).getQrCodeBase64());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("createUrlRequest", new CreateUrlRequest());
        return "index";
    }
}
