package Tempo;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.application.*;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.*;

public class TempoGui extends Application{
	private static final double MAIN_PADDING_TOP = 12;
	private static final double MAIN_PADDING_RIGHT = 12;
	private static final double MAIN_PADDING_MAIN_PADDING_BOTTOM = 12;
	private static final double MAIN_PADDING_LEFT = 12;
	private static final String SPACE = " ";
	Calendar calendar; 
	public static void main(String args[]) {
		launch("a");
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Stage window = primaryStage;
		setInitialStageDimensions(window,550,610);
		String fname = getFileFromUser();
		calendar = createCalendar(fname);
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
		initTree(calendar,eventTree,floatingTaskTree,taskTree,fname);
		RequestHandler rh = new RequestHandler(fname);
		
		
		ComboBox run = new ComboBox();
		run.setPrefWidth(550);
		run.setPromptText("Give me a command");
		run.setEditable(true);
		
		run.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent ke) {
	            if (ke.getCode() == KeyCode.ENTER) {
	                System.out.println("ENTER was released");
	                System.out.println(run.getValue().toString());
	                ArgParser parser = new ArgParser();
	                String cmd = parser.getCommand(run.getValue().toString());
	    			String args = parser.getArguments(run.getValue().toString());
	    			rh.execute(cmd, args);
	    			refreshCalander(fname);
	    			clear();
	    			initTree(calendar,eventTree,floatingTaskTree,taskTree,fname);
	    			//tree.refresh();
	    			

	                
	            }
	        }

			private void clear() {
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
				calendar = new Calendar(filename);
			}
	 });
		
		
		
		//mainLayout.setHgap(0.2);
		mainLayout.setVgap(4);
		mainLayout.add(tree, 0,20);
		mainLayout.add(run, 0, 16);
		
		window.setScene(mainScene);
		window.show();
		
	}
		
	//add event drink from 10/10/2015 at 10:00 to 10/11/2015 at 11:00
	//<KEY> event <name> from <start date> at <start time> to <end date> at <end time>
	private void clearTree(TreeItem<String> treeItem) {
	
		System.out.println("SIZE OF TREE  " + treeItem.getChildren().size());

			
		for(int i = 0; i < treeItem.getChildren().size(); i++) {
			treeItem.getChildren().remove(i);
			//System.out.println("REMOVING " + i + "\t" + eventTree.getChildren().get);
		}
		
	}
	
	
	/**
	 * Sets the initial dimensions of a 
	 * javafx Stage with given width,height
	 * @param window
	 * @param width
	 * @param height
	 */
	private void setInitialStageDimensions(Stage window, int width,int height) {
		window.setMinHeight(height);
		window.setMinWidth(width);
	}
	/**
	 * Returns an instance of calendar 
	 * from the filename
	 * @param fname
	 * @return
	 */
	private Calendar createCalendar(String fname) {
	 Calendar calendar = new Calendar(fname);
	 return calendar;
	}
	private void createLayout() {
		
	}
	
	/**
	 * Creates a blocking alert box 
	 * prompting the user for a file
	 * @return
	 */
	private String getFileFromUser() {
		String fileName =  AskForFile.display("Welcome to Tempo", "Please Enter a file");
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
	private void initEventBranch(Calendar calander, TreeItem<String> et,String fname) {
		// TODO Auto-generated method stub
		clearTree(et);
		Calendar newCal = new Calendar(fname);
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
	private TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
		// TODO Anguto-generated method stub
		TreeItem<String> item = new TreeItem<>(title);
		item.setExpanded(false);
		parent.getChildren().add(item);
		return item;
	}
	
	
}