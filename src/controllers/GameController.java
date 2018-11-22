package controllers;

import java.util.HashMap;

/**
 * Ovo sluzi kako bi omogucilo i drzalo "signleton" instance ka ostalim kontrolerima
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public abstract class GameController {
    /**
     * Lista instanciranih kontrolera
     */
    private static final HashMap<String, GameController> INSTANCES = new HashMap<>();

    /**
     * Glavni kontroller toka igrice
     */
    public static final CoreGameController coreController = (CoreGameController) GameController.getSharedInstance(CoreGameController.class.getName());

    /**
     * Game loop kontroler koji izvrsava sve render i draw metode u "GameActor"-ima
     */
    public static final GameLoopController loopController = (GameLoopController) GameController.getSharedInstance(GameLoopController.class.getName());

    /**
     * Kontroler zvuka
     */
    public static final SoundController soundController = (SoundController) GameController.getSharedInstance(SoundController.class.getName());

    /**
     * Kontroler konekcije na bazu
     */
    public static final DatabaseController databaseController = (DatabaseController) GameController.getSharedInstance(DatabaseController.class.getName());

    /**
     * Vraca "singleton" instancu kontrolera
     *
     * @return Instanca zeljenog kontrolera
     */
    public static GameController getSharedInstance(String callClassName) {
        if (false == INSTANCES.containsKey(callClassName)) {
            try {
                INSTANCES.put(callClassName, (GameController) Class.forName(callClassName).newInstance());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return INSTANCES.containsKey(callClassName) ? INSTANCES.get(callClassName) : null;
    }
}