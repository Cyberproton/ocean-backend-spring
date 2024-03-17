package me.cyberproton.ocean.auth;

import me.cyberproton.ocean.common.V1ApiController;
import org.springframework.web.bind.annotation.PostMapping;

@V1ApiController("auth")
public class AuthController {
    @PostMapping("/login")
    public void login() {

    }
}
