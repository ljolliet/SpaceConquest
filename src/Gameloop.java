import game.Planet;
import game.Player;
import game.spaceships.LittleSpaceship;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import utils.Utils;

import java.util.ArrayList;

public class Gameloop extends AnimationTimer{
    //this class will update all graphics change
    //ex : ships' deplacement

    private Canvas canvas;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Planet> planets = new ArrayList<>();

    public Gameloop(Canvas canvas){
        this.canvas = canvas;
        init();
    }

    @Override
    public void handle(long now) {
        //code here is repeated
        actualizeProduction();
    }

    public void actualizeProduction(){
        for(Player pl : players){
            for(Planet p : pl.getController().getPlanets()){
                p.addProduction();
            }
        }
        //System.out.println(planets);
    }

    public void init(){
        initPlayers();
        initPlanets();
    }

    public void initPlayers(){
        players.add(new Player(true));
        //start at 1 because 0 is the human
        for(int i = 1; i < Utils.NB_PLAYER; i++){
            players.add(new Player(false));
        }
    }

    public void initPlanets(){
        initPlayerPlanets();
        initNeutralPlanets();
    }

    //give random position at a good distance
    public void initPlayerPlanets(){
        for(int i = 0; i < players.size(); i ++){
            Planet p = new Planet(i*50,100,false, 1, new LittleSpaceship());
            players.get(i).getController().getPlanets().add(p);
            p.setOwner(players.get(i).getController());
            planets.add(p);
        }
    }

    //give totally random position but not on another planet
    public void initNeutralPlanets(){

    }



}
