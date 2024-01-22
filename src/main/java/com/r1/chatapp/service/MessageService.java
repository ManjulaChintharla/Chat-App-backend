// Authors: Satu ja Mirva
// Tässä luokassa ovat viestejä koskevat tietokanta-operaatiot suorittavat metodit.
// Toimii yhteistyössä MessageController-luokan kanssa.

package com.r1.chatapp.service;
import com.r1.chatapp.dto.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class MessageService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Hakee tietokannasta sellaiset käyttäjät, joiden kanssa sisäänkirjautunut
    // käyttäjä on käynyt keskusteluja sekä
    // ja kuhunkin keskusteluun kuuluvat viestit
    // Metodin palauttama lista on järjestetty viestien päivämäärän perusteella
    // uusimmasta vanhimpaan.
    // Metodin palauttamat tiedot näytetään käyttöliittymän päänäkymän Omat
    // Yhteystiedot -näkymässä.
    public List<Message> fetchFamiliarUsers(int user_id) {

        String sql = "SELECT m.message_id, m.message, u1.user_id as sender_id, u1.username AS sender, u2.user_id AS receiver_id, u2.username AS receiver, m.date AS latest_date "
                +
                "FROM message m " +
                "INNER JOIN user u1 ON m.sender_id = u1.user_id " +
                "INNER JOIN user u2 ON m.receiver_id = u2.user_id " +
                "WHERE (m.sender_id = ? OR m.receiver_id = ?) " +
                "AND m.date = ( " +
                "SELECT MAX(date) FROM message " +
                "WHERE (sender_id = m.sender_id AND receiver_id = m.receiver_id) " +
                "OR (sender_id = m.receiver_id AND receiver_id = m.sender_id) " +
                ")" +
                "ORDER BY latest_date DESC;";

        List<Message> familiarUsers = jdbcTemplate.query(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, user_id);
                ps.setInt(2, user_id);
            }
        }, (rs, rowNum) -> {

            Message familiarUser = new Message();

            familiarUser.setMessageId(rs.getInt("message_id"));

            String message = rs.getString("message");

            // Viimeisimmän viestin pituuden tarkastus. Jos yli 40 merkkiä,
            // UI:ssä näytetään vain viestin alkuosa.
            if (message.length() < 40) {
                familiarUser.setMessage(message);
            } else {
                String shortenedMessage = message.substring(0, 40) + "...";
                familiarUser.setMessage(shortenedMessage);
            }

            int sender_id = rs.getInt("sender_id");

            if (user_id != sender_id) {
                familiarUser.setContactId(sender_id);
                familiarUser.setContactName(rs.getString("sender"));
            }

            int receiver_id = rs.getInt("receiver_id");

            if (user_id != receiver_id) {
                familiarUser.setContactId(receiver_id);
                familiarUser.setContactName(rs.getString("receiver"));
            }

            // Päivämäärän formatointi (viimeisimmän viestin päivämäärä Omat Yhteystiedot
            // -näkymässä)
            Timestamp timestamp = rs.getTimestamp("latest_date");
            LocalDateTime localdatetime = timestamp.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            String formattedDateTime = localdatetime.format(formatter);
            String datetime = formattedDateTime;
            familiarUser.setDatetime(datetime);

            return familiarUser;
        });

        return familiarUsers;
    }

    // Päänäkymän keskusteluikkunassa näytettävät viestit:
    // Haekee kaikki kahden käyttäjän väliseen keskusteluun kuuluvat viestit tietokannasta.
    // Viestit järjestetään lähetysajan mukaan, vanhin viesti ensin.
    public List<Map<String, Object>> getListMessage(@PathVariable("from") Integer from,
            @PathVariable("to") Integer to) {

        List<Map<String, Object>> messagelist = jdbcTemplate.queryForList(
                "select * from message where (sender_id = ? and receiver_id = ?) or (receiver_id = ? and sender_id = ?) order by date asc",
                from, to, from, to);

        // Viestien päivämäärien formatointi:
        for (Map<String, Object> item : messagelist) {
            Timestamp timestamp = (Timestamp) item.get("date");
            LocalDateTime localdatetime = timestamp.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            String formattedDateTime = localdatetime.format(formatter);

            item.put("date", formattedDateTime);
        }

        return messagelist;
    }

    // Yksityisviestin lähettäminen:
    // Tallentaa uuden viestin tietokantaan, jonka jälkeen simpMessagingTemplate ilmoittaa
    // uudesta viestistä osoitteessa topic/messages
    public void sendMessage(String to, Message message) {

        // Päivämäärän asettaminen viestille ennen viestin tallentamista
        LocalDateTime date = LocalDateTime.now();
        message.setDate(date);

        String sql = "insert into message (message, date, sender_id, receiver_id) values (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, message.getMessage(), message.getDate(), message.getSenderId(),
                    message.getReceiverId());
            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
        } catch (Exception e) {
            System.out.println("Viestin lähettäminen epäonnistui.");
        }
    }
}