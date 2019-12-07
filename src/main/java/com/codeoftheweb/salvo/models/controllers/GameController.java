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
            return new ResponseEntity<>(Util.makeMap("error", "Jugador no logueado"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
            return new ResponseEntity<>(Util.makeMap("error","No esta autorizado para ver la partida"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer==null){
            return new ResponseEntity<>(Util.makeMap("error", "Gameplayer no valido"),HttpStatus.UNAUTHORIZED);
        }

        if (playerLogueado.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(Util.makeMap("error","No puede acceder a esta informacion"),HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getSalvoes().stream().filter(salvo1 -> salvo1.getTurn() == salvo.getTurn()).count()>0){
            return new ResponseEntity<>(Util.makeMap("error", "Ya tiro salvos en ese turno"), HttpStatus.FORBIDDEN);
        }

        if(gamePlayer.getSalvoes().isEmpty()){
            salvo.setTurn(1);
        }else{
            salvo.setTurn(gamePlayer.getSalvoes().size()+1);
        }

        salvo.setGamePlayer(gamePlayer);
        salvoRepository.save(salvo);
        gamePlayer.addSalvo(salvo);

        String state =  Util.getState(gamePlayer, gamePlayer.getOpponent());

        if ( state.equalsIgnoreCase("TIE") ) {
            Score scoreSelf = new Score(new Date(), 0.5,gamePlayer.getGame(), gamePlayer.getPlayer()) ;
            Score scoreOpponent = new Score(new Date(), 0.5, gamePlayer.getGame(), gamePlayer.getOpponent().getPlayer());
            if (!Util.existScore(gamePlayer.getGame())) {
                scoreRepository.save(scoreSelf);
                scoreRepository.save(scoreOpponent);
            }
        }else{
            GamePlayer gamePlayerGanador = gamePlayer;
            GamePlayer gamePlayerPerdedor = gamePlayer.getOpponent();
            //solo para inicializarlas
            if (state.equalsIgnoreCase("WON")){
                gamePlayerGanador = gamePlayer;
                gamePlayerPerdedor = gamePlayer.getOpponent();
                Score scoreWinner= new Score(new Date(), 1.0, gamePlayer.getGame(), gamePlayerGanador.getPlayer());
                Score scoreLoser = new Score(new Date(), 0.0, gamePlayer.getGame(), gamePlayerPerdedor.getPlayer());
                if (!Util.existScore(gamePlayer.getGame())) {
                    scoreRepository.save(scoreWinner);
                    scoreRepository.save(scoreLoser);
                }
            }
            if (state.equalsIgnoreCase("LOST")){
                gamePlayerGanador = gamePlayer.getOpponent();
                gamePlayerPerdedor = gamePlayer;
                Score scoreWinner= new Score(new Date(), 1.0, gamePlayer.getGame(), gamePlayerGanador.getPlayer());
                Score scoreLoser = new Score(new Date(), 0.0, gamePlayer.getGame(), gamePlayerPerdedor.getPlayer());
                if (!Util.existScore(gamePlayer.getGame())) {
                    scoreRepository.save(scoreWinner);
                    scoreRepository.save(scoreLoser);
                }
            }
        }
        return new ResponseEntity<>(Util.makeMap("OK", "Salvos bien asignados"), HttpStatus.CREATED);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping (path = "/games/players/{gpid}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable long gpid, @RequestBody List<Ship> ships, Authentication authentication){

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Jugador no logueado"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
            return new ResponseEntity<>(Util.makeMap("error","No esta autorizado para ver la partida"), HttpStatus.UNAUTHORIZED);
        }

        if (playerLogueado.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(Util.makeMap("error","No puede acceder a esta informacion"),HttpStatus.UNAUTHORIZED);
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
            return new ResponseEntity<>("Jugador no logueado", HttpStatus.UNAUTHORIZED);
        }
        Player playerAutenticado = playerRepository.findByEmail(authentication.getName()).orElse(null);

        if (playerAutenticado==null){
            return new ResponseEntity<>("No esta autorizado para ver la partida", HttpStatus.UNAUTHORIZED);
        }
        Game game = gameRepository.save(new Game());
        GamePlayer gamePlayer =gamePlayerRepository.save(new GamePlayer(playerAutenticado, game));

        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }
}
