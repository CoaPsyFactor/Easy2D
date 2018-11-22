package actors;

import controllers.GameLoopController.Layer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import lib.gameactor.GameActor;
import lib.sprite.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by azivanovic on 7/12/16.
 */
public class StaticMap extends GameActor {

    private Sprite mapSprite;

    private boolean isActive;

    @Override
    public void render(double delta) {
    }

    @Override
    public void onBegin() {
        System.out.println("Static Map Loaded");
    }

    @Override
    public void onEnd() {
        mapSprite = null;

        System.out.println("Static Map Unloaded");
    }

    @Override
    public Layer getLayer() {
        return Layer.BASE;
    }

    @Override
    public void setLayer(Layer layer) {
        return;
    }

    @Override
    public String getTag() {
        return "map";
    }

    public Sprite getSprite() {
        return mapSprite;
    }

    public void setSprite(Sprite sprite) {
        return;
    }

    public void setBody(HashMap<double[], ArrayList<Sprite>> body) {
        if (false == body.entrySet().iterator().hasNext()) {
            throw new RuntimeException("Body definition is empty.");
        }

        Map.Entry<double[], ArrayList<Sprite>> bodyEntry = body.entrySet().iterator().next();

        double[] bodyDimensions = bodyEntry.getKey();

        WritableImage image = new WritableImage((int) bodyDimensions[0], (int) bodyDimensions[1]);

        ArrayList<Sprite> sprites = bodyEntry.getValue();

        sprites.forEach(sprite -> {
            double[] offset = sprite.getOffset();

            ImageView imageView = sprite.getView();

            Image spriteImage = imageView.getImage();

            try {
                for (int pixelX = 0; pixelX < spriteImage.getWidth(); pixelX++) {
                    for (int pixelY = 0; pixelY < spriteImage.getHeight(); pixelY++) {

                        int pixelOffsetX = (int) offset[0] + pixelX;

                        int pixelOffsetY = (int) offset[1] + pixelY;

                        int argb = spriteImage.getPixelReader().getArgb(pixelX, pixelY);

                        if (argb < 0) {
                            image.getPixelWriter().setArgb(pixelOffsetX, pixelOffsetY, argb);
                        }
                    }
                }
            } catch (Exception exc) {
                System.out.println("Error adding body block to map " + exc.getMessage());
            }
        });

        sprites.clear();

        if (null == mapSprite) {
            mapSprite = new Sprite(image);
        }
    }
}
