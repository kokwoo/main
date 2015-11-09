package Tempo.GUI;
//@@author A0145073L
/**
 * 
 * ashish juneja
 * Tempo Gui Launcher.
 */
import Tempo.Logic.Calendar;
import Tempo.Logic.RequestHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TempoGui extends Application {
	private static final String ASK_FOR_FILE_MSG = "Enter a filename";
	private static final String WELCOME_TO_TEMPO_MSG = "Welcome to Tempo";
	private static final String TITLE = "Tempo";
	private static final String XML_VIEW = "temp.fxml";
	private static final String GUI_ARGS = " ";
	private static final Integer TEMPO_GUI_WIDTH = 750;
	private static final Integer TEMPO_GUI_HEIGHT = 607;
	public static void main(String args[]) {
		launch(GUI_ARGS);
	}
	
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		String fname =  AskForFile.display(ASK_FOR_FILE_MSG, WELCOME_TO_TEMPO_MSG);
		RequestHandler tempRH = RequestHandler.getInstance();
		System.out.println(tempRH.initialize(fname));

		Parent root = FXMLLoader.load(getClass().getResource(XML_VIEW));
		primaryStage.setTitle(TITLE);
		primaryStage.setScene(new Scene(root,TEMPO_GUI_WIDTH,TEMPO_GUI_HEIGHT));
		primaryStage.show();
		
	}
	
	
	
	
	
	
}
