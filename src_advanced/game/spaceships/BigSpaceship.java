package game.spaceships;

import game.Spaceship;
import javafx.scene.paint.Color;
import utils.Utils;

public class BigSpaceship extends Spaceship {

    public BigSpaceship(Color color) {
        super(color);
        necessary_production = Utils.BIG_SPACESHIP_NEC_PROD;
        damage = Utils.BIG_SPACESHIP_DAMAGE;
        length = Utils.BIG_SPACESHIP_LENGTH;
        speed = Utils.BIG_SPACESHIP_SPEED;
        this.direction = Utils.SPACESHIP_DIRECTION;
    }

    @Override
    public Spaceship getInstance() {
        return new BigSpaceship(color);
    }


}
