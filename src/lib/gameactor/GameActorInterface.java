package lib.gameactor;

import controllers.GameLoopController.Layer;
import lib.sprite.Sprite;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public interface GameActorInterface {
    /**
     * Izvrsava radnju registrovanu za svaki "GameActor" objekat.
     *
     * @param delta Vreme izmedju trenutnog i prethodnog frejma prikazanu u milisekundama
     */
    void render(double delta);

    /**
     * Metoda se poziva prilikom starta nivoa
     */
    void onBegin();

    /**
     * Metoda se poziva prilikom uklanjanja "GameActor"-a sa nivoa, ili prilikom brisanja istog.
     */
    void onEnd();

    /**
     * Vraca sloj na kom "GameActor" treba da bude iscrtan
     *
     * @return Sloj (layer) na kome se "GameActor" nalazi
     */
    Layer getLayer();

    /**
     * Menja sloj (layer) na kome trenutni "GameActor" treba da bude iscrtan
     *
     * @param layer Sloj na kome "GameActor" treba biti iscrtan
     */
    void setLayer(Layer layer);

    /**
     * @param value <b>TRUE</b> ili <b>FALSE</b> u zavisnosti od toga da li treba da se pokrene i iscrta ili ne
     */
    void setIsActive(boolean value);

    /**
     * Proveravanje da li je trenutni "GameActor" aktivan ili ne
     *
     * @return <b>TRUE</b> ili <b>FALSE</b>
     */
    boolean isActive();

    /**
     * Vraca ime trenutnog "GameActora"
     *
     * @return Tekstualno ime trenutnog "GameActor"-a
     */
    String getTag();

    /**
     * Vraca trenutno vezan Sprite (sliku) za "GameActor"-a
     *
     * @return Slika (sprite) koji treba da bude iscrtan ili ciju animaciju menjamo/animiramo
     */
    Sprite getSprite();

    /**
     * Menja/uklanja sliku (Sprite) koji ce "GameActor" da koristi za iscrtavanje
     *
     * @param sprite Sprite (slika) koja ce da se koristi za trenutnog "GameActor"-a
     */
    void setSprite(Sprite sprite);
}
