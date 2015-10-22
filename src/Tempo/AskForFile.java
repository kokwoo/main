package Tempo;


import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.*;
public class AskForFile {
	static boolean value = false;
	static String fname = "";

	public static String display(String title,String message) {
		GridPane gp = new GridPane();
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(220);
		window.setMinHeight(180);
		TextField eventName = new TextField();
		eventName.setPromptText("Event Name");
		eventName.setMinWidth(205);
		Label label = new Label();
		label.setText("Enter a filename.");
		TextField startTime = new TextField();
				
		 Image image = new Image("/tempo.png");
		 System.out.println(System.getProperty("user.dir"));

         // simple displays ImageView the image as is
         ImageView iv1 = new ImageView();
         iv1.setFitWidth(35);
         iv1.setFitHeight(30);
         iv1.setImage(image);
       
		Button closeButton = new Button("Select");
		closeButton.setOnAction(e -> {
			System.out.println(eventName.getText());
			window.close();

		});

		
		
		
		
		gp.setPadding(new Insets(12,12,12,12));

		gp.setVgap(12);
		gp.add(label, 0, 1);
		gp.add(eventName, 0, 2);
		gp.add(closeButton, 0, 4);
		gp.add(iv1, 1, 4);
		//layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(gp);
		window.setScene(scene);
		window.showAndWait();
		String fileName = eventName.getText();	
		return fileName;

	}

	private static String formatDate(int month, int day, int year) {
		return Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
	}

}
