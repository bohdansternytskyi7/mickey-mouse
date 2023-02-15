import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private Model model;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.model = new Model(stage);
        Image logo = new Image("img/logo.gif");
        stage.getIcons().add(logo);
        stage.setTitle("Mickey Mouse");
        stage.setWidth(900);
        stage.setHeight(640);
        stage.setResizable(false);
        stage.setScene(MenuView.getInstance(this.model));
        stage.show();
    }
}
