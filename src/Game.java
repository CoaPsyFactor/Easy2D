import controllers.CoreGameController;
import controllers.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
import levels.campaign.IntroLevel;

/**
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        CoreGameController gameController = (CoreGameController) GameController.getSharedInstance(CoreGameController.class.getName());

        gameController.loadLevel(new IntroLevel());

        gameController.setStage(primaryStage);

        gameController.start();
    }
}
