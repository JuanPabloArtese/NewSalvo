package com.codeoftheweb.salvo;



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
    // --------------------------------------------------------------------------------------------------------------------------------
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

    // --------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping("/game_view/{gpid}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable (name = "gpid") long gpid, Authentication authentication) {

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Paso algo6"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
        return new ResponseEntity<>(Util.makeMap("error","paso algo5"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer==null){
            return new ResponseEntity<>(Util.makeMap("error","paso algo7"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getId() != playerLogueado.getId()){
        return new ResponseEntity<>(Util.makeMap("error","paso algo4"),HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();

        hits.put("self", new ArrayList<>());
        hits.put("opponent", new ArrayList<>());

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gameState", getState(gamePlayer, gamePlayer.getOpponent()));
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream().map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                .stream().flatMap(gamePlayer1 -> gamePlayer.getSalvoes()
                        .stream()).map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));
               dto.put("scores", gamePlayer.getGame().getGamePlayers()
                        .stream().map(gamePlayer1 -> gamePlayer.getScore()));
               dto.put("hits", hits);

        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

    // --------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping (path = "/game/{gpid}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gpid, Authentication authentication) {

        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "You can't join a game if you are not logged in"), HttpStatus.UNAUTHORIZED);
        }

        if (gameRepository.findById(gpid).get() == null) {
            return new ResponseEntity<>(Util.makeMap("error", "not such game"), HttpStatus.FORBIDDEN);
        }

        Game game = gameRepository.findById(gpid).get();


        if (game.getGamePlayers().size() == 2) {

            return new ResponseEntity<>(Util.makeMap("error", "game is full"), HttpStatus.FORBIDDEN);
        }

   GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(playerAuth(authentication), game));

    return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);

    }


    // --------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping (path = "/games/players/{gpid}/ships")
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable long gpid, @RequestBody List <Ship> ships, Authentication authentication){

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
        return new ResponseEntity<>(Util.makeMap("ok", "Barcos bien asignados"), HttpStatus.CREATED);
    }




    // --------------------------------------------------------------------------------------------------------------------------------


    @RequestMapping (path = "/games/players/{gpid}/salvos")
    public ResponseEntity<Map<String, Object>> placeSalvos(@PathVariable long gpid, @RequestBody Salvo salvo, Authentication authentication){

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Paso algo3"), HttpStatus.UNAUTHORIZED);
        }
        Player playerLogueado = playerRepository.findByEmail(authentication.getName()).orElse(null);
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);

        if (playerLogueado==null){
            return new ResponseEntity<>(Util.makeMap("error","paso algo9"), HttpStatus.UNAUTHORIZED);
        }

        if (playerLogueado.getId() != gamePlayer.getPlayer().getId()){
            return new ResponseEntity<>(Util.makeMap("error","paso algo 8"),HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getSalvoes().stream().filter(salvo1 -> salvo.getTurn() == salvo.getTurn()).count()>0){
            return new ResponseEntity<>(Util.makeMap("error", "Ya tiro salvos en ese turno"), HttpStatus.FORBIDDEN);

        }

        salvo.setGamePlayer(gamePlayer);
        salvoRepository.save(salvo);

        return new ResponseEntity<>(Util.makeMap("ok", "Salvos bien asignados"), HttpStatus.CREATED);
    }

    // --------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email).orElse(null) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

 //------------------------------------------------------------------------------------------------------------------------------------------

    public String getState (GamePlayer gamePlayerSelf, GamePlayer gamePlayerOpponent){
        if(gamePlayerSelf.getShips().isEmpty()){
            return "PLACESHIPS";
        }

        if(gamePlayerSelf.getGame().getGamePlayers().size()==1){
            return "WAITINGFOROPP";
        }

        if (gamePlayerSelf.getId()<gamePlayerOpponent.getId()){
            return "PLAY";
        }
        if (gamePlayerSelf.getId()>gamePlayerOpponent.getId()){

            return "WAIT";
        }
        return "LOST";
}

// ----------------------------------------------------------------------------------------------------------------------------------------------

    private Player playerAuth (Authentication authentication){
        return playerRepository.findByEmail(authentication.getName()).orElse(null);
}

}