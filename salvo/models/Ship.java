package com.codeoftheweb.salvo.models;



import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String typeShip;

    @ElementCollection
    @Column(name="location")
    private List<String> shipLocations = new LinkedList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Ship() {
    }

    public Ship(String typeShip, List<String> shipLocations, GamePlayer gamePlayer) {
        this.typeShip = typeShip;
        this.shipLocations = shipLocations;
        this.gamePlayer=gamePlayer;
     }


    public Map <String, Object> makeShipDTO (){

        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("locations", this.getShipLocations());
        dto.put ("type", this.getTypeShip());
        return dto;
    }

    public void shipLocations (String location){
        this.shipLocations.add(location);
    }


    //Getters y setters
    public long getId() {
        return id;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }

    public String getTypeShip() {
        return typeShip;
    }

    public void setTypeShip(String typeShip) {
        this.typeShip = typeShip;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.shipLocations = new LinkedList<>(locations);
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
