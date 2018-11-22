package lib.gameactor;

import javafx.scene.input.MouseEvent;

/**
 * Interfejs koji "GameActor" <b>MORA</b> da implementira ukoliko zelimo da game engine slusa dogadjaje misa na trenutnom "GameActor"-u
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public interface GameActorMouseListener {
    /**
     * Metoda se poziva prilikom pritiska bilo kog dugmeta na misu (ukoliko se mis nalazi preko trenutnog "GameActor"-a)
     *
     * @param event Dogadjaj koji se desio
     * @return <b>TRUE</b> ukoliko skripta treba da nastavi sa izvrsavanjem ostalih dogadjaja vezanih za tastaturu, u suprotnom <b>FALSE</b>
     */
    boolean onMousePressed(MouseEvent event);

    /**
     * Metoda se poziva prilikom pustanja bilo kog dugmeta na misu (ukoliko se mis nalazi preko trenutnog "GameActor"-a)
     *
     * @param event Dogadjaj koji se desio
     * @return <b>TRUE</b> ukoliko skripta treba da nastavi sa izvrsavanjem ostalih dogadjaja vezanih za tastaturu, u suprotnom <b>FALSE</b>
     */
    boolean onMouseReleased(MouseEvent event);

    /**
     * Metoda se poziva prilikom klika na bilo koje dugme na misu (ukoliko se mis nalazi preko trenutnog "GameActor"-a)
     *
     * @param event Dogadjaj koji se desio
     * @return <b>TRUE</b> ukoliko skripta treba da nastavi sa izvrsavanjem ostalih dogadjaja vezanih za tastaturu, u suprotnom <b>FALSE</b>
     */
    boolean onMouseClicked(MouseEvent event);

    /**
     * Metoda koja se poziva kada se predje misem preko trenutnog "GameActor"-a
     *
     * @param event Dogadjas koji se desio
     * @return <b>TRUE</b> ukoliko skripta treba da nastavi sa izvrsavanjem ostalih dogadjaja vezanih za tastaturu, u suprotnom <b>FALSE</b>
     */
    boolean onMouseMoved(MouseEvent event);
}
