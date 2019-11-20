package com.codeoftheweb.salvo.models;

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
    private GamePlayer salvoGamePlayer;


    public Salvo() {
    }

    public Salvo( int turn, List<String> salvoLocations, GamePlayer gamePlayer) {
        this.turn = turn;
        this.salvoLocations = (List<String>) salvoLocations;
        this.salvoGamePlayer=gamePlayer;
    }



    public Map<String, Object> makeSalvoDTO (){

        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("turn", this.getTurn());
        dto.put("locations", this.getSalvoLocations());
        return dto;
    }

    public void salvoLocations (String salvoLocation){
        this.salvoLocations.add(salvoLocation);
    }


    //Getters y setters
    public long getId() {
        return id;
    }


    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getSalvoLocations() {
        return (List<String>) salvoLocations;
    }

    public void setLocations(List<String> salvoLocations) {
        this.salvoLocations = (List<String>) salvoLocations;
    }

    public GamePlayer getGamePlayer() {
        return salvoGamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.salvoGamePlayer = gamePlayer;
    }

}
