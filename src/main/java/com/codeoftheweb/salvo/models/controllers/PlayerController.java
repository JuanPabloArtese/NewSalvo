package com.codeoftheweb.salvo.models.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Util;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping ("/api")
public class PlayerController {

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

    @RequestMapping(path = "/game/{gpid}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gpid, Authentication authentication) {

        if (Util.isGuest(authentication)) {
            return new ResponseEntity<>(Util.makeMap("error", "Jugador no logueado"), HttpStatus.UNAUTHORIZED);
        }

        if (gameRepository.findById(gpid).get() == null) {
            return new ResponseEntity<>(Util.makeMap("error", "No existe la partida"), HttpStatus.FORBIDDEN);
        }

        Game game = gameRepository.findById(gpid).get();

        if (game.getGamePlayers().size() == 2) {

            return new ResponseEntity<>(Util.makeMap("error", "La partida esta llena"), HttpStatus.FORBIDDEN);
        }
        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(playerAuth(authentication), game));

        return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);

    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Falta informacion", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email).orElse(null) !=  null) {
            return new ResponseEntity<>("Nombre de usuario en uso", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    private Player playerAuth (Authentication authentication){
        return playerRepository.findByEmail(authentication.getName()).orElse(null);
    }

}
