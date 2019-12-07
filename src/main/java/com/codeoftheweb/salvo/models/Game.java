package com.codeoftheweb.salvo.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date creationDate;


    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private List<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores;

    public Game (){
    this.creationDate = new Date();
    }

    public Game (Date creationDate){
        this.creationDate = creationDate;
    }

    //--------------------------------------------------------------------------------------------------------------------------------

    public Map<String, Object> makeGameDTO(){

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put ("created", this.getCreationDate());
        dto.put ("gamePlayers", this.getGamePlayers()
                .stream()
                .map (gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("scores", this.getScores()
                .stream()
                .map(score -> score.makeScoreDTO())
                .collect(Collectors.toList()));
        return dto;

    }

    //--------------------------------------------------------------------------------------------------------------------------------

    //Getters y setters
    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonIgnore
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

}
