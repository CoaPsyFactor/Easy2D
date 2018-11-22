package lib.gameactor;

import com.sun.media.jfxmediaimpl.MediaDisposer;
import controllers.GameLoopController;
import javafx.scene.canvas.Canvas;
import lib.sprite.Sprite;

/**
 * GameActor je glavna stvar koju dodajemo na nivo. GameActor moze da bude npr Igrac, NPC, Health Bar
 * Sve sto zelimo da prikazemo na mapi i da manipulisemo njime (poeramo, proveravamo za koliziju, klikcemo ...)
 */
public abstract class GameActor implements GameActorInterface, MediaDisposer.Disposable {

    /**
     * Vrednost koja predstavlja aktivno stanje trenutnog "GameActor"-a
     */
    protected boolean isActive;

    /**
     * Pozicija X
     */
    private double positionX = 0;

    /**
     * Pozicija Y
     */
    private double positionY = 0;

    /**
     * "flag" koji oznacava trenutnog "GameActor"-a za brisanje.
     * Ukoliko je ovo <br>TRUE</b> onda ce prilikom sledeceg frejma (frame) actor biti izbrisan
     */
    private boolean _delete = false;

    /**
     * @return X pozicija "GameActor"-a
     */
    public double getPositionX() {
        if (null != getSprite() && null != getSprite().getView()) {
            return getSprite().getView().getX();
        }

        return positionX;
    }

    /**
     * @return Y pozicija "GameActor"-a
     */
    public double getPositionY() {
        if (null != getSprite()) {
            return getSprite().getView().getY();
        }

        return positionY;
    }

    /**
     * Podesava novu poziciju trenutnog "GameActor"-a
     *
     * @param x X koordinata
     * @param y Y koordinata
     */
    public void setPosition(double x, double y) {
        positionX = x;

        positionY = y;

        if (null == getSprite()) {
            return;
        }

        getSprite().setPosition(x, y);

        if (this instanceof GameActorColliderInterface) {
            GameActorColliderInterface coll = (GameActorColliderInterface) this;

            if (null == coll.getCollider()) {
                return;
            }

            coll.getCollider().setPosition(positionX, positionY);
        }
    }

    /**
     * @param x faktor skaliranja po X osi
     * @param y faktor skaliranja po Y osi
     */
    public void setSale(double x, double y) {

        getSprite().getView().setFitWidth(getSprite().getView().getImage().getWidth() * x);

        getSprite().getView().setFitHeight(getSprite().getView().getImage().getHeight() * y);

        if (this instanceof GameActorColliderInterface && null != ((GameActorColliderInterface) this).getCollider()) {
            ((GameActorColliderInterface) this).getCollider().setWidth((int) getSprite().getView().getFitWidth());
            ((GameActorColliderInterface) this).getCollider().setHeight((int) getSprite().getView().getFitHeight());
        }
    }

    /**
     * Iscrtavanje trenutnog "GameActor"-a na zadato platno (Canvas)
     *
     * @param drawCanvas Canvas na kom treba da se iscrta
     */
    public void draw(Canvas drawCanvas) {
        if (false == isActive() || null == getSprite()) {
            return;
        }

        getSprite().draw(drawCanvas);
    }

    /**
     * Postavlja trenutnog "GameActor"-a na odredjenu XY poziciju
     *
     * @param x <b>X</b> koordinata
     * @param y <b>Y</b> koordinata
     */
    protected void moveTo(double x, double y) {
        positionX = x;

        positionY = y;

        getSprite().setPosition(x, y);

        if (this instanceof GameActorColliderInterface && ((GameActorColliderInterface) this).getCollider() != null) {
            ((GameActorColliderInterface) this).getCollider().setPosition(positionX, positionY);
        }
    }

    /**
     * Pomera trenutnog "GameActor"-a za XY piksela
     *
     * @param x broj piksela po X osi
     * @param y broj piksela po Y osi
     */
    protected void moveFor(double x, double y) {
        positionX = Double.sum(positionX, x);

        positionY = Double.sum(positionY, y);

        getSprite().setPosition(positionX, positionY);

        if (this instanceof GameActorColliderInterface && ((GameActorColliderInterface) this).getCollider() != null) {
            ((GameActorColliderInterface) this).getCollider().setPosition(positionX, positionY);
        }
    }

    /**
     * Stavlja "delete" "flag" na <b>TRUE</b> kako bi se "GameActor" obrisao u sledecem frejmu (frame)
     * <p>
     * Ovo radimo kako bi smo izbegli potencijale "greske" (exceptions) prilikom iscrtavanja koje se odvija na drugoj niti (thread)
     */
    public void delete() {
        _delete = true;
    }

    /**
     * Provera da li je "GameActor" rezervisan za brisanje.
     *
     * @return <b>boolean</b> vrednost koja govori da li je ili nije "GameActor" obrisan
     */
    public boolean isDeleted() {
        return _delete;
    }

    /**
     * @param delta Time between this and previous frame (ms)
     */
    @Override
    public void render(double delta) {
    }

    /**
     * Method gets called when actor spawns on level
     */
    @Override
    public void onBegin() {
    }

    /**
     * Method gets called when actor de-spawns from level
     */
    @Override
    public void onEnd() {
    }

    /**
     * @return Layer on which actor should be drawn
     */
    @Override
    public GameLoopController.Layer getLayer() {
        return GameLoopController.Layer.TOP;
    }

    /**
     * Sets new value of actor layer
     *
     * @param layer
     */
    @Override
    public void setLayer(GameLoopController.Layer layer) {
    }

    /**
     * Changes active state of actor
     *
     * @param value
     */
    @Override
    public void setIsActive(boolean value) {
        isActive = value;
    }

    /**
     * In game core, this method is used to deremine should actor render() method be called
     * and should actor be rendered on to canvas
     *
     * @return <b>boolean</b> value that holds active state of actor
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * @return <b>null</b> or Sprite that will be rendered on to canvas
     */
    @Override
    public Sprite getSprite() {
        return null;
    }

    /**
     * Changes render sprite for current actor
     *
     * @param sprite Sprite that will be used for rendering
     */
    @Override
    public void setSprite(Sprite sprite) {
    }

    /**
     * Disposes Sprite and all of its contents
     */
    @Override
    public void dispose() {
        if (null != getSprite()) {
            getSprite().dispose();
        }

        setIsActive(false);
    }

}
