package com.codeoftheweb.salvo.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    private int turn;

    @ElementCollection
    @Column(name="salvoLocation")
    private List<String> salvoLocations = new LinkedList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Salvo() {
    }

    public Salvo( int turn, List<String> salvoLocations, GamePlayer gamePlayer) {
        this.turn = turn;
        this.salvoLocations = salvoLocations;
        this.gamePlayer=gamePlayer;
    }



    public Map<String, Object> makeSalvoDTO (){

        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocations());
        return dto;
    }



    public void addSalvoLocations (String salvoLocation){
        this.salvoLocations.add(salvoLocation);
    }


    //Getters y setters

    @JsonIgnore
    public long getId() {
        return id;
    }

    @JsonIgnore
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @JsonIgnore
    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    @JsonIgnore


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
