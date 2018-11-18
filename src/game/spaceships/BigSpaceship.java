package game.spaceships;

import game.Spaceship;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BigSpaceship extends Spaceship {
	
	public BigSpaceship() {
		necessary_production = 500;
	}

	@Override
	public Spaceship getInstance() {
		return new BigSpaceship();
	}

	@Override
	public void draw(Group root) {
		Circle c = new Circle(pos.getX(),pos.getY(),10);
		c.setFill(Color.BLACK);
		root.getChildren().add(c);
	}
}
