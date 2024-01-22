// Author: Satu
// Sisältää WebsSocket-konfiguroinnin

package com.r1.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // registerStompEndpoints:
    // WebSocket-yhteys voidaan muodostaa osoitteessa "/ws"
    // kaikki alkuperät sallitaan

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    // configureMessageBroker:
    // clientin ja serverin välisen viestinvälitysjärjestelmän (MessageBroker)
    // määritteleminen
    // viestejä voi lähettää ja vastaanottaa osoitteessa "/topic"
    // viestejä voi lähettää palvelimelle osoitteesta "/app".

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

}