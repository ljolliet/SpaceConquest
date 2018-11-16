package graphics;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	//class from https://gamedevelopment.tutsplus.com/tutorials/introduction-to-javafx-for-game-development--cms-23835
	private Image image;
    private double positionX;
    private double positionY;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
 
    // ...
    // methods omitted for brevity
    // ...
 
    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
 
    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }
 
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }
 
    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
}
