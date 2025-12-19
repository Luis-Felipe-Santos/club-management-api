package dev.clube_api.clube;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    @GetMapping("/boasVindas")
    public String boasVindas() {
        return "Boas Vindas ao Clube";
    }
}
