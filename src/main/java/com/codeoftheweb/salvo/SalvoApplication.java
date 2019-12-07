package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.temporal.ChronoUnit;
import java.util.*;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

    public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository)  {
		return (args) -> {

            //Creacion de jugadores
            Player player_jBauer = new Player("jpa@hola.com", passwordEncoder().encode("hola1234"));
            Player player_obrian = new Player("c.obrian@ctu.gov",passwordEncoder().encode("sdfasdfasdf"));
            Player player_kBauer = new Player("kim_bauer@gmail.com", passwordEncoder().encode("sadfasdfasd"));
            Player player_almeida = new Player("t.almeida@ctu.gov", passwordEncoder().encode("sdfasdfadf"));
            Player player_asdfasdda = new Player("admin@admin.com", passwordEncoder().encode("admin"));

            //creacion de juegos
            Game game1 = new Game();
            Game game2 = new Game();
            Game game3 = new Game();
            Game game4 = new Game();
            Game game5 = new Game();
            Game game6 = new Game();
            Game game7 = new Game();
            Game game8 = new Game();

            //creacion de gamePlayers
            GamePlayer gamePlayer1 = new GamePlayer(player_jBauer, game1); //game1
            GamePlayer gamePlayer2 = new GamePlayer(player_obrian, game1); //game1

            GamePlayer gamePlayer3 = new GamePlayer(player_jBauer, game2); //game2
            GamePlayer gamePlayer4 = new GamePlayer(player_obrian, game2); //game2

            GamePlayer gamePlayer5 = new GamePlayer(player_obrian, game3); //game3
            GamePlayer gamePlayer6 = new GamePlayer(player_almeida, game3); //game3

            GamePlayer gamePlayer7 = new GamePlayer(player_obrian, game4); //game4
            GamePlayer gamePlayer8 = new GamePlayer(player_jBauer, game4); //game4

            GamePlayer gamePlayer9 = new GamePlayer(player_almeida, game5); //game5
            GamePlayer gamePlayer10 = new GamePlayer(player_jBauer, game5); //game5

//            GamePlayer gamePlayer11 = new GamePlayer(player_kBauer, game6); //game6
//            GamePlayer gamePlayer12 = new GamePlayer( , game6); //game6

//            GamePlayer gamePlayer13 = new GamePlayer(player_almeida, game7); //game7
//            GamePlayer gamePlayer14 = new GamePlayer( , game7); //game7

            GamePlayer gamePlayer15 = new GamePlayer(player_kBauer, game8); //game8
            GamePlayer gamePlayer16 = new GamePlayer(player_almeida, game8); //game8



            //por consigna quiere que le modifiquemos la hora para que aparezca una hora despues del anterior
            game2.setCreationDate(Date.from(game1.getCreationDate().toInstant().plusSeconds(3600)));
            game3.setCreationDate(Date.from(game2.getCreationDate().toInstant().plusSeconds(3600)));
            game4.setCreationDate(Date.from(game3.getCreationDate().toInstant().plusSeconds(3600)));
            game5.setCreationDate(Date.from(game4.getCreationDate().toInstant().plusSeconds(3600)));
            game6.setCreationDate(Date.from(game5.getCreationDate().toInstant().plusSeconds(3600)));
            game7.setCreationDate(Date.from(game6.getCreationDate().toInstant().plusSeconds(3600)));
//            game8.setCreationDate(Date.from(game7.getCreationDate().toInstant().plusSeconds(3600)));
            game8.setCreationDate(Date.from(game7.getCreationDate().toInstant().plus(1, ChronoUnit.HOURS))); //otra forma de hacerlo

            //creacion ships

            //ships game 1
            Ship ship1 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("H2", "H3", "H4")), gamePlayer1);
            Ship ship2 = new Ship("SUBMARINE", new ArrayList<>(Arrays.asList("E1", "F1", "G1")), gamePlayer1);
            Ship ship3 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("B4", "B5")), gamePlayer1);
            Ship ship4 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gamePlayer2);
            Ship ship5 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("F1", "F2")), gamePlayer2);

            //ships game 2
            Ship ship6 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gamePlayer3);
            Ship ship7 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("C6", "C7")), gamePlayer3);
            Ship ship8 = new Ship("SUBMARINE", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gamePlayer4);
            Ship ship9 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("G6", "H6")), gamePlayer4);

            //ships game 3
            Ship ship10 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gamePlayer5);
            Ship ship11 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("C6", "C7")), gamePlayer5);
            Ship ship12 = new Ship("SUBMARINE", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gamePlayer6);
            Ship ship13 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("G6", "H6")), gamePlayer6);

            //ships game 4
            Ship ship14 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gamePlayer7);
            Ship ship15 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("C6", "C7")), gamePlayer7);
            Ship ship16 = new Ship("SUBMARINE", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gamePlayer8);
            Ship ship17 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("G6", "H6")), gamePlayer8);

            //ships game 5
            Ship ship18 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gamePlayer9);
            Ship ship19 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("C6", "C7")), gamePlayer9);
            Ship ship20 = new Ship("SUBMARINE", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gamePlayer10);
            Ship ship21 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("G6", "H6")), gamePlayer10);

            //ships game 6
//            Ship ship22 = new Ship(Ship.TypeShip.DESTROYER, gamePlayer11, new ArrayList<String>(Arrays.asList("B5", "C5", "D5")));
//            Ship ship23 = new Ship(Ship.TypeShip.PATROLBOAT, gamePlayer11, new ArrayList<String>(Arrays.asList("C6", "C7")));

            //ships game 8
            Ship ship24 = new Ship("DESTROYER", new ArrayList<>(Arrays.asList("B5", "C5", "D5")), gamePlayer15);
            Ship ship25 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("C6", "C7")), gamePlayer15);
            Ship ship26 = new Ship("SUBMARINE", new ArrayList<>(Arrays.asList("A2", "A3", "A4")), gamePlayer16);
            Ship ship27 = new Ship("PATROLBOAT", new ArrayList<>(Arrays.asList("G6", "H6")), gamePlayer16);



            //creacion de salvoes
            //salvoes game 1
            Salvo salvo_GamePlayer1_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("B5", "C5", "F1")), gamePlayer1);
            Salvo salvo_GamePlayer1_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("F2", "D5")), gamePlayer1);
            Salvo salvo_GamePlayer2_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("B4", "B5", "B6")), gamePlayer2);
            Salvo salvo_GamePlayer2_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("E1", "H3", "A2")), gamePlayer2);

            //salvoes game 2
            Salvo salvo_GamePlayer3_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("A2", "A4", "G6")), gamePlayer3);
            Salvo salvo_GamePlayer3_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("A3", "H6")), gamePlayer3);
            Salvo salvo_GamePlayer4_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("B5", "D5", "C7")), gamePlayer4);
            Salvo salvo_GamePlayer4_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("C5", "C6")), gamePlayer4);

            //salvoes game 3
            Salvo salvo_GamePlayer5_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("G6", "H6", "A4")), gamePlayer5);
            Salvo salvo_GamePlayer5_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("A2", "A3", "D8")), gamePlayer5);
            Salvo salvo_GamePlayer6_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("H1", "H2", "H3")), gamePlayer6);
            Salvo salvo_GamePlayer6_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("E1", "F2", "G3")), gamePlayer6);

            //salvoes game 4
            Salvo salvo_GamePlayer7_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("A3", "A4", "F7")), gamePlayer7);
            Salvo salvo_GamePlayer7_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("A2", "G6", "H6")), gamePlayer7);
            Salvo salvo_GamePlayer8_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("B5", "C6", "H1")), gamePlayer8);
            Salvo salvo_GamePlayer8_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("C5", "C7", "D5")), gamePlayer8);

            //salvoes game 5
            Salvo salvo_GamePlayer9_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("A1", "A2", "A3")), gamePlayer9);
            Salvo salvo_GamePlayer9_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("G6", "G7", "G8")), gamePlayer9);
            Salvo salvo_GamePlayer10_Turn1 = new Salvo(1, new ArrayList<>(Arrays.asList("B5", "B6", "C7")), gamePlayer10);
            Salvo salvo_GamePlayer10_Turn2 = new Salvo(2, new ArrayList<>(Arrays.asList("C6", "D6", "E6")), gamePlayer10);
            Salvo salvo_GamePlayer10_Turn3 = new Salvo(3, new ArrayList<>(Arrays.asList("H1", "H8")), gamePlayer10);


            Score score1 = new Score(new Date(), 1, game1,player_jBauer);
            Score score2 = new Score(new Date(), 0, game1,player_obrian);

            Score score3 = new Score(new Date(), 0.5, game2,player_jBauer);
            Score score4 = new Score(new Date(), 0.5, game2,player_obrian);

            Score score5 = new Score(new Date(), 1, game3,player_obrian);
            Score score6 = new Score(new Date(), 0, game3,player_almeida);

            Score score7 = new Score(new Date(), 0, game4,player_obrian);
            Score score8 = new Score(new Date(), 1, game4,player_jBauer);

            Score score9 = new Score(new Date(), 1, game5,player_almeida);
            Score score10 = new Score(new Date(), 0, game5,player_jBauer);

            //Score score11 = new Score (new Date(), 0.5, gamePlayer11, player_kBauer);
            //Score score12 = new Score (new Date(), 0.5, gamePlayer12, );

            //Score score13 = new Score (new Date(), 0.5, gamePlayer13, player_almeida );
            //Score score14 = new Score (new Date(), 0.5, gamePlayer14, );


            Score score15 = new Score (new Date(), 1, game6, player_kBauer);
            Score score16 = new Score (new Date(), 0, game6, player_almeida);





            //Creo listas para enviar toda la informacion en menos mensajes.
            java.util.List<Player> playerList = new LinkedList<>();
            playerList.addAll(new ArrayList<>(Arrays.asList(player_jBauer, player_obrian, player_kBauer, player_almeida, player_asdfasdda)));

            List<Game> gameList = new LinkedList<>();
            gameList.addAll(new ArrayList<>(Arrays.asList(game1, game2, game3, game4, game5, game6, game7, game8)));

            List<GamePlayer> gamePlayerList = new LinkedList<>();
            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer1, gamePlayer2)));//game 1
            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer3, gamePlayer4)));//game 2
            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer5, gamePlayer6)));//game 3
            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer7, gamePlayer8)));//game 4
            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer9, gamePlayer10)));//game 5
//            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer11, gamePlayer12)));//game 6
//            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer13, gamePlayer14)));//game 7
            gamePlayerList.addAll(new ArrayList<>(Arrays.asList(gamePlayer15, gamePlayer16)));//game 8

            List<Ship> shipList = new LinkedList<>();
            shipList.addAll(new ArrayList<>(Arrays.asList(ship1, ship2, ship3, ship4, ship5))); //game 1
            shipList.addAll(new ArrayList<>(Arrays.asList(ship6, ship7, ship8, ship9))); //game 2
            shipList.addAll(new ArrayList<>(Arrays.asList(ship10, ship11, ship12, ship13))); //game 3
            shipList.addAll(new ArrayList<>(Arrays.asList(ship14, ship15, ship16, ship17))); //game 4
            shipList.addAll(new ArrayList<>(Arrays.asList(ship18, ship19, ship20, ship21))); //game 5
//            shipList.addAll(new ArrayList<>(Arrays.asList(ship22, ship23))); //game 6
            shipList.addAll(new ArrayList<>(Arrays.asList(ship24, ship25, ship26, ship27))); //game 8



            List<Salvo> salvoList = new LinkedList<>();
            //game 1
            salvoList.addAll(new ArrayList<>(Arrays.asList(salvo_GamePlayer1_Turn1, salvo_GamePlayer1_Turn2, salvo_GamePlayer2_Turn1, salvo_GamePlayer2_Turn2)));

            //game 2
            salvoList.addAll(new ArrayList<>(Arrays.asList(salvo_GamePlayer3_Turn1, salvo_GamePlayer3_Turn2, salvo_GamePlayer4_Turn1, salvo_GamePlayer4_Turn2)));

            //game 3
            salvoList.addAll(new ArrayList<>(Arrays.asList(salvo_GamePlayer5_Turn1, salvo_GamePlayer5_Turn2, salvo_GamePlayer6_Turn1, salvo_GamePlayer6_Turn2)));

            //game 4
            salvoList.addAll(new ArrayList<>(Arrays.asList(salvo_GamePlayer7_Turn1, salvo_GamePlayer7_Turn2, salvo_GamePlayer8_Turn1, salvo_GamePlayer8_Turn2)));

            //game 5
            salvoList.addAll(new ArrayList<>(Arrays.asList(salvo_GamePlayer9_Turn1, salvo_GamePlayer9_Turn2, salvo_GamePlayer10_Turn1, salvo_GamePlayer10_Turn2, salvo_GamePlayer10_Turn3)));

            List<Score> scoreList = new LinkedList<>();

            scoreList.addAll(new ArrayList<>(Arrays.asList(score1, score2)));
            scoreList.addAll(new ArrayList<>(Arrays.asList(score3, score4)));
            scoreList.addAll(new ArrayList<>(Arrays.asList(score5, score6)));
            scoreList.addAll(new ArrayList<>(Arrays.asList(score7, score8)));
            scoreList.addAll(new ArrayList<>(Arrays.asList(score9, score10)));
            //scoreList.addAll(new ArrayList<>(Arrays.asList(score11, score12)));
            //scoreList.addAll(new ArrayList<>(Arrays.asList(score13, score14)));
            scoreList.addAll(new ArrayList<>(Arrays.asList(score15, score16)));


            //Guardar en la base de datos
            gameRepository.saveAll(gameList);
            playerRepository.saveAll(playerList);
            gamePlayerRepository.saveAll(gamePlayerList);
            shipRepository.saveAll(shipList);
            salvoRepository.saveAll(salvoList);
            scoreRepository.saveAll(scoreList);
//            scoreRepository.saveAll(Arrays.asList(score1, score2, score3));

        };
    }

}

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {


        @Autowired
       PlayerRepository playerRepository;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(inputName-> {
                        Player player = playerRepository.findByEmail(inputName).orElse(null);
                        if (player != null) {
                                return new User(player.getEmail(), player.getPassword(),
                                        AuthorityUtils.createAuthorityList("USER"));
                        } else {
                                throw new UsernameNotFoundException("Unknown user: " + inputName);
                        }
                });
        }
        }

        @Configuration
        @EnableWebSecurity
        class WebSecurityConfig extends WebSecurityConfigurerAdapter {
                @Override
                protected void configure(HttpSecurity http) throws Exception {
                        http.authorizeRequests()
                                    .antMatchers("/**").permitAll();
//                                .antMatchers("/web/**").permitAll()
////                                .antMatchers("/web/**").permitAll()
//                                .antMatchers("/api/games").permitAll()
//                                .antMatchers("/api/players").permitAll()
//                                .antMatchers("/api/**").hasAuthority("USER")
////                                .antMatchers("/api/players","/api/login","/api/logout").permitAll()
//                                .antMatchers("/rest").denyAll();
////                                .antMatchers("/web/games.html").permitAll()
////                                .antMatchers("/web/game.html?gp=*","/api/game_view/*").hasAuthority("USER")
////                                .antMatchers("/rest/**").permitAll()
////                                .anyRequest().denyAll();
//-------------------------------------------------------------------------------------------------------
                        http.formLogin()
                                .usernameParameter("name")
                                .passwordParameter("pwd")
                                .loginPage("/api/login");

                        http.logout().logoutUrl("/api/logout");
//--------------------------------------------------------------------------------------------------------

                        // turn off checking for CSRF tokens
                        http.csrf().disable();

                        // if user is not authenticated, just send an authentication failure response
                        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

                        // if login is successful, just clear the flags asking for authentication
                        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

                        // if login fails, just send an authentication failure response
                        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

                        // if logout is successful, just send a success response
                        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                }
                //--------------------------------------------------------------------------------------------------------------------------------

                private void clearAuthenticationAttributes(HttpServletRequest request) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                        }
                }
                }

