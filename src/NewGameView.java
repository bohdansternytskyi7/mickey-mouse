import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class NewGameView {
    private Scene scene;
    private int gameSpeed;
    private int animationSpeed;

    public Scene getScene(Model model) {
        this.gameSpeed = 1000;
        this.animationSpeed = 900;
        model.setMickeyPosition(1);
        model.setScore(0);
        model.setEggPosition(0);
        model.setHealth(4);

        Alert gameInfo = new Alert(Alert.AlertType.INFORMATION);
        gameInfo.setHeight(200);
        gameInfo.setTitle("Rules");
        gameInfo.setHeaderText(null);
        gameInfo.setContentText("To move Mickey use the following keys: Q, E, A, D.\n" +
                "Catch as much eggs as you can. You have only 4 points of " +
                "health, with every egg drop you lose one point of health.");
        gameInfo.showAndWait();

        Image mickeyLeft = new Image("./img/mickey.png"),
            mickeyLeftDown = new Image("./img/mickey-down.png"),
            mickeyRight = new Image("./img/mickey-right.png"),
            mickeyRightDown = new Image("./img/mickey-down-right.png"),
            eggImage = new Image("./img/egg.png"),
            heartImage = new Image("./img/heart.png");

        ImageView mickey = new ImageView(mickeyLeft),
                egg = new ImageView(eggImage);
        egg.getStyleClass().add("egg");

        mickey.setX(292);
        mickey.setY(280);

        Pane root = new Pane();
        root.getChildren().add(mickey);
        root.getChildren().add(egg);

        ImageView[] hearts = new ImageView[] {
                new ImageView(heartImage),
                new ImageView(heartImage),
                new ImageView(heartImage),
                new ImageView(heartImage)
        };

        hearts[1].setX(32);
        hearts[2].setX(64);
        hearts[3].setX(96);

        Group heartGroup = new Group(hearts[0], hearts[1], hearts[2], hearts[3]);
        root.getChildren().add(heartGroup);
        heartGroup.setTranslateX(200);
        heartGroup.setTranslateY(20);

        Label label = new Label("0");
        label.setTranslateX(410);
        label.setScaleX(2);
        label.setScaleY(2);
        root.getChildren().add(label);

        TranslateTransition fallDown = new TranslateTransition();
        fallDown.setDuration(Duration.millis(this.animationSpeed));
        RotateTransition eggRotate = new RotateTransition();
        eggRotate.setDuration(Duration.millis(this.animationSpeed));
        fallDown.setNode(egg);
        eggRotate.setNode(egg);

        Timer game = new Timer(this.gameSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(model.dropEgg()) {
                            if(model.getHealth() > 2) {
                                heartGroup.getChildren().remove(hearts[3]);
                            } else if (model.getHealth() > 1) {
                                heartGroup.getChildren().remove(hearts[2]);
                            } else if (model.getHealth() > 0) {
                                heartGroup.getChildren().remove(hearts[1]);
                            } else {
                                heartGroup.getChildren().remove(hearts[0]);

//                                    stop the game
                                ((Timer)e.getSource()).stop();

                                TextInputDialog dialog = new TextInputDialog();
                                dialog.setTitle("Game Over");
                                dialog.setHeaderText("Your score is: " + model.getScore());
                                dialog.setContentText("Please enter your nickname:");
                                Optional<String> nickname = dialog.showAndWait();
                                nickname.ifPresent(name -> model.saveScore(new Score(model.getScore(), name)));

                                model.switchToMenuScene();
                            }
                        }

                        int eggPosition = (int)(Math.random() * 4.0) + 1;

                        if(eggPosition == model.getPreviousEggPosition())
                            if(eggPosition == 4)
                                eggPosition = 1;
                            else
                                eggPosition += 1;

                        egg.getStyleClass().remove("hide");
                        egg.setTranslateX(0);
                        egg.setTranslateY(0);

                        switch (eggPosition) {
                            case 1:
                                egg.setX(120);
                                egg.setY(210);
                                fallDown.setByX(100);
                                fallDown.setByY(60);
                                eggRotate.setByAngle(720);
                                break;
                            case 2:
                                egg.setX(780);
                                egg.setY(190);
                                fallDown.setByX(-100);
                                fallDown.setByY(60);
                                eggRotate.setByAngle(-720);
                                break;
                            case 3:
                                egg.setX(120);
                                egg.setY(360);
                                fallDown.setByX(100);
                                fallDown.setByY(60);
                                eggRotate.setByAngle(720);
                                break;
                            case 4:
                                egg.setX(780);
                                egg.setY(340);
                                fallDown.setByX(-100);
                                fallDown.setByY(60);
                                eggRotate.setByAngle(-720);
                                break;
                        }

                        fallDown.play();
                        eggRotate.play();
                        model.setPreviousEggPosition(eggPosition);
                        model.setEggPosition(eggPosition);
                        if(model.catchEgg()) {
                            egg.getStyleClass().add("hide");
                            label.setText(String.valueOf(model.getScore()));
                        }
                    }
                });
            }
        });
        game.start();

        this.scene = new Scene(root);
        this.scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCharacter().equals("q") || keyEvent.getCharacter().equals("Q")) {
                    mickey.setImage(mickeyLeft);
                    model.setMickeyPosition(1);
                }
                else if(keyEvent.getCharacter().equals("e") || keyEvent.getCharacter().equals("E")) {
                    mickey.setImage(mickeyRight);
                    model.setMickeyPosition(2);
                }
                else if(keyEvent.getCharacter().equals("a") || keyEvent.getCharacter().equals("A")) {
                    mickey.setImage(mickeyLeftDown);
                    model.setMickeyPosition(3);
                }
                else if(keyEvent.getCharacter().equals("d") || keyEvent.getCharacter().equals("D")) {
                    mickey.setImage(mickeyRightDown);
                    model.setMickeyPosition(4);
                } else if (keyEvent.isShiftDown() && keyEvent.isControlDown() &&
                        (keyEvent.getCharacter().equals("q") || keyEvent.getCharacter().equals("Q"))){
//                        model.switchToMenuScene();
                }

                if(model.catchEgg()) {
                    egg.getStyleClass().add("hide");
                    label.setText(String.valueOf(model.getScore()));
                }
            }
        });

        new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameSpeed -= (int)(gameSpeed * 0.05);
                animationSpeed -= (int)(animationSpeed * 0.05);
                game.setDelay(gameSpeed);
                fallDown.setDuration(Duration.millis(animationSpeed));
                eggRotate.setDuration(Duration.millis(animationSpeed));
            }
        }).start();

        scene.getStylesheets().add("new-game-style.css");
        return scene;
    }
}
