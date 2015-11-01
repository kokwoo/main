package Tempo.UI;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Tempo.Logic.Calendar;
import javafx.geometry.*;
public class AddEventWindow {
	static boolean value = false;
	static String fname = "";

	public static String display(String title,String message,Calendar calendar) {
		GridPane gp = new GridPane();
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(220);
		window.setMinHeight(320);
		TextField eventName = new TextField();
		eventName.setPromptText("Event Name");
		Label label = new Label();
		label.setText("Start Date");
		DatePicker startDate = new DatePicker();
		DatePicker endDate = new DatePicker();
		TextField startTime = new TextField();
		startTime.setPromptText("Event Start Time");
		TextField endTime = new TextField();
		endTime.setPromptText("Event End Time");
		
		 Image image = new Image("/tempo.png");
		 System.out.println(System.getProperty("user.dir"));

         // simple displays ImageView the image as is
         ImageView iv1 = new ImageView();
         iv1.setFitWidth(40);
         iv1.setFitHeight(40);
         iv1.setImage(image);
       
		Button closeButton = new Button("Add to Calendar");
		closeButton.setOnAction(e -> {
			value = true;
			//1/1/15 
			System.out.println("DATE " + startDate.getValue().getMonthValue() + "/" + startDate.getValue().getDayOfMonth() + "/" + startDate.getValue().getYear());
			int sMonth = startDate.getValue().getMonthValue();
			int sDay = startDate.getValue().getDayOfMonth();
			int sYear = startDate.getValue().getYear();

			int eMonth = endDate.getValue().getMonthValue();
			int eDay = endDate.getValue().getDayOfMonth();
			int eYear = endDate.getValue().getYear();

			String startDateFormatted = formatDate(sMonth,sDay,sYear);
			String endDateFormatted = formatDate(eMonth,eDay,eYear);

			//calendar.addEvent(eventName.getText(), startDateFormatted, startTime.getText(), endDateFormatted, endTime.getText());
			window.close();

		});

		Button noButton = new Button("Close the window");
		noButton.setOnAction(e -> {
			value = false;
			window.close();
		});
		startDate.setPromptText("Event Start Date");
		endDate.setPromptText("Event End Date");
		
		startDate.getValue();
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(12,12,12,12));
		layout.getChildren().addAll(eventName,
				//closeButton,
				//noButton,
				startDate,
				startTime,
				endDate,
				endTime,
				closeButton,
				iv1
				);
		gp.setPadding(new Insets(12,12,12,12));

		gp.setVgap(12);
		
		gp.add(eventName, 0, 0);
		gp.add(startDate, 0, 1);
		gp.add(startTime, 0, 2);
		gp.add(endDate, 0, 3);
		gp.add(endTime, 0, 4);
		gp.add(closeButton, 0, 5);
		gp.add(iv1, 1, 5);
		//layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(gp);
		window.setScene(scene);
		window.showAndWait();
		return "";	

	}

	private static String formatDate(int month, int day, int year) {
		return Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
	}

}
