package co.empresa.proyecto_desarrollo3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public String publicTest() {
        return "Public endpoint";
    }

    @GetMapping("/private")
    public String privateTest() {
        return "Private endpoint ";
    }
}