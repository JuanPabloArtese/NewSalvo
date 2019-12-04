package com.codeoftheweb.salvo.models.controllers;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api")
public class GameController {

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

    //------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping("/games")
    public Map<String, Object> getGameAll(Authentication authentication) {
        Map <String, Object> dto = new LinkedHashMap<>();
        if (Util.isGuest (authentication)){
            dto.put("player", "Guest");
        }
        else
        {
            Player playerAutenticado = playerRepository.findByEmail(authentication.getName()).get();
            dto.put("player", playerAutenticado.makePlayerDTO());
        }
        dto.put("games", gameRepository.findAll()
                .stream()
                .map(Game::makeGameDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(path = "/games/players/{gpid}/salvoes")
    public ResponseEntity<Map<String, Object>> placeSalvos(@PathVariable long gpid, @RequestBody Salvo salvo, Authentication authentication){

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Paso algo3"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
            return new ResponseEntity<>(Util.makeMap("error","paso algo9"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer==null){
            return new ResponseEntity<>(Util.makeMap("error", "paso algo10"),HttpStatus.UNAUTHORIZED);
        }

        if (playerLogueado.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(Util.makeMap("error","paso algo 8"),HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getSalvoes().stream().filter(salvo1 -> salvo1.getTurn() == salvo.getTurn()).count()>0){
            return new ResponseEntity<>(Util.makeMap("error", "Ya tiro salvos en ese turno"), HttpStatus.FORBIDDEN);

        }

        salvo.setGamePlayer(gamePlayer);
        salvo.setTurn(1);
        salvoRepository.save(salvo);

        return new ResponseEntity<>(Util.makeMap("ok", "Salvos bien asignados"), HttpStatus.CREATED);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping (path = "/games/players/{gpid}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable long gpid, @RequestBody List<Ship> ships, Authentication authentication){

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Paso algo3"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
            return new ResponseEntity<>(Util.makeMap("error","paso algo2"), HttpStatus.UNAUTHORIZED);
        }

        if (playerLogueado.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(Util.makeMap("error","paso algo 1"),HttpStatus.UNAUTHORIZED);
        }

        if (!gamePlayer.getShips().isEmpty()) {
            return new ResponseEntity<>(Util.makeMap("error", "Ya tiene barcos colocados"),HttpStatus.FORBIDDEN);
        }


        ships.stream().forEach(ship -> ship.setGamePlayer(gamePlayer));
        shipRepository.saveAll(ships);
        return new ResponseEntity<>(Util.makeMap("OK", "Barcos bien asignados"), HttpStatus.CREATED);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping (path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication){
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>("No esta autorizado1", HttpStatus.UNAUTHORIZED);
        }
        Player playerAutenticado = playerRepository.findByEmail(authentication.getName()).orElse(null);

        if (playerAutenticado==null){
            return new ResponseEntity<>("No esta autorizado2", HttpStatus.UNAUTHORIZED);
        }
        Game game = gameRepository.save(new Game());
        GamePlayer gamePlayer =gamePlayerRepository.save(new GamePlayer(playerAutenticado, game));

        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }
}
