package billiards.viewer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Console {

    private final VBox root = new VBox();
    private final Stage stage = new Stage();
    private final Scene scene = new Scene(root);

    private final TextArea text = new TextArea();
    private final Label label = new Label("Output of check:");

	public Console(final String windowTitle, final String content) {
        stage.setScene(scene);
        stage.setTitle(windowTitle);
        stage.setOnCloseRequest(event -> stage.close());

		text.setText(content);
		text.setPrefSize(910, 575);
		text.setEditable(false);

		root.setSpacing(10);
		root.setPadding(new Insets(10));
		root.getChildren().addAll(label, text);
	}

    public void close() {
        stage.close();
    }

    public void show() {
        stage.show();
    }
}
