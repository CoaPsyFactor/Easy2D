package lib.gameactor;

import javafx.scene.input.KeyEvent;

/**
 * Interfejs koji "GameActor" <b>MORA</b> da implementira ukoliko treba da slusa dogadjaje na tastaturi
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public interface GameActorKeyboardListener {
    /**
     * Metoda koja se poziva kada se neki taster tastature pritisne
     *
     * @param currentActor "GameActor" preko kojeg se trenutno nalazi kursor
     * @param event        Dogadjaj koji se desio
     * @return <b>TRUE</b> ukoliko skripta treba da nastavi sa izvrsavanjem ostalih dogadjaja vezanih za tastaturu, u suprotnom <b>FALSE</b>
     */
    boolean onKeyPressed(GameActor currentActor, KeyEvent event);

    /**
     * Metoda koja se poziva kada se neki taster tastature pusti
     *
     * @param currentActor "GameActor" preko kojeg se trenutno nalazi kursor
     * @param event        Dogadjaj koji se desio
     * @return <b>TRUE</b> ukoliko skripta treba da nastavi sa izvrsavanjem ostalih dogadjaja vezanih za tastaturu, u suprotnom <b>FALSE</b>
     */
    boolean onKeyReleased(GameActor currentActor, KeyEvent event);
}
