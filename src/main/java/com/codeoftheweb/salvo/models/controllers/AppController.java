package com.codeoftheweb.salvo.models.controllers;



import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api")
public class AppController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping("/game_view/{gpid}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable (name = "gpid") long gpid, Authentication authentication) {

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Jugador no logueado"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
        return new ResponseEntity<>(Util.makeMap("error","No esta autorizado para ver la partida"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer==null){
            return new ResponseEntity<>(Util.makeMap("error","Gameplayer no valido"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getId() != playerLogueado.getId()){
        return new ResponseEntity<>(Util.makeMap("error","No puede acceder a esta informacion"),HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();
        String state = Util.getState(gamePlayer, gamePlayer.getOpponent());

        hits.put("self", new ArrayList<>());
        hits.put("opponent", new ArrayList<>());

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gameState", state);
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream().map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                .stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                        .stream()).map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));

        if( state.equalsIgnoreCase("PLACESHIPS") ||
                state.equalsIgnoreCase("WAITINGFOROP") ||
                gamePlayer.getOpponent().getSalvoes() == null
        )    {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
            dto.put("hits", hits);
        }else{
            dto.put("hits", gamePlayer.makeHitsDTO(gamePlayer, gamePlayer.getOpponent()));
        }


        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

}



