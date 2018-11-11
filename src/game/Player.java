package game;

import controllers.ComputerController;
import controllers.Controller;
import controllers.HumanController;

public class Player{

    private Controller controller;

    public Player(boolean human){
        if(human){
            this.controller = new HumanController();
        }else{
            this.controller = new ComputerController();

        }
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
