package Tempo.UI;

import Tempo.Logic.Calendar;
import Tempo.Logic.RequestHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TempoGui extends Application {

	public static void main(String args[]) {
		launch("a");
		
	//	RequestHandler tempRH = RequestHandler.getInstance();
	//	System.out.println(tempRH.initialize("gom"));
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		String fname = 	"l";			
		//String fname =  AskForFile.display("Enter a filename", "Welcome to Tempo!");
		
		
		RequestHandler tempRH = RequestHandler.getInstance();
		System.out.println(tempRH.initialize(fname));
		Calendar calendar = tempRH.getCalendar();
		
	//	Controller a = new Controller();
		
		
		
	
		
		
		

		
		
		
		
		
		
		
		Parent root = FXMLLoader.load(getClass().getResource("temp.fxml"));
		primaryStage.setTitle("Tempo");
		primaryStage.setScene(new Scene(root,750,607));
		primaryStage.show();
	//	a.create(1,1,"a");
		
	}
	
	
	
	
	
	
}
