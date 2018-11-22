package lib.sprite;

import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * Klasa koja predstavlja animaciju jednog "Sprite"-a
 * <p>
 * Ovo je skup razlicitih slicica koje predstavljaju animaciju jednog "Sprite"-a
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class SpriteAnimation implements MediaDisposer.Disposable {

    /**
     * Trenutni frejm animacije
     */
    private int currentFrame = 0;

    /**
     * Svi frejmovi trenutne animacije
     */
    private ArrayList<Image> animationFrames = new ArrayList<>();

    /**
     * Pravi novu "Sprite" animaciju
     *
     * @param sheet  "SpriteSheet" od kojeg se pravi animacija
     * @param frames Odstupanja na "SpriteSheet" koja predstavljaju po jedan frejm animacije
     */
    public SpriteAnimation(SpriteSheet sheet, int[] frames) {
        construct(sheet, frames, true);
    }

    private void construct(SpriteSheet sheet, int[] frames, boolean shared) {
        for (int i = 0; i < frames.length; i++) {
            if (shared) {
                animationFrames.add(sheet.getSprite(frames[i]).getView().getImage());

                continue;
            }

            animationFrames.add(sheet.getNewSprite(frames[i]).getView().getImage());
        }
    }

    /**
     * Vraca sledeci frejm animacije
     *
     * @return Sledeci frejm
     */
    public Image getNextFrame() {

        if (null == animationFrames) {
            return null;
        }

        if (currentFrame == animationFrames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame++;
        }

        return animationFrames.get(currentFrame);
    }

    /**
     * Vraca trenutni frejm animacije
     *
     * @return Trenutni frejm
     */
    public Image getCurrentFrame() {
        if (null == animationFrames) {
            return null;
        }

        return animationFrames.get(currentFrame);
    }

    /**
     *
     * Promena frejma animacije
     *
     * @param index Odstojanje frejma u sheet-u
     */
    public void setCurrentFrame(int index) {
        currentFrame = index;
    }

    /**
     *
     * Vraca trenutni broj frejma animacije
     *
     * @return Trenutni frejm animacije
     */
    public int getCurrentFrameIndex() {
        return currentFrame;
    }

    /**
     * Proverava da li je trenutni frejm poslednji frejm u animaciji
     *
     * @return <b>TRUE</b> ukoliko je poslednji frejm, u suprotnom <b>FALSE</b>
     */
    public boolean isLastFrame() {
        return currentFrame == animationFrames.size() - 1;
    }

    /**
     * Postavlja trenutni frejm na pocetni
     */
    public void resetAnimation() {
        currentFrame = 0;
    }

    @Override
    public void dispose() {
        animationFrames.forEach(image -> image.cancel());

        animationFrames.clear();
    }
}
