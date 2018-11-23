package game.spaceships;

import game.Spaceship;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class BigSpaceship extends Spaceship {
	
	public BigSpaceship(Color color) {
		super(color);

		necessary_production = 500;
	}

	@Override
	public Spaceship getInstance() {
		return new BigSpaceship(color);
	}

	@Override
	public void draw(Group root, boolean selected) {
		Circle c = new Circle(pos.getX(),pos.getY(),10);
		c.setFill(Color.BLACK);
		root.getChildren().add(c);
	}

	@Override
	public Polygon initPolygon() {
		return null;
	}
}
