package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

    private java.util.List<String> List;

    public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {

			//Crea y guarda players
		    Player player1 = new Player("Jack");
			Player player2 = new Player("Chloe");
			Player player3 = new Player("Kim");
			Player player4 = new Player("David");
			Player player5 = new Player("Michelle");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);

			//Crea y guarda games
			Game game1 = new Game();
			Game game2 = new Game();

			gameRepository.save(game1);
			gameRepository.save(game2);

			//Crea y guarda GamePlayers
			GamePlayer gamePlayer1 = new GamePlayer(player1, game1);
			GamePlayer gamePlayer2= new GamePlayer(player2, game1);
			GamePlayer gamePlayer3 = new GamePlayer(player3, game2);

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);


			//Crea y guarda ships
            Ship ship1 = new Ship();
            Ship ship2 = new Ship();
            Ship ship3 = new Ship();
            Ship ship4 = new Ship();
            Ship ship5 = new Ship();

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);

			ship1.shipLocations("A1");
			ship1.shipLocations("A2");
			ship1.shipLocations("A3");

			ship2.shipLocations("F3");
			ship2.shipLocations("F4");
			ship2.shipLocations("F5");
			ship2.shipLocations("F6");
			ship2.shipLocations("F7");


            //Crea y guarda los salvos
            Salvo salvo1 = new Salvo();
            Salvo salvo2 = new Salvo();
            Salvo salvo3 = new Salvo();
            Salvo salvo4 = new Salvo();
            Salvo salvo5 = new Salvo();
            Salvo salvo6 = new Salvo();
            Salvo salvo7 = new Salvo();
            Salvo salvo8 = new Salvo();
            Salvo salvo9 = new Salvo();
            Salvo salvo10 = new Salvo();


            salvoRepository.save (salvo1);
            salvoRepository.save (salvo2);
            salvoRepository.save (salvo3);
            salvoRepository.save (salvo4);
            salvoRepository.save (salvo5);
            salvoRepository.save (salvo6);
            salvoRepository.save (salvo7);
            salvoRepository.save (salvo8);
            salvoRepository.save (salvo9);
            salvoRepository.save (salvo10);

            salvo1.salvoLocations("A3");
            salvo2.salvoLocations("A4");
            salvo3.salvoLocations("F5");
            salvo4.salvoLocations("F6");
            salvo5.salvoLocations("F2");
		};
	}
}