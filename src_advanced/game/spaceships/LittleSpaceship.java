package game.spaceships;

import game.Spaceship;
import javafx.scene.paint.Color;
import utils.Utils;

public class LittleSpaceship extends Spaceship {

    public LittleSpaceship(Color color) {
        super(color);
        necessary_production = Utils.LITTLE_SPACESHIP_NEC_PROD;
        damage = Utils.LITTLE_SPACESHIP_DAMAGE;
        length = Utils.LITTLE_SPACESHIP_LENGTH;
        speed = Utils.LITTLE_SPACESHIP_SPEED;
        this.direction = Utils.SPACESHIP_DIRECTION;
    }

    /**
     * @return A new instance of this Spaceship.
     */
    @Override
    public Spaceship getInstance() {
        return new LittleSpaceship(color);
    }


}