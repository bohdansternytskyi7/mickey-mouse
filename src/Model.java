import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Vector;

public class Model {
    private Stage stage;
    private int mickeyPosition;     //  1 - up-left, 2 - up-right, 3 - down-left, 4 - down-right
    private int score;
    private int previousEggPosition;
    private int eggPosition;
    private int health;
    private File catchFile, brokeFile;
    private Vector<Score> scoreList;

    public Model(Stage stage) {
        this.stage = stage;
        this.mickeyPosition = 1;
        this.score = 0;
        this.eggPosition = 0;
        this.previousEggPosition = 0;
        this.health = 4;

        this.scoreList = new Vector<Score>();

        catchFile = new File("./src/audio/catch.wav");
        brokeFile = new File("./src/audio/broke.wav");
    }

    public void readScores() {
        this.scoreList.removeAllElements();
        ObjectInputStream ois = null;

        try {
            File file = new File("./scores.txt");
            if(file.exists() && !file.isDirectory()) {
                FileInputStream fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                while(fis.available() > 0) {
                    scoreList.add((Score)ois.readObject());
                }
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveScore(Score totalScore) {
        ObjectOutputStream oos = null;
        File file = new File("./scores.txt");

        try {
            if(file.exists() && !file.isDirectory()) {
                oos = new ObjectOutputStream(new FileOutputStream("./scores.txt", true)) {
                    protected void writeStreamHeader() throws IOException {
                        reset();
                    }
                };
            } else {
                oos = new ObjectOutputStream(new FileOutputStream("./scores.txt", true));
            }
            oos.writeObject(totalScore);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector<Score> getScoreList() {
        return scoreList;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPreviousEggPosition(int previousEggPosition) {
        this.previousEggPosition = previousEggPosition;
    }

    public int getPreviousEggPosition() {
        return previousEggPosition;
    }

    public void playSound(boolean success) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(success ? catchFile : brokeFile);
            Clip clip  = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public boolean catchEgg() {
        if(mickeyPosition == eggPosition) {
            playSound(true);
            this.eggPosition = 0;
            this.score++;
            return true;
        }
        return false;
    }

    public boolean dropEgg() {
        if(eggPosition != 0) {
            playSound(false);
            this.health--;
            return true;
        }
        return false;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEggPosition(int position) {
        this.eggPosition = position;
    }

    public Stage getStage() {
        return stage;
    }

    public void setMickeyPosition(int mickeyPosition) {
        this.mickeyPosition = mickeyPosition;
    }

    public int getMickeyPosition() {
        return mickeyPosition;
    }

    public void switchToNewGameScene() {
        this.stage.setScene(new NewGameView().getScene(this));
        this.getStage().setWidth(this.getStage().getWidth() == 900 ? 901 : 900);
    }

    public void switchToHighScoresScene() {
        this.stage.setScene(new HighScoresView().getScene(this));
        this.getStage().setWidth(this.getStage().getWidth() == 900 ? 901 : 900);
    }

    public void switchToMenuScene() {
        this.stage.setScene(MenuView.getInstance(this));
        this.getStage().setWidth(this.getStage().getWidth() == 900 ? 901 : 900);
    }

    public void setBounds() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMaximized(true);
    }
}
