package lib.gameactor;

/**
 * Interfejs koji se implementira na "GameActor"-a ukoliko zelimo da game engine racuna koliziju za njega
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public interface GameActorColliderInterface {
    /**
     * Metoda koja se poziva kada se trenutni "GameActor" "sudari" sa drugim
     *
     * @param target GameActor sa kojim se kolizija desila
     */
    void onCollision(GameActor target);

    /**
     * @return Definiciju oblika kolizije
     */
    GameActorCollider getCollider();
}
