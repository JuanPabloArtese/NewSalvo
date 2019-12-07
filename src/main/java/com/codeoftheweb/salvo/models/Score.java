package com.codeoftheweb.salvo.models;


import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private double score;
    private Date finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    public Score() {this.finishDate = new Date();
    }

    public Score(Date finishDate, double score, Game game, Player player) {
        this.finishDate = new Date();
        this.score=score;
        this.game = game;
        this.player = player;
    }

    //--------------------------------------------------------------------------------------------------------------------------------

    public Map<String, Object> makeScoreDTO (){

        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("finishDate", this.finishDate);
        dto.put("score", this.getScore());
        dto.put("player", this.getPlayer().getId());
        return dto;
    }

    //--------------------------------------------------------------------------------------------------------------------------------

//Getters y setters

    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonIgnore
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @JsonIgnore
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
