package lib.gameactor;

import javafx.geometry.Rectangle2D;

/**
 * Objekat koji se koristi kako bi game engine znao gde su granice "GameActor"-a, i kako bi omogucio koliziju sa ostalim actorima na nivou
 * <p>
 * Kolizija je "Box Based" sto znaci da svaki "GameActor" koji koristi koliziju je detektovan kao pravugaonik sa X i Y sirinom i visinom
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class GameActorCollider {

    /**
     * Sirina actora
     */
    private int width;

    /**
     * Visina actora
     */
    private int height;

    /**
     * Odstupanje pravugaone kolizije od slike (Sprite) po X osi
     */
    private int offsetX;

    /**
     * Odstupanje pravugaone kolizije od slike (Sprite) po Y osi
     */
    private int offsetY;

    /**
     * X koordinata trenutnog pravouganika kolizije
     */
    private double x;

    /**
     * X koordinata trenutnog pravouganika kolizije
     */
    private double y;

    /**
     * Konstruktor klase, kome se dodeljuju neophodne vrednosti kao sto su sirina, visina i odstupanja
     *
     * @param _width   Sirina pravugaonika kolizije
     * @param _height  Visina pravugaonika kolizije
     * @param _offsetX Odstupanje po X osi
     * @param _offsetY Odstupanje po Y osi
     */
    public GameActorCollider(int _width, int _height, int _offsetX, int _offsetY) {
        width = _width;

        height = _height;

        offsetX = _offsetX;

        offsetY = _offsetY;

        x = 0;

        y = 0;
    }

    /**
     * Vraca pocetnu X koordinatu sa odstupanjem po X osi
     *
     * @return X koordinata sa odstupanjem po X osi
     */
    public double getX() {
        return x + offsetX;
    }

    /**
     * Vraca pocetnu Y koordinatu sa odstupanjem po Y osi
     *
     * @return Y koordinata sa odstupanjem po Y osi
     */
    public double getY() {
        return y + offsetY;
    }

    /**
     * Vraca krajnju X koordinatu sa odstupanjem po X osi
     *
     * @return X koordinata sa odstupanjem po X osi
     */
    public double getMaxX() {
        return getX() + width;
    }


    /**
     * Vraca krajnju Y koordinatu sa odstupanjem po Y osi
     *
     * @return Y koordinata sa odstupanjem po Y osi
     */
    public double getMaxY() {
        return getY() + height;
    }

    /**
     * Vraca sirinu trenutnog pravougaonika kolizije
     *
     * @return Sirina kolizije
     */
    public int getWidth() {
        return width;
    }

    /**
     * Menja sirninu pravouganika kolizije
     */
    public void setWidth(int value) {
        width = value;
    }

    /**
     * Vraca visinu trenutnog pravougaonika kolizije
     *
     * @return Visina kolizije
     */
    public int getHeight() {
        return height;
    }

    /**
     * Menja visinu pravouganika kolizije
     */
    public void setHeight(int value) {
        width = value;
    }

    /**
     * Proverava da li je trenutni "GameActor" u koliziji sa zadatim "target" (da li se dodiruju)
     *
     * @param target "GameActor" sa koji proveravamo da li je u koliziji
     * @return <b>TRUE</b> if colliding with given "GameActor", <b>FALSE</b> otherwise
     */
    public boolean inCollision(GameActorCollider target) {
        if (target == null) {
            return false;
        }

        Rectangle2D thisCollider = new Rectangle2D(getX(), getY(), getWidth(), getHeight());

        Rectangle2D targetCollider = new Rectangle2D(target.getX(), target.getY(), target.getWidth(), target.getHeight());

        return thisCollider.intersects(targetCollider);
    }

    /**
     * Podesava X i Y koordinate trenutne kolizije
     *
     * @param _x X koordinata
     * @param _y Y koordinata
     */
    public void setPosition(double _x, double _y) {
        x = _x;
        y = _y;
    }
}
