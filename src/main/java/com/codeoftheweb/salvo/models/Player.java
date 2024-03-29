package com.codeoftheweb.salvo.models;


import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String email;
    private String password;



    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;


    public Player() {
    }

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }



    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getEmail());
        return dto;
    }




    //Getters y setters
    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonIgnore
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScore(Set<Score> scores) {
        this.scores = scores;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { //Solo necesario si das la opcion de cambiar la contraseña
        this.password = password;
    }

    @JsonIgnore
    public Score getOneScore(GamePlayer gamePlayer) {
        Score score = scores.stream()
                .filter(_score -> _score.getGame() == gamePlayer.getGame())
                .findFirst()
                .orElse(null);
        return score;
    }

}