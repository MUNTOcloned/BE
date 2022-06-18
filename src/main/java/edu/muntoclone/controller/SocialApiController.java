package edu.muntoclone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialApiController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials")
    public void addSocial() {

    }

    @PatchMapping("/socials/{id}")
    public void updateSocial(@PathVariable Long id) {

    }

    @DeleteMapping("/socials/{id}")
    public void deleteSocial(@PathVariable Long id) {

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/socials/{id}/participation")
    public void participate(@PathVariable Long id) {

    }
}
