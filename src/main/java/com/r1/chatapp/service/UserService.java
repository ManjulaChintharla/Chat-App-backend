// Authors: Satu ja Mirva
// Tässä luokassa ovat käyttäjää koskevat tietokanta-operaatiot suorittavat metodit.
// Tämä luokka toimii yhteistyössä UserController-luokan kanssa.

package com.r1.chatapp.service;

import com.r1.chatapp.crypt.Encrypt;
import com.r1.chatapp.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Rekisteröityminen:
    // Lisää uuden käyttäjän tietokantaan. Ei hyväksy jo käytössä olevaa
    // käyttäjätunnusta.
    public User createUser(String name, String username, String password) {

        String sql = "select count (*) from user where username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { username });

        if (count > 0) {
            throw new RuntimeException("Käyttäjätunnus on varattu.");
        } else {
            // Salasanan salaaminen SHA-256-salausalgoritmilla:
            String encryptedPassword = Encrypt.encryptPassword(password);

            sql = "insert into user (name, username, password) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, name, username, encryptedPassword);
            // Palauttaa uuden käyttäjän tiedot
            return new User(name, username, encryptedPassword);
        }
    }

    // Sisäänkirjautuminen:
    // Palauttaa käyttäjän ID:n, jos käyttäjänimi + salasana yhdistelmä löytyy tietokannasta.
    // Jos ei löydy, palauttaa 0.
    public int loginUser(String username, String password) {

        try {
            // Käyttäjän syöttämän salasanan muuttaminen hash-muotoon:
            String encryptedPassword = Encrypt.encryptPassword(password);

            // Jos käyttäjätunnus ja hash-muotoinen salasana löytyvät tietokannasta, palautetaan käyttäjän ID.
            String sql = "select user_id from user where username = ? and password = ?";
            int user_id = jdbcTemplate.queryForObject(sql, int.class, username, encryptedPassword);
            return user_id;
        } catch (Exception e) {
            return 0;
        }
    }

    // Salasanan vaihtaminen:
    // Tarkastetaan ensin, onko vanha salasana syötetty oikein.
    // Jos käyttäjätunnus + vanha salasana -yhdistelmä löytyy tietokannasta, salasanan vaihtaminen onnistuu.
    public void changePassword(User user) {

        // Käyttäjän syöttämän salasanan muuttaminen hash-muotoon:
        String encryptedPassword = Encrypt.encryptPassword(user.getPassword());

        String sql = "select count(*) from user where username = ? and password = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class,
                new Object[] { user.getUsername(), encryptedPassword });

        if (count > 0) {
            String newPassword = user.getNewPassword();
            String encryptedNewPassword = Encrypt.encryptPassword(newPassword);
            user.setNewPassword(encryptedNewPassword);

            sql = "update user set password = ? where username = ?";
            jdbcTemplate.update(sql, user.getNewPassword(), user.getUsername());
        } else {
            throw new RuntimeException("Virheellinen vanha salasana.");
        }
    }

    // Päänäkymän hakutoiminto, käyttäjähaku:
    // Hakee kaikkien (paitsi oman) käyttäjien id:n, nimen ja sähköpostiosoitteen
    // tietokannasta.
    // Metodin palauttama lista on järjestetty käyttäjänimen mukaan aakkosjärjestykseen.
    public List<Map<String, Object>> fetchAllUsers(String myId) {
        String sql = "select user_id, name, username from user where not user_id = ? order by username";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, myId);
        return users;
    }

}