package Tempo.UI.GUI;
//@@author A0145073L
/**
 * 
 * Dialog to ask for file.
 */

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
	private static final Integer PADDING_GRIDVIEW = 12;
	private static final String PROMPT_TEXT = "Enter a filename : ";
	private static final String EVENT_NAME = "Event name";
	private static final String IMG_DIR = "/tempo.png";
	private static final String USR_DIR = "user.dir";
	private static final Integer IMG_WIDTH = 35;
	private static final Integer IMG_HEIGHT = 30;
	private static final Integer ZERO = 0;
	private static final Integer TWELVE = 12;
	private static final Integer ONE = 1;
	private static final Integer WINDOW_MIN_WIDTH = 220;
	private static final Integer WINDOW_MIN_HEIGHT = 180;
	private static final Integer EVENT_NAME_WIDTH = 205;
	private static final Integer GRIDVIEW_VERTICAL_GAP = 12;
	private static final Integer FOUR = 4;
	private static final Integer TWO = 2;
	private static final String CLOSE_TEXT = "OK";
	public static String display(String title,String message) {
		GridPane gp = new GridPane();
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(WINDOW_MIN_WIDTH);
		window.setMinHeight(WINDOW_MIN_HEIGHT);
		TextField eventName = new TextField();
		eventName.setPromptText(EVENT_NAME);
		eventName.setMinWidth(EVENT_NAME_WIDTH);
		Label label = new Label();
		label.setText(PROMPT_TEXT);
		TextField startTime = new TextField();
				
		// Image image = new Image(IMG_DIR);
		 System.out.println(System.getProperty(USR_DIR));

         // simple displays ImageView the image as is
       //  ImageView iv1 = new ImageView();
        // iv1.setFitWidth(IMG_WIDTH);
        // iv1.setFitHeight(IMG_HEIGHT);
        //
		 //iv1.setImage(image);
       
		Button closeButton = new Button(CLOSE_TEXT);
		closeButton.setOnAction(e -> {
			System.out.println(eventName.getText());
			window.close();

		});

		
		
		
		
		
		
		gp.setPadding(new Insets(PADDING_GRIDVIEW,PADDING_GRIDVIEW,PADDING_GRIDVIEW,PADDING_GRIDVIEW));

		gp.setVgap(GRIDVIEW_VERTICAL_GAP);
		gp.add(label, ZERO, ONE);
		gp.add(eventName, ZERO, TWO);
		gp.add(closeButton, ZERO, FOUR);
		//gp.add(iv1, ONE, FOUR);

		Scene scene = new Scene(gp);
		window.setScene(scene);
		window.showAndWait();
		String fileName = eventName.getText();	
		return fileName;

	}

	
}
