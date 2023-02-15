import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MenuView {
    private static Scene scene;

    public static Scene getInstance(Model model) {
        if(scene == null) {
            Button newGameBtn = new Button("New Game");
            Button highScoresBtn = new Button("High Scores");
            Button exitBtn = new Button("Exit");

            newGameBtn.setOnAction(actionEvent -> model.switchToNewGameScene());
            highScoresBtn.setOnAction(actionEvent -> model.switchToHighScoresScene());
            exitBtn.setOnAction(actionEvent -> System.exit(0));

            GridPane gridPane = new GridPane();
            gridPane.getStyleClass().add("grid-pane");
            gridPane.setVgap(10);
            gridPane.add(newGameBtn, 0, 0, 1, 1);
            gridPane.add(highScoresBtn, 0, 1, 1, 1);
            gridPane.add(exitBtn, 0, 2, 1, 1);

            scene = new Scene(gridPane);
            scene.getStylesheets().add("menu-style.css");
        }
        return scene;
    }
}
