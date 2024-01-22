// Authors: Satu ja Mirva
// frontilta tulevat HTTP-pyynnöt käsittelevät rajapinnat

package com.r1.chatapp.controller;
import com.r1.chatapp.dto.User;
import com.r1.chatapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/chatapp/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Uuden käyttäjän rekisteröiminen
    // Suoritetaan, kun käyttäjä painaa "rekisteröidy" -painiketta rekisteröitymisnäkymässä
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User userDTO) {
        String name = userDTO.getName();
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        // Tarkistus, että attribuutit eivät ole tyhjiä
        if (name == null || username == null || password == null) {
            // Uuden käyttäjän luominen ei tässä tapauksessa onnistu
            return ResponseEntity.badRequest().body("Kentät eivät saa olla tyhjiä.");
        }

        // Kutsuu UserServicen createUser-metodia ja
        // välittää sille käyttäjän syöttämän nimen, käyttäjätunnuksen ja salasanan
        // palauttaa tilakoodin 200 ok kutsujalle
        User user = userService.createUser(name, username, password);
        return ResponseEntity.ok(user);
    }

    // Sisäänkirjautuminen
    // Suoritetaan, kun käyttäjä painaa "kirjaudu sisään" -painiketta kirjautumisnäkymässä
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User userDTO) {

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        // Tarkistus, että attribuutit eivät ole tyhjiä
        if (username == null || password == null) {
            // Sisäänkirjautuminen ei tässä tapauksessa onnistu
            return ResponseEntity.badRequest().body("Kentät eivät saa olla tyhjiä.");
        }

        // Kutsuu UserServicen loginUser-metodia ja
        // välittää sille käyttäjän syöttämän käyttäjätunnuksen ja salasanan
        int authUser = userService.loginUser(username, password);

        // jos käyttäjä löytyy, palauttaa kutsujalle tilakoodin 200 ok
        if (authUser != 0) {
            return ResponseEntity.ok(authUser);
        }
        else {
            return ResponseEntity.badRequest().body("Käyttäjätunnus tai salasana virheellinen.");
        }
    }

    // Kaikkien käyttäjien hakeminen tietokannasta hakutoimintoa varten
    // Suoritetaan sisäänkirjautumisen jälkeen kun pääikkuna avautuu
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/fetchAllUsers/{loggedInUserId}")
    public List<Map<String, Object>> fetchAllUsers(@PathVariable("loggedInUserId") String loggedInUserId) {
        // Kutsuu UserServicen fetchAllUsers-metodia ja välittää sille sisäänkirjautuneen käyttäjän id:n
        return userService.fetchAllUsers(loggedInUserId);
    }

    // Salasanan vaihtaminen
    // Suoritetaan salasanan vaihto -näkymässä kun käyttäjä on syöttänyt vanhan salasanan ja uuden salasanan
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody User userDTO) {
        // Kutsuu UserServicen changePassword-metodia ja
        // välittää sille käyttäjän syöttämän vanhan ja uuden salasanan
        try {
            userService.changePassword(userDTO);
            return ResponseEntity.ok("Salasanan vaihtaminen onnistui.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Salasanan vaihtaminen epäonnistui.");
        }
    }

}