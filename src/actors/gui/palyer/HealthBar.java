package actors.gui.palyer;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class HealthBar extends AttachGUI {

    private WritableImage image = new WritableImage(64, 12);

    private Sprite sprite;

    private GameActor parent;

    public HealthBar(GameActor actor) {
        super(0, 64, actor);
    }

    public void updateHealthBar(double health, double maxHealth) {
        double width = (((health / maxHealth) * 100) / 100) * 64;

        for (int x = 0; x < 64; x++) {
            for (int y = 0; y < 12; y++) {
                if (((x >= 0 && x < 4) || (x >= 60 && x <= 64)) || ((y >= 0 && y <= 4) || (y >= 8 && y <= 12))) {
                    image.getPixelWriter().setColor(x, y, Color.BLACK);

                    continue;
                }

                if (x < width) {
                    image.getPixelWriter().setColor(x, y, Color.GREEN);
                } else {
                    image.getPixelWriter().setColor(x, y, Color.RED);
                }
            }
        }

        if (null == sprite) {
            sprite = new Sprite(image);
        }
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getTag() {
        return "gui_healthbar";
    }
}
