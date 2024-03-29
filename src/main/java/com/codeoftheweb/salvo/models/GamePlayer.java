package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.models.controllers.AppController;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes;


    public GamePlayer() {
        this.joinDate = new Date();
    }

    public GamePlayer(Player player, Game game) {
        this.joinDate = new Date();
        this.player = player;
        this.game = game;
    }


//-------------------------------------------------------------------------------------------------------------------------------

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());

        return dto;
    }


    public Map<String, Object> makeHitsDTO(GamePlayer self, GamePlayer opponent) {

        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("self", makeDamageDTO(self, opponent));
        dto.put("opponent", makeDamageDTO(opponent, self));

        return dto;
    }


    public List<Map> makeDamageDTO(GamePlayer self, GamePlayer opponent) {
        List<Map> dto = new ArrayList<>();

        int carrierDamage = 0;
        int destroyerDamage = 0;
        int submarineDamage = 0;
        int patrolboatDamage = 0;
        int battleshipDamage = 0;

        List<String> carrierLocations = new ArrayList<>();
        List<String> destroyerLocations = new ArrayList<>();
        List<String> submarineLocations = new ArrayList<>();
        List<String> patrolboatLocations = new ArrayList<>();
        List<String> battleshipLocations = new ArrayList<>();
//        List<String> carrierLocationsAux = new ArrayList<>();
//        List<String> destroyerLocationsAux = new ArrayList<>();
//        List<String> submarineLocationsAux = new ArrayList<>();
//        List<String> patrolboatLocationsAux = new ArrayList<>();
//        List<String> battleshipLocationsAux = new ArrayList<>();

//        int sunk = 0;

        for (Ship ship : self.getShips()) {
            switch (ship.getType()) {
                case "carrier":
                    carrierLocations = ship.getShipLocations();
//                    carrierLocationsAux = ship.getShipLocations();
                    break;
                case "destroyer":
                    destroyerLocations = ship.getShipLocations();
//                    destroyerLocationsAux = ship.getShipLocations();
                    break;
                case "submarine":
                    submarineLocations = ship.getShipLocations();
//                    submarineLocationsAux = ship.getShipLocations();
                    break;
                case "patrolboat":
                    patrolboatLocations = ship.getShipLocations();
//                    patrolboatLocationsAux = ship.getShipLocations();
                    break;
                case "battleship":
                    battleshipLocations = ship.getShipLocations();
//                    battleshipLocationsAux = ship.getShipLocations();
                    break;
            }
        }
        for (Salvo salvo : opponent.getSalvoes()) {
            Integer carrierHitsInTurn = 0;
            Integer destroyerHitsInTurn = 0;
            Integer submarineHitsInTurn = 0;
            Integer patrolboatHitsInTurn = 0;
            Integer battleshipHitsInTurn = 0;
            Integer shotsMissed = salvo.getSalvoLocations().size();
            Map<String, Object> hitsMapPerTurn = new LinkedHashMap<>();
            Map<String, Object> damagesPerTurn = new LinkedHashMap<>();
            List<String> salvoLocationList = new ArrayList<>();
            List<String> hitCellsList = new ArrayList<>();
            salvoLocationList.addAll(salvo.getSalvoLocations());

            for (String salvoShot : salvoLocationList) {
                if (carrierLocations.contains(salvoShot)) {
                    carrierDamage++;
                    carrierHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    shotsMissed--;
//                    carrierLocationsAux.remove(salvoShot);
                }
                if (destroyerLocations.contains(salvoShot)) {
                    destroyerDamage++;
                    destroyerHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    shotsMissed--;
//                    destroyerLocationsAux.remove(salvoShot);
                }
                if (submarineLocations.contains(salvoShot)) {
                    submarineDamage++;
                    submarineHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    shotsMissed--;
//                    submarineLocationsAux.remove(salvoShot);
                }
                if (patrolboatLocations.contains(salvoShot)) {
                    patrolboatDamage++;
                    patrolboatHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    shotsMissed--;
//                    patrolboatLocationsAux.remove(salvoShot);
                }
                if (battleshipLocations.contains(salvoShot)) {
                    battleshipDamage++;
                    battleshipHitsInTurn++;
                    hitCellsList.add(salvoShot);
                    shotsMissed--;
//                    battleshipLocationsAux.remove(salvoShot);
                }

            }
            damagesPerTurn.put("carrierHits", carrierHitsInTurn);
            damagesPerTurn.put("destroyerHits", destroyerHitsInTurn);
            damagesPerTurn.put("submarineHits", submarineHitsInTurn);
            damagesPerTurn.put("patrolboatHits", patrolboatHitsInTurn);
            damagesPerTurn.put("battleshipHits", battleshipHitsInTurn);
            damagesPerTurn.put("carrier", carrierDamage);
            damagesPerTurn.put("destroyer", destroyerDamage);
            damagesPerTurn.put("submarine", submarineDamage);
            damagesPerTurn.put("patrolboat", patrolboatDamage);
            damagesPerTurn.put("battleship", battleshipDamage);

            hitsMapPerTurn.put("turn", salvo.getTurn());
            hitsMapPerTurn.put("hitLocations", hitCellsList);
            hitsMapPerTurn.put("damages", damagesPerTurn);
            hitsMapPerTurn.put("missed", shotsMissed);
            dto.add(hitsMapPerTurn);

        }

        return dto;
}

    //--------------------------------------------------------------------------------------------------------------------------------
    //    public Map<String, Object> makeHitsDTO(Salvo salvo, GamePlayer gamePlayer){
//
//        Map<String, Object> dto = new LinkedHashMap<>();
//        dto.put("turn", gamePlayer.getSalvoes().stream().map(salvo1 -> salvo.getTurn()));
//        dto.put("hitLocations", makeLocationsDTO(salvo, gamePlayer));
//        dto.put ("damages", makeDamageDTO(gamePlayer));
//
//        return dto;
//    }
//
//    public List<String> makeLocationsDTO(){
//
//        return gamePlayer.getShips()
//                .stream()
//                .flatMap(ship -> ship.getShipLocations()
//                        .stream()
//                        .flatMap(shipLocation -> gamePlayer
//                                .getOpponent()
//                                .getSalvoes()
//                                .stream()
//                                .flatMap(salvo1 -> salvo
//                                        .getSalvoLocations()
//                                        .stream()
//                                        .filter(salvoLoc-> shipLocation.contains(salvoLoc)))))
//                .collect(Collectors.toList())
//                ;
//    }

//    public int countHits(Ship ship, GamePlayer gamePlayer){
//
//        int contador = 0;
//        for (int i=0; i <=gamePlayer.getShips().size(); i++) {
//
//            for (int j=0; j <= ship.getShipLocations().size(); j++){
//
//               if (ship.getShipLocations().get(j).contains((CharSequence) gamePlayer.getOpponent().getSalvoes().stream().map(salvo -> salvo.getSalvoLocations()))){
//
//                   contador++;
//
//               }
//
//            }
//
//        }
//        return contador;
//    }
    //--------------------------------------------------------------------------------------------------------------------------------



    //--------------------------------------------------------------------------------------------------------------------------------


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
