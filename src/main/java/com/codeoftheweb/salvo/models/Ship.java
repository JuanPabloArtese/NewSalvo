package com.codeoftheweb.salvo.models;


import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String type;

    @ElementCollection
    @Column(name="location")
    private List<String> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Ship() {
    }

    public Ship(String type, List<String> locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations=locations;
        this.gamePlayer=gamePlayer;
     }


    public Map <String, Object> makeShipDTO (){

        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("locations", this.getLocations());
        dto.put ("type", this.getType());
        return dto;
    }

    public void shipLocations (String location){
        this.locations.add(location);
    }

    public long getId() {
        return id;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = new ArrayList<>(locations);
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

}
