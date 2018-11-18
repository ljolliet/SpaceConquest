package game.spaceships;

import game.Spaceship;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LittleSpaceship extends Spaceship {

    public LittleSpaceship(){
        necessary_production = 15;
    }

    @Override
    public Spaceship getInstance() {
        return new LittleSpaceship();
    }

    @Override
    public void draw(Group root) {
        Circle c = new Circle(pos[0],pos[1],5);
        c.setFill(Color.BLACK);
        root.getChildren().add(c);
    }
}
