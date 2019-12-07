package com.codeoftheweb.salvo.models;


import com.codeoftheweb.salvo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Util {


    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------

    public static String getState(GamePlayer gamePlayerSelf, GamePlayer gamePlayerOpponent) {


        if (gamePlayerSelf.getShips().isEmpty()) {
            return "PLACESHIPS";
        }

        if (gamePlayerSelf.getGame().getGamePlayers().size() == 1) {
            return "WAITINGFOROPP";
        }
        if (gamePlayerSelf.getShips().isEmpty() || gamePlayerOpponent.getShips().isEmpty()) {
            return "WAIT";
        } else {

            if (gamePlayerSelf.getSalvoes().isEmpty()) {
                return "PLAY";
            }

            if (!gamePlayerSelf.getSalvoes().isEmpty() && gamePlayerOpponent.getSalvoes().isEmpty()) {
                return "WAIT";
            }

            if (gamePlayerSelf.getSalvoes().isEmpty() && !gamePlayerOpponent.getSalvoes().isEmpty()) {
                return "PLAY";
            }

//                              ---------------------------------------------

            boolean allMyShipsSunked = gamePlayerSelf.getDamages(gamePlayerSelf, gamePlayerOpponent);
            boolean allMyOpponentShipsSunked = gamePlayerSelf.getDamages(gamePlayerOpponent, gamePlayerSelf);


            if (gamePlayerSelf.getSalvoes().size() == gamePlayerOpponent.getSalvoes().size()) {

                if (allMyShipsSunked && allMyOpponentShipsSunked) {
                    return "TIE";
                }
                if (allMyOpponentShipsSunked) {
                    return "WON";
                }
                if (allMyShipsSunked) {
                    return "LOST";
                }
                return "PLAY";
            } //ambos jugadores
            if (gamePlayerSelf.getSalvoes().size() > gamePlayerOpponent.getSalvoes().size())
                return "WAIT";
        }
        return "PLAY";
    }
    //--------------------------------------------------------------------------------------------------------------------------------


    public static boolean existScore(Game game) {
        boolean existScore = false;

        if (!game.getScores().isEmpty()) {
            existScore = true;
        }
        return existScore;

    }
}
