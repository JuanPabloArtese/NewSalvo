package com.codeoftheweb.salvo;



import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Score;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api")
public class AppController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public List<Map<String, Object>> getGameAll(){

        return gameRepository.findAll(  )
                .stream()
                .map(Game::makeGameDTO)
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gpid}")
    public Map<String, Object> getGameView(@PathVariable  long  gpid) {
        Map<String, Object> dto = new LinkedHashMap<>();
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).get();
                dto.put ("id", gamePlayer.getGame().getId());
                dto.put("created", gamePlayer.getGame().getCreationDate());
                dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).collect(Collectors.toList()));
                dto.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
                dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                        .stream().flatMap(gamePlayer1 -> gamePlayer.getSalvoes()
                                .stream()).map(salvo -> salvo.makeSalvoDTO())
                        .collect(Collectors.toList()));
                dto.put("scores", gamePlayer.getGame().getGamePlayers()
                        .stream().map(gamePlayer1 -> gamePlayer.getPlayer().getScores()
                                .stream().map(score -> makeScoreDTO())
                                .collect(Collectors.toList())));
        return dto;
    }

    @RequestMapping ("/leaderboard")
    public Map <String, Object> getLeaderboard(){
        Map <String, Object> dto = new LinkedHashMap<>();
          Score score;
          /*dto.put ("id", );
          dto.put ("score", );
          dto.put ("player", );

          */
        return dto;

    }

}
