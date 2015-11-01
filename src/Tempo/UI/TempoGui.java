package Tempo.UI;


import java.util.ArrayLis

import Tempo.Logic.Calendar;
import Tempo.Logic.tempRequestHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RunTempo extends Application {
	private static final double MAIN_PADDING_TOP = 12;
	private static final double MAIN_PADDING_RIGHT = 12;
	private static final double MAIN_PADDING_MAIN_PADDING_BOTTOM = 12;
	private static final double MAIN_PADDING_LEFT = 12;
	private static final String SPACE = " ";
	
	public void main(Stage primaryStage) throws Exception {
		// TODO Auto-generated method 
		launch("a");
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage window = primaryStage;
		setInitialStageDimensions(window,550,610);
		String fname = getFileFromUser();
		
		tempRequestHandler tempRH = tempRequestHandler.getInstance();
		System.out.println(tempRH.initialize(fname));
		Calendar calendar = tempRH.getCalendar();
		GridPane mainLayout = new GridPane();
		mainLayout.setPadding(new Insets(MAIN_PADDING_TOP,MAIN_PADDING_RIGHT,MAIN_PADDING_MAIN_PADDING_BOTTOM,MAIN_PADDING_LEFT));
		Scene mainScene = new Scene(mainLayout);
		
		
		
		TreeItem<String> root = new TreeItem<>();
		TreeView<String> tree = new TreeView<>(root);
		tree.setPrefWidth(450);
		tree.setShowRoot(false);
		
		TreeItem<String> eventTree = makeBranch("Events",root);
		TreeItem<String> taskTree = makeBranch("Tasks",root);
		TreeItem<String> floatingTaskTree = makeBranch("Floating Tasks",root);
		TreeItem<String> searchResults = makeBranch("Search Results",root);
		
		
		ComboBox run = new ComboBox();
		run.setPrefWidth(550);
		run.setPromptText("Give me a command");
		run.setEditable(true);
		
		
		run.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
		
			public void handle(KeyEvent ke) {
				
				if (ke.getCode() == KeyCode.ENTER) {
					System.out.println("SIZE BEFORE " + calendar.getFloatingTasksList().size());
					System.out.println("ENTER was released");
					System.out.println(run.getValue().toString());
					ArrayList<String> u = 	tempRH.processCommand(run.getValue().toString());
					for (int i = 0; i < u.size(); i++) {
						System.out.println("output " + Integer.toString(i) + ":\t" + u.get(i));
					}
					System.out.println("SIZE AFTER " + calendar.getFloatingTasksList().size());
					clear(eventTree,floatingTaskTree,taskTree);
					initTree(calendar,eventTree,floatingTaskTree,taskTree,fname);
				

				}
			}
		});
		
		
		
	
	
		
		initTree(calendar,eventTree,floatingTaskTree,taskTree,fname);
		
		
		
		
		
		
		
		
		
		
		mainLayout.setVgap(4);
		mainLayout.add(tree, 0,20);
		mainLayout.add(run, 0, 16);

		window.setScene(mainScene);
		window.show();

		
		
	}
	
	private void initEventBranch(Calendar calander, TreeItem<String> et,String fname) {
		// TODO Auto-generated method stub
		
		
		Calendar newCal = calander;
		System.out.println(newCal.getEventsList().size());
		System.out.println(newCal.toString());
		for(int i = 0; i < newCal.getEventsList().size(); i++) {
			System.out.println(newCal.getEventsList().get(i));
			String name = newCal.getEventsList().get(i).getName();
			String startTime = newCal.getEventsList().get(i).getStartTime();
			String startDate = newCal.getEventsList().get(i).getEndDate();
			String endTime = newCal.getEventsList().get(i).getEndTime();
			String endDate = newCal.getEventsList().get(i).getEndDate();
			String id = Integer.toString(newCal.getEventsList().get(i).getIndex());
			String displayToUser = name + SPACE +  "from" + SPACE + startTime + SPACE +  "on" + SPACE + startDate + 
					SPACE + "till" + SPACE + endDate + SPACE + "," + SPACE + endTime + SPACE + "["  + id + "]";
			makeBranch(displayToUser,et);
		}	
		//<KEY> event <id> name: <name>

	}
	private void clear(TreeItem<String> eventTree,TreeItem<String> floatingTaskTree,TreeItem<String> taskTree) {
		// TODO Auto-generated method stub
		int count = 0;
		while(eventTree.getChildren().size() != 0) {
			for(int i = 0; i < eventTree.getChildren().size();i++) {
				eventTree.getChildren().remove(i);
				System.out.println("NEW SIZE: " + eventTree.getChildren().size());
			}

		}
		while(taskTree.getChildren().size() != 0) {
			for(int i = 0; i < taskTree.getChildren().size();i++) {
				taskTree.getChildren().remove(i);
				System.out.println("NEW SIZE: " + taskTree.getChildren().size());
			}
		}
		while(floatingTaskTree.getChildren().size() != 0) {
			for(int i = 0; i < floatingTaskTree.getChildren().size();i++) {
				floatingTaskTree.getChildren().remove(i);
				System.out.println("NEW SIZE: " + floatingTaskTree.getChildren().size());
			}
		}
	}

	private void refreshCalander(String filename) {
		//calendar.createFile(filename);;
	}
	
	
	private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
		// TODO Anguto-generated method stub
		TreeItem<String> item = new TreeItem<>(title);
		item.setExpanded(false);
		parent.getChildren().add(item);
		return item;
	}

	private void setInitialStageDimensions(Stage window, int width,int height) {
		window.setMinHeight(height);
		window.setMinWidth(width);
	}
	
	private String getFileFromUser() {
		String fileName =  AskForFile.display("Welcome to TempoRun", "Please Enter a file");
		return fileName;
	}
	
	private void initTree(Calendar calander,TreeItem<String> et,TreeItem<String> ft, TreeItem<String> t,String fname) {

		initEventBranch(calander,et, fname);
		initFloatingTaskBranch(calander,ft);
		initTaskBranch(calander,t);
	}
	
	private void initTaskBranch(Calendar calander, TreeItem<String> t) {
		// TODO Auto-generated method stub

		for(int i = 0; i < calander.getTasksList().size(); i++) {
			makeBranch(calander.getTasksList().get(i).getName(),t);
		}

	}
	private void initFloatingTaskBranch(Calendar calander, TreeItem<String> ft) {
		// TODO Auto-generated method stub

		for(int i = 0; i < calander.getFloatingTasksList().size(); i++) {
			makeBranch(calander.getFloatingTasksList().get(i).getName(),ft);
		}

	}

	
}
