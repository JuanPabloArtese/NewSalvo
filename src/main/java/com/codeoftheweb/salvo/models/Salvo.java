package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    private int turnNumber;

    @ElementCollection
    @Column(name="salvoLocation")
    private List<String> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;



    public Salvo() {
    }

    public Salvo( int turnNumber, List<String> locations, GamePlayer gamePlayer) {
        this.turnNumber = turnNumber;
        this.locations = locations;
        this.gamePlayer=gamePlayer;
    }



    public Map<String, Object> makeSalvoDTO (){

        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("turn", this.getTurnNumber());
        dto.put("locations", this.getLocations());
        return dto;
    }

    public void salvoLocations (String location){
        this.locations.add(location);
    }


    public long getId() {
        return id;
    }


    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
