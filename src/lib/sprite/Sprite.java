package lib.sprite;

import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Klasa koja predstavlja izgled jednog "GameActor"-a
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class Sprite implements MediaDisposer.Disposable {

    /**
     * Odstupanje od pocetka (gornji levi ugao) po X osi
     */
    private double _offsetX = 0.0f;

    /**
     * Odstupanje od pocetka (gornji levi ugao) po Y osi
     */
    private double _offsetY = 0.0f;

    /**
     * Objekat koji je zasluzan za poziciju i skaliranje Sprite-a
     */
    private ImageView imageView;

    /**
     * Animacija koja moze biti startovana
     */
    private SpriteAnimation spriteAnimation;

    /**
     * Brzina izvrsavanja animacije
     */
    private int animationSpeed = 100;

    /**
     * Identifikator koji sluzi da zbroji ukupno vreme izmedju trenutnog i prethodnog animacionog frejma (slike)
     */
    private long previousAnimationCount = 0;

    /**
     * Sprite je jedan deo "GameActor"-a koji nije obavezan, sluzi kako bi se "GameActor"-u mogla dodeliti slika koja se isrctava i kako bi se ta slika mogla animirati
     *
     * @param image Slika koja ce biti iscrtana
     */
    public Sprite(Image image) {
        imageView = getNewImageView(image);
    }

    /**
     * Sprite je jedan deo "GameActor"-a koji nije obavezan, sluzi kako bi se "GameActor"-u mogla dodeliti slika koja se isrctava i kako bi se ta slika mogla animirati
     *
     * @param image     Slika koja ce biti iscrtana
     * @param positionX X koordinata sprajta
     * @param positionY Y koordinata sprajta
     */
    public Sprite(Image image, double positionX, double positionY) {
        imageView = getNewImageView(image);

        imageView.setX(positionX);

        imageView.setY(positionY);
    }

    /**
     * Sprite je jedan deo "GameActor"-a koji nije obavezan, sluzi kako bi se "GameActor"-u mogla dodeliti slika koja se isrctava i kako bi se ta slika mogla animirati
     *
     * @param image     Slika koja ce biti iscrtana
     * @param positionX X koordinata sprajta
     * @param positionY Y koordinata sprajta
     * @param offsetX   Odstupanje od pocetka (gore levo) po X osi
     * @param offsetY   Odstupanje od pocetka (gore levo) po Y osi
     */
    public Sprite(Image image, double positionX, double positionY, double offsetX, double offsetY) {
        imageView = getNewImageView(image);

        imageView.setX(positionX + offsetX);

        imageView.setY(positionY + offsetY);

        _offsetX = offsetX;

        _offsetY = offsetY;
    }

    /**
     * Resetuje animaciju na pocetak
     */
    public void clearAnimation() {
        if (null == spriteAnimation) {
            return;
        }

        spriteAnimation.resetAnimation();

        imageView.setImage(spriteAnimation.getCurrentFrame());
    }

    /**
     * Izvrsava animaciju - ukoliko je dovoljno milisekundi prosli menja sliku "Sprite"-a na sledecu animacioni frejm
     */
    public void animate() {
        if (null == spriteAnimation) {
            return;
        }

        long currentAnimationCount = System.currentTimeMillis();

        if ((currentAnimationCount - previousAnimationCount) >= animationSpeed) {
            imageView.setImage(spriteAnimation.getNextFrame());

            previousAnimationCount = currentAnimationCount;
        }
    }

    /**
     * Iscrtava trenutno stanje "Sprite"-a na njegovu poziciju
     *
     * @param drawCanvas Platno ("Canvas") na kom "Sprite" treba da se iscrta
     */
    public void draw(Canvas drawCanvas) {
        drawCanvas.getGraphicsContext2D().drawImage(
                imageView.getImage(),
                imageView.getX() + _offsetX,
                imageView.getY() + _offsetY,
                imageView.getFitWidth(),
                imageView.getFitHeight()
        );
    }

    /**
     * Vraca trenutni animacioni set koji se koristi
     *
     * @return Trenutni animacioni set koji se koristi
     */
    public SpriteAnimation getSpriteAnimation() {
        return spriteAnimation;
    }

    public void setSpriteAnimation(SpriteAnimation animation) {
        spriteAnimation = animation;

        if (null != spriteAnimation) {
            spriteAnimation.resetAnimation();
        }
    }

    /**
     * Podesava vreme izmedju dva frejma u animaciji.
     * Vreme je u milisekundama.
     *
     * @param speed Vreme koje predstavlja na koliko milisekundi animacija treba da promeni frejm
     */
    public void setAnimationSpeed(int speed) {
        animationSpeed = speed;
    }

    public Sprite setPosition(double x, double y) {

        imageView.setX(x);

        imageView.setY(y);

        return this;
    }

    /**
     * Podesava odstupanje po X i Y osi za trenutni "Sprite"
     *
     * @param x Odstojanje po X osi
     * @param y Odstojanje po Y osi
     * @return Trenutni "Sprite"
     */
    public Sprite setOffset(double x, double y) {
        _offsetX = x;

        _offsetY = y;

        return this;
    }

    /**
     * Vraca niz tipa <b>double</b> koji u sebi sadrzi informacije o trenutnom odstupanju "Sprite"-a
     * Prvi (0) clan niza predstavlja odstupanje po X osi, drugi (1) clan nzia predstavlja odstupanje po Y osi
     *
     * @return <b>double</b> niz
     */
    public double[] getOffset() {
        double[] offset = new double[2];

        offset[0] = _offsetX;

        offset[1] = _offsetY;

        return offset;
    }

    /**
     * Vraca pogled koji predstavlja sliku na X,Y poziciji sa sirinom i visinom.
     * Ovaj pogled se koristi kako bi engine znao gde i koliko da iscrta trenutni "Sprite"
     *
     * @return Pogled slike koja se iscrtava na platnu ("Canvas")
     */
    public ImageView getView() {
        return imageView;
    }

    @Override
    public void dispose() {
        if (null != imageView && null != imageView.getImage()) {
            imageView.getImage().cancel();
        }

        imageView = null;

        if (null != spriteAnimation) {
            spriteAnimation.dispose();
        }
    }

    /**
     * Pravi novi pogled sa zadatom slikom
     *
     * @param image Slika koja ce da se koristi u novom pogledu.
     * @return Novo napravljeni pogled.
     */
    private ImageView getNewImageView(Image image) {
        ImageView imageView = new ImageView();

        imageView.setPickOnBounds(true);

        imageView.setFitWidth(image.getWidth());

        imageView.setFitHeight(image.getHeight());

        imageView.setImage(image);

        imageView.setScaleX(1);

        imageView.setScaleY(1);

        return imageView;
    }
}
