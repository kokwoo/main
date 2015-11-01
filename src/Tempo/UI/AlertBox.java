package Tempo.UI;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
public class AlertBox {
	static boolean value = false;
	static String fname = "";
	public static String display(String title,String message) {
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		TextField filename = new TextField ();
		filename.setPromptText("filename");
		
		Button closeButton = new Button("Enter");
		closeButton.setOnAction(e -> {
			fname = filename.getText().toString();
			window.close();
		});
		
		fname = filename.getText().toString();
		System.out.println("fname 1" + fname);
	
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().add(filename);
		layout.getChildren().add(closeButton);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		return fname;
	}

}
