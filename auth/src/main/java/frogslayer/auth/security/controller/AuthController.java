package frogslayer.auth.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(){
        return new ResponseEntity<>("토큰 갱신에 성공해슴", HttpStatus.OK);
    }
}
