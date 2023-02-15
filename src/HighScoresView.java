import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class HighScoresView {
    private Scene scene;

    public Scene getScene(Model model) {
        model.readScores();

        ListView<String> scores = new ListView<>();

        ArrayList<String> scoresToString = new ArrayList<>();
        for(Score s: model.getScoreList())
            scoresToString.add(s.toString());

        scores.getItems().addAll(scoresToString);
        scores.setMinWidth(900);
        scores.setMaxHeight(800);

        Pane pane = new Pane(scores);
        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.switchToMenuScene();
            }
        });
        back.setTranslateX(780);
        back.setTranslateY(560);

        pane.getChildren().add(back);

        scene = new Scene(pane);

        scene.getStylesheets().add("high-scores-style.css");

        return scene;
    }
}
