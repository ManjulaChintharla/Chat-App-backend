// Authors: Satu ja Mirva
// frontilta tulevat HTTP-pyynnöt käsittelevät rajapinnat

package com.r1.chatapp.controller;
import com.r1.chatapp.dto.Message;
import com.r1.chatapp.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/chatapp/users")
public class MessageController {

    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Yhteystietojen hakeminen.
    // (yhteystiedot = familiarUsers = ne käyttäjät, joiden kanssa kirjautuneella käyttäjällä on keskusteluhistoriaa)
    // Näytetään käyttöliittymässä keskusteluikkunan vasemmalla puolella.
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/fetchFamiliarUsers/{userid}")
    public List<Message> fetchFamiliarUsers(@PathVariable("userid") int userid) {
        // kutsuu MessageServicen fetchFamiliarUsers-metodia ja 
        // välittää sille kirjautuneen käyttäjän id:n
        // kun rajapintaa kutsutaan, se palauttaa listan käyttäjän yhteystiedoista kutsujalle.
        return messageService.fetchFamiliarUsers(userid);
    }

    // Kahden käyttäjän väliseen keskusteluun kuuluvien viestien hakeminen.
    // Näytetään käyttöliittymässä päänäkymän keskusteluikkunassa.
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/listmessage/{from}/{to}")
    public List<Map<String, Object>> getListMessageChat(@PathVariable("from") Integer from, @PathVariable("to") Integer to) {
        // kutsuu MessageServicen getListMessage-metodia ja
        // välittää sille viestin lähettäjän id:n (from) vastaanottajan id:n (to)
        // kun rajapintaa kutsutaan, se palauttaa listan keskusteluun kuuluvista viesteistä kutsujalle.
        return messageService.getListMessage(from, to);
    }

    // Viestin lähettäminen toiselle käyttäjälle.
    // Suoritetaan, kun sisäänkirjautunut käyttäjä painaa päänäkymässä "lähetä viesti" -painiketta.
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, Message message) {
        // kutsuu mMessageServicen sendMessage-metodia ja
        // välittää sille viestin vastaanottajan id:n (to) ja viestin tiedot (message).
        messageService.sendMessage(to, message);

        // viestin tietojen printtaaminen konsoliin:
        System.out.println(
            "Viesti lähetetty!" +
            " Viestissä luki: " + message.getMessage() +
            " Viestin id oli: " + message.getMessageId() +
            " Lähettäjän id oli: " + message.getSenderId() +
            " Vastaanottajan id oli: " + message.getReceiverId()
        );
    }
}