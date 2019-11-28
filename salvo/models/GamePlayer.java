package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "salvoGamePlayer", fetch = FetchType.EAGER)
    private Set <Salvo> salvoes = new HashSet<>();



    public GamePlayer() {
        this.joinDate = new Date();
    }

    public GamePlayer(Player player, Game game) {
        this.joinDate = new Date();
        this.player = player;
        this.game = game;
    }




    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }


    //Getters y setters
    @JsonIgnore
    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvos(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    @JsonIgnore
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Score> scores (Player player){

        Set <Score> scores = new HashSet<>();
        return scores;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    @JsonIgnore
    public Score getScore () {
        return player.getOneScore(this);
    }

    @JsonIgnore
    public GamePlayer getOpponent (){
       return this.getGame().getGamePlayers().stream()
                .filter(gamePlayer -> gamePlayer.getId() != this.getId())
                .findFirst()
                .orElse(new GamePlayer());

    }
}
