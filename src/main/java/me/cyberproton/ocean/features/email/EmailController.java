package me.cyberproton.ocean.features.email;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
}
