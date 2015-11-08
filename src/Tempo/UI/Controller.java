package Tempo.UI;

/**	@author ashish juneja
 *  Direct questions, issue tracker requests to howhow71@github
 * 	Controller Class for Tempo.UI.
 *  The user interface is stored in a Controller-View pattern.
 *  
 *  The View is an xml (temp.xml)
 *  The controller inflates the view and handles events.
 *  
 *  References: 
 * 
 */






import java.util.ArrayList;
import java.util.HashMap;

import Tempo.CalendarObjects.CalendarObject;
import Tempo.CalendarObjects.Event;
import Tempo.CalendarObjects.FloatingTask;
import Tempo.CalendarObjects.Task;
import Tempo.Commands.Result;
import Tempo.Logic.Calendar;
import Tempo.Logic.Display;
import Tempo.Logic.RequestHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Controller {
	/**
	 * There is no main method; Methods are called based on user events.
	 * Therefore all the objects used need to be inflated as soon as the class 
	 * is created and prepared in the initialize method.
	 */

	@FXML
	Text viewAll;
	@FXML
	Text statusAll;
	@FXML
	TextField consoleAll;

	@FXML
	Text viewToday;
	@FXML
	Text statusToday;
	@FXML
	TextField consoleToday;

	@FXML
	Text viewUpcoming;
	@FXML
	Text statusUpcoming;
	@FXML
	TextField consoleUpcoming;


	@FXML
	Text viewMissed;
	@FXML
	Text statusMissed;
	@FXML
	TextField consoleMissed;

	@FXML
	Text viewSearch;
	@FXML
	Text statusSearch;
	@FXML
	TextField statusConsole;

	@FXML
	Text viewUndone;
	@FXML
	Text statusUndone;
	@FXML
	TextField consoleUndone;


	@FXML
	HBox display;
	@FXML
	TabPane layout;
	@FXML
	ComboBox inputBox;
	@FXML
	TableView table;
	@FXML 
	TableView todayTable;
	@FXML
	TableColumn id;
	@FXML
	TableColumn name;
	@FXML
	TableColumn startTime;
	@FXML
	TableColumn startDate;
	@FXML
	TableColumn endTime;
	@FXML
	TableColumn endDate;
	@FXML
	TableColumn done;


	@FXML
	TableColumn idToday;
	@FXML
	TableColumn nameToday;
	@FXML
	TableColumn startTimeToday;
	@FXML
	TableColumn startDateToday;
	@FXML
	TableColumn endTimeToday;
	@FXML
	TableColumn endDateToday;
	@FXML
	TableColumn doneToday;


	@FXML
	TableView upcomingTable;
	@FXML
	ComboBox inputBoxToday;

	@FXML
	TableColumn upcomingId;
	@FXML
	TableColumn upcomingName;
	@FXML
	TableColumn upcomingDone;
	@FXML
	TableColumn upcomingStartDate;
	@FXML
	TableColumn upcomingStartTime;
	@FXML
	TableColumn upcomingEndDate;
	@FXML
	TableColumn upcomingEndTime;

	@FXML
	ComboBox inputBoxUpComing;

	@FXML
	ComboBox missedInputBox;

	@FXML
	TableView missedTable;

	@FXML
	TableColumn missedId;

	@FXML
	TableColumn missedName;

	@FXML
	TableColumn missedDone;

	@FXML
	TableColumn missedStartDate;

	@FXML
	TableColumn missedStartTime;

	@FXML
	TableColumn missedEndDate;

	@FXML
	TableColumn missedEndTime;



	//

	@FXML
	ComboBox undoneInputBox;

	@FXML
	TableView undoneTable;

	@FXML
	TableColumn undoneId;

	@FXML
	TableColumn undoneName;

	@FXML
	TableColumn undoneDone;

	@FXML
	TableColumn undoneStartDate;

	@FXML
	TableColumn undoneStartTime;

	@FXML
	TableColumn undoneEndDate;

	@FXML
	TableColumn undoneEndTime;


	@FXML
	TabPane tabView;

	@FXML
	TableView searchTable;
	@FXML
	ComboBox searchInput;
	@FXML 
	TableColumn searchId;
	@FXML
	TableColumn searchName;
	@FXML
	TableColumn searchDone;
	@FXML
	TableColumn searchStartDate;
	@FXML 
	TableColumn searchStartTime;
	@FXML
	TableColumn searchEndDate;
	@FXML
	TableColumn searchEndTime;
	@FXML
	ComboBox helpInputBox;
	@FXML
	TextField consoleSearch;
	RequestHandler tempRH;
	Calendar calendar;
	String view  = "all";
	ArrayList<ComboBox> inputBoxes;
	ArrayList<TableView> views;
	ArrayList<Text> userviews;
	ArrayList<Text> status;
	ArrayList<TextField> consoles;
	private static final String KEY_BEST_MATCHES = "bestMatches";
	private static final String KEY_ALTERNATIVE_MATCHES = "alternativeMatches";
	private static final String KEY_EVENTS_BEST_MATCHES = "eventsBestMatches";
	private static final String KEY_EVENTS_ALTERNATIVE_MATCHES = "eventsAlternativeMatches";
	private static final String KEY_TASKS_BEST_MATCHES = "tasksBestMatches";
	private static final String KEY_TASKS_ALTERNATIVE_MATCHES = "tasksAlternativeMatches";
	private static final String KEY_FLOATING_TASKS_BEST_MATCHES = "floatingTasksBestMatches";
	private static final String KEY_FLOATING_TASKS_ALTERNATIVE_MATCHES = "floatingTasksAlternativeMatches";
	Result query;

	public ComboBox getCurrentSearchBox() {
		int currTab = getCurrentTab();
		ComboBox currentBox = inputBoxes.get(currTab);
		return currentBox;	
	}


	public int getCurrentTab() {
		SingleSelectionModel<Tab> selected = tabView.getSelectionModel();	
		return selected.getSelectedIndex();
	}

	private void refresh(Page currPage,String view) {
		SingleSelectionModel<Tab> selected = tabView.getSelectionModel();
		selected.select(5);
		ObservableList<TableEntry> events = determineView(currPage,view);
		TableView currentTable = views.get(5);
		currentTable.setItems(events);		
		currentTable.refresh();
		setViews(view);
		int numberOfMissed = getNumberOfMissedTasks();
		int numberOfUpComing = getNumberOfUpcomingTasks();
		setStatus(numberOfMissed,numberOfUpComing);
	}

	private ObservableList<TableEntry> determineView(Page currPage,String view) {		
		System.out.println("VIEW " + view);

		ObservableList<TableEntry> events = null;
		if(view.equals("task")) {
			events = FXCollections.observableArrayList(currPage.tableTasks);
			System.out.println("SIZE OF TSK" + currPage.tableTasks.size());
			System.out.println("SIZE OF EVT" + currPage.tableEvents.size());
			System.out.println("DISPLAY TASKS");

		}
		else if(view.equals("event")) {
			System.out.println("DISPLAY EVENTS");
			events = FXCollections.observableArrayList(currPage.tableEvents);
			System.out.println("SIZE OF " + currPage.tableEvents.size());
			System.out.println("SIZE OF EVT" + currPage.tableEvents.size());
			System.out.println("DISPLAY EVENTS");


		}
		else if (view.equals("float")) {
			//System.out.println("DISPLAY FLOAT");
			//System.out.println("SIZE OF " + currPage.tableFloatingTasks.size());
			//System.out.println("INSIDE FLOAT : BEFORE SIZE : " + events.size());
			events = FXCollections.observableArrayList(currPage.tableFloatingTasks);
			System.out.println("INSIDE FLOAT : SIZE : " + events.size());
			//System.out.println("SIZE OF EVT" + currPage.tableEvents.size());
			//System.out.println("DISPLAY TASKS");

		}

		else if (view.equals("all")) {
			//System.out.println("DISPLAY FLOAT");
			//System.out.println("SIZE OF " + currPage.tableFloatingTasks.size());
			//System.out.println("INSIDE FLOAT : BEFORE SIZE : " + events.size());
			System.out.println(currPage.entries.size());
			events = FXCollections.observableArrayList(currPage.entries);
			System.out.println("INSIDE FLOAT : SIZE : " + events.size());
			//System.out.println("SIZE OF EVT" + currPage.tableEvents.size());
			//System.out.println("DISPLAY TASKS");

		}
		else {
			events = FXCollections.observableArrayList(currPage.entries);
		}

		return events;

	}

	private void refresh(String view) {
		System.out.println("VIEW " + view);

		SingleSelectionModel<Tab> selected = tabView.getSelectionModel();
		int currIndex = selected.getSelectedIndex();
		Page currPage = null;
		setViews(view);
		int numberOfMissed = getNumberOfMissedTasks();
		int numberOfUpComing = getNumberOfUpcomingTasks();
		setStatus(numberOfMissed,numberOfUpComing);
		tempRH = RequestHandler.getInstance();
		currPage = determineCurrentPage(currIndex);
		calendar = tempRH.getCalendar();
		ObservableList<TableEntry> events = determineView(currPage,view);
		System.out.println("TABLE ENTRY EVENTS SIZE " + events.size());
		TableView currentTable = views.get(currIndex);
		currentTable.setItems(events);		
		currentTable.refresh();
		
		//inputBox.setValue("add event do something from 10/10/2015 at 10:00 to 10/11/2015 at 11:00");
	}

	private Page determineCurrentPage(int currIndex) {

		Page currPage;
		if(currIndex == 0 ) {

			currPage = new Page();
			populateAllPage(currPage);
			System.out.println(currPage.entries.size());

		}

		else if (currIndex == 1) {

			currPage = new Page();
			populateTodayPage(currPage);	
		}

		else if (currIndex == 2) {

			currPage = new Page();
			populateUpcommingPage(currPage);	
		}

		else if(currIndex == 3) {
			System.out.println("***POPULATING MISSED*****");
			currPage = new Page();
			populateMissedPage(currPage);
			System.out.println("SIZE OF TASKS : " + currPage.tableTasks);
			//   System.exit(0);
		}

		else if(currIndex == 4) {
			currPage = new Page();
			populateUndonePage(currPage);

		}
		else {

			currPage = new Page();
			populateAllPage(currPage);
			System.out.println(currPage.entries.size());


		}
		return currPage;
	}



	private void populateUpcommingPage(Page currPage) {
		// TODO Auto-generated method stub
		populateUpCommingEvents(currPage);
		populateUpCommingTasks(currPage);
		coalesce(currPage.entries,currPage.tableEvents,currPage.tableTasks);
		System.out.println("UPCOMING CURR PAGE ENTRIES: " + currPage.entries.size());

	}

	/**System.out.println("SIZE TODAYS TASK" + Display.getInstance().getTasksToday());
		Result todaysCalendarObjects = Display.getInstance().getTasksToday();
		ArrayList<CalendarObject> todaysTasksCalendarObjects = todaysCalendarObjects.getResults().get("Tasks");
		ArrayList<Task> todaysTasks = toTasks(todaysTasksCalendarObjects);
		fillTasks(currPage,todaysTasks);
	 * 
	 * 
	 * @param currPage
	 */





	private void populateMissedPage(Page currPage) {
		populateMissedTasks(currPage);
		combine(currPage.entries,currPage.tableTasks);
		System.out.println("CURR PAGE TABLE EVENT SIZE; " + currPage.tableEvents.size());
	}

	private void populateUndoneTasks(Page currPage) {
		// TODO Auto-generated method stub

		Result CalendarObjects = Display.getInstance().getUndoneFloatingTasks();
		ArrayList<CalendarObject> todaysTasksCalendarObjects = CalendarObjects.getResults().get("Tasks");
		ArrayList<FloatingTask> todaysTasks = toFloatingTasks(todaysTasksCalendarObjects);
		fillFloatingTasks(currPage,todaysTasks);

	}

	private void populateUndonePage(Page currPage) {
		// TODO Auto-generated method stub
		populateUndoneTasks(currPage);
		combine(currPage.entries,currPage.tableFloatingTasks);

	}

	private void populateMissedTasks(Page currPage) {
		// TODO Auto-generated method stub

		Result CalendarObjects = Display.getInstance().getMissedTasks();
		ArrayList<CalendarObject> todaysTasksCalendarObjects = CalendarObjects.getResults().get("Tasks");
		ArrayList<Task> todaysTasks = toTasks(todaysTasksCalendarObjects);
		fillTasks(currPage,todaysTasks);

	}





	private void populateUpCommingTasks(Page currPage) {
		// TODO Auto-generated method stub

		Result CalendarObjects = Display.getInstance().getUpcomingTasks();
		ArrayList<CalendarObject> todaysTasksCalendarObjects = CalendarObjects.getResults().get("Tasks");
		ArrayList<Task> todaysTasks = toTasks(todaysTasksCalendarObjects);
		fillTasks(currPage,todaysTasks);

	}

	private void populateUpCommingEvents(Page currPage) {
		// TODO Auto-generated method stub

		Result todaysCalendarObjects = Display.getInstance().getEventsToday();
		ArrayList<CalendarObject> todaysTasksCalendarObjects = todaysCalendarObjects.getResults().get("Events");
		ArrayList<Event> upComingTasks = toEvents(todaysTasksCalendarObjects);
		fillEvents(currPage,upComingTasks);
		//fillEvent(currPage,upComingTasks);
	}


	private void populateTodayPage(Page currPage) {
		// TODO Auto-generated method stub
		populateTodaysEvents(currPage);
		populateTodaysTasks(currPage);
		coalesce(currPage.entries,currPage.tableEvents,currPage.tableTasks);



	}


	private void populateTodaysTasks(Page currPage) {

		Result todaysCalendarObjects = Display.getInstance().getTasksToday();
		ArrayList<CalendarObject> todaysTasksCalendarObjects = todaysCalendarObjects.getResults().get("Tasks");
		ArrayList<Task> todaysTasks = toTasks(todaysTasksCalendarObjects);
		fillTasks(currPage,todaysTasks);
	}

	private void populateTodaysEvents(Page currPage) {

		Result todaysEventsCalendarObjects = Display.getInstance().getEventsToday();
		ArrayList<Event> todaysEvents = toEvents(todaysEventsCalendarObjects.getResults().get("Events"));
		fillEvents(currPage,todaysEvents);


	}


	private boolean recursiveSearch() {
		SingleSelectionModel<Tab> selected = tabView.getSelectionModel();
		if(selected.getSelectedIndex() == 5) {
			processSearch(query);
			return true;
		}
		return false;
	}


	//add event do something from 10/10/2015 at 10:00 to 10/11/2015 at 11:00
	@FXML
	public void handleEnterPressed(KeyEvent evt){
		ComboBox currentBox = getCurrentSearchBox();
		String userInput =  currentBox.getValue().toString();
		int currentTab = tabView.getSelectionModel().getSelectedIndex();

		if(evt.getCode().equals(KeyCode.ENTER)) {
			currentBox.setValue("");
			if(currentTab == 6) {
				handleAll();
				return;
			}
			else if(userInput.equals("help")) {
				changeTab(6);
				currentTab = getCurrentTab();
			}
			else if(userInput.equals("all")) {
				handleAll();
				return;
			}
			else if(userInput.equals("view all")) {
				view = "all";
				if(recursiveSearch()) {
					return;
				}
				refresh(view);
				table.refresh();
			}

			else if(userInput.equals("td")) {
				changeTab(1);
				currentTab = getCurrentTab();
				refresh(view);

				table.refresh();
			} 
			else if(userInput.equals("up")) {
				changeTab(2);				
				currentTab = getCurrentTab();
				refresh(view);
				table.refresh();
			}

			else if (userInput.equals("m")) {
				changeTab(3);
				currentTab = getCurrentTab();
				refresh(view);
				table.refresh();
			}

			else if(userInput.equals("ud")) {
				SingleSelectionModel<Tab> selected = tabView.getSelectionModel();
				selected.select(4);
				currentTab = selected.getSelectedIndex();
				refresh(view);
				table.refresh();
			}
			else if (userInput.equals("evt")) {
				view = "event";
				Boolean searchRequired = recursiveSearch();
				if(!searchRequired) {
					refresh(view);
				}
				return;
			}
			else if(userInput.equals("flt")) {
				view = "float";
				Boolean searchRequired = recursiveSearch();
				if(!searchRequired) {
					refresh(view);
				}
				return;
			}

			else if(userInput.equals("tsk")) {
				view = "task";
				Boolean searchRequired = recursiveSearch();
				if(!searchRequired) {
					refresh(view);
				}				
				return;
			}
			else if(currentTab == 5) {
				Result userResult = tempRH.processCommand(userInput);
				updateConsoles(userResult);
				processSearch(query);
			}
			else {
				System.out.println("USER ENTERED : " + userInput.toString());
				Result userResult = tempRH.processCommand(userInput);
				updateConsoles(userResult);
				System.out.println("Preformed: " + userResult.getCmdPerformed());
				if(userInput.contains(("search"))) {
					query = userResult;
					processSearch(userResult);
					updateConsoles(userResult);
					return;
				}
				refresh(view);
				table.refresh();
				return;
			}
		}
	}	

	private void changeTab(int index) {
		// TODO Auto-generated method stub
		SingleSelectionModel<Tab> selected = tabView.getSelectionModel();
		selected.select(index);

	}


	private void handleAll() {
		// TODO Auto-generated method stub
		System.out.println("********************ALLLLLL***********************");
		SingleSelectionModel<Tab> selected = tabView.getSelectionModel();
		view = "all";
		selected.select(0);
		refresh("all");
	}


	private void processSearch(Result search ) {

		Page searchPage = new Page();
		populateSearchEvents(searchPage,search);
		populateSearchTasks(searchPage,search);
		populateSearchFloatingTask(searchPage,search);
		coalesce(searchPage.entries,searchPage.tableEvents,searchPage.tableTasks);
		combine(searchPage.entries,searchPage.tableFloatingTasks);
		refresh(searchPage,view);

	}


	private void populateSearchFloatingTask(Page page,Result result) {
		//ArrayList<Event> userEvents = calendar.getEventsList();
		ArrayList<CalendarObject> userCalendarObjects = result.getResults().get(KEY_FLOATING_TASKS_BEST_MATCHES);
		ArrayList<CalendarObject> alternative = result.getResults().get(KEY_FLOATING_TASKS_ALTERNATIVE_MATCHES);
		ArrayList<FloatingTask> userFloatingTasks = toFloatingTasks(userCalendarObjects);
		ArrayList<FloatingTask> alternativeMatches = toFloatingTasks(alternative);
		
		for(int i = 0; i < userFloatingTasks.size(); i++) {
			TableEntry entry = newTableFloatingTaskEntry(userFloatingTasks.get(i));
			page.tableFloatingTasks.add(entry);
		}
		
		for(int i = 0; i < alternativeMatches.size(); i++) {
			TableEntry entry = newTableFloatingTaskEntry(alternativeMatches.get(i));
			page.tableFloatingTasks.add(entry);
		}
		

	}


	private void populateSearchEvents(Page page,Result result) {
		//ArrayList<Event> userEvents = calendar.getEventsList();
		ArrayList<CalendarObject> userCalendarObjects = result.getResults().get(KEY_EVENTS_BEST_MATCHES);
		ArrayList<CalendarObject> alternativeResults = result.getResults().get(KEY_EVENTS_ALTERNATIVE_MATCHES);
		if(userCalendarObjects == null) {
			return;
		}
		ArrayList<Event> userEvents = toEvents(userCalendarObjects);
		ArrayList<Event> alternative = toEvents(alternativeResults);
		for(int i = 0; i < userEvents.size(); i++) {
			TableEntry entry = newTableEventEntry(userEvents.get(i));
			page.tableEvents.add(entry);
		}

		for(int i = 0; i < alternative.size(); i++) {
			TableEntry entry = newTableEventEntry(alternative.get(i));
			page.tableEvents.add(entry);
		}	

		
	}


	private void populateSearchTasks(Page page,Result result) {
		//ArrayList<Event> userEvents = calendar.getEventsList();
		ArrayList<CalendarObject> userCalendarObjects = result.getResults().get(KEY_TASKS_BEST_MATCHES);
		ArrayList<CalendarObject> alternativeTasks = result.getResults().get(KEY_TASKS_ALTERNATIVE_MATCHES);
		System.out.println("***SIZE EVENTS SEARCH " + userCalendarObjects.size());
		ArrayList<Task> userEvents = toTasks(userCalendarObjects);
		ArrayList<Task> alternative  = toTasks(alternativeTasks);
		for(int i = 0; i < userEvents.size(); i++) {
			TableEntry entry = newTableTaskEntry(userEvents.get(i));
			page.tableTasks.add(entry);
		}	

		for(int i = 0; i < alternative.size(); i++) {
			TableEntry entry = newTableTaskEntry(alternative.get(i));
			page.tableTasks.add(entry);
		}	
		
	}



	@FXML 
	public void handleType(KeyEvent evt) {

	}



	private void setAttributes() {

		id.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("index"));
		name.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("name"));
		startTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startTime"));
		startDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startDate"));
		endTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endTime"));
		endDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endDate"));
		done.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("done"));


		idToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("index"));
		nameToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("name"));
		startTimeToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startTime"));
		startDateToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startDate"));
		endTimeToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endTime"));
		endDateToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endDate"));
		doneToday.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("done"));


		upcomingId.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("index"));
		upcomingName.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("name"));
		upcomingStartTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startTime"));
		upcomingStartDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startDate"));
		upcomingEndTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endTime"));
		upcomingEndDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endDate"));
		upcomingDone.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("done"));




		missedId.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("index"));
		missedName.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("name"));
		missedStartTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startTime"));
		missedStartDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startDate"));
		missedEndTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endTime"));
		missedEndDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endDate"));
		missedDone.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("done"));


		undoneId.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("index"));
		undoneName.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("name"));
		undoneStartTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startTime"));
		undoneStartDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startDate"));
		undoneEndTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endTime"));
		undoneEndTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endDate"));
		undoneDone.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("done"));


		searchId.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("index"));
		searchName.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("name"));
		searchStartTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startTime"));
		searchStartDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("startDate"));
		searchEndTime.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endTime"));
		searchEndDate.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("endDate"));
		searchDone.setCellValueFactory(new PropertyValueFactory<TableEntry,String>("done"));


	}	

	private void populateInputBoxes() {
		inputBoxes = new ArrayList<ComboBox>();
		inputBoxes.add(inputBox);
		inputBoxes.add(inputBoxToday);
		inputBoxes.add(inputBoxUpComing);
		inputBoxes.add(missedInputBox);
		inputBoxes.add(undoneInputBox);
		inputBoxes.add(searchInput);
		inputBoxes.add(helpInputBox);
		helpInputBox.setValue("Enter any key to go to exit");
	}

	private void populateViews() {
		views = new ArrayList<TableView>();
		views.add(table);
		views.add(todayTable);
		views.add(upcomingTable);
		views.add(missedTable);
		views.add(undoneTable);
		views.add(searchTable);

	}
	private void populateUserViews() {
		userviews = new ArrayList<Text>();
		userviews.add(viewAll);
		userviews.add(viewToday);
		userviews.add(viewUpcoming);
		userviews.add(viewUndone);
		userviews.add(viewMissed);
		userviews.add(viewSearch);


	}
	private void populateConsoles() {
		consoles = new ArrayList<TextField>();
		consoles.add(consoleAll);
		consoles.add(consoleToday);
		consoles.add(consoleUndone);
		consoles.add(consoleMissed);
		consoles.add(consoleUpcoming);
		consoles.add(consoleSearch);

	}

	private void populateStatus() {
		status = new ArrayList<Text>();
		status.add(statusMissed);
		status.add(statusSearch);
		status.add(statusUndone);
		status.add(statusUpcoming);
		status.add(statusToday);
		status.add(statusAll);
	}
	
	private void initGuiComponents() {
		populateInputBoxes();
		populateViews();
		populateUserViews();
		populateConsoles();
		populateStatus();
	}
	@FXML
	public void initialize() {
		initGuiComponents();
		int numberOfMissed = getNumberOfMissedTasks();
		int numberOfUpComing = getNumberOfUpcomingTasks();
		setStatus(numberOfMissed,numberOfUpComing);
		Page allPage = new Page();
		tempRH = RequestHandler.getInstance();
		calendar = tempRH.getCalendar();
		populateAllPage(allPage);
		ObservableList<TableEntry> events = FXCollections.observableArrayList(allPage.entries);
		setAttributes();
		table.setItems(events);
		//inputBox.setValue("add event do something from 10/10/2015 at 10:00 to 10/11/2015 at 11:00");
		
		table.refresh();
		
	}  

	private void updateConsoles(Result result) {
		String cmdPref = result.getCmdPerformed();
		if(result.isSuccess()) {
			cmdPref = cmdPref = " was successful";
		}
		else if(!result.isSuccess() && result.hasWarning()) {
			
			cmdPref = "Error : " + result.getWarning();
		}
		else {
			cmdPref = cmdPref = " not successful";
		}
		
		
		for(int i = 0; i < consoles.size(); i++) {
			consoles.get(i).setText(cmdPref);
		}
	}


	private void populateAllPage(Page allPage) {
		populateEvents(allPage);
		populateTasks(allPage);
		populateFloatingTasks(allPage);
		coalesce(allPage.entries,allPage.tableEvents,allPage.tableTasks);
		combine(allPage.entries,allPage.tableFloatingTasks);
		//		combine(allPage.entries,allPage.tableEvents);

	}
	private ArrayList<Event> toEvents(ArrayList<CalendarObject> list) {
		ArrayList<Event> userEvents = new ArrayList<Event>();
		for(int i = 0; i < list.size(); i++) {
			CalendarObject currentCalendarObject = list.get(i);
			Event currentEvent = (Event) currentCalendarObject;
			System.out.println("EVENT TO STRING " + currentEvent.toString());
			userEvents.add(currentEvent);
		}
		return userEvents;
	}

	private ArrayList<Task> toTasks(ArrayList<CalendarObject> list) {
		ArrayList<Task> userEvents = new ArrayList<Task>();
		for(int i = 0; i < list.size(); i++) {
			CalendarObject currentCalendarObject = list.get(i);
			Task currentEvent = (Task) currentCalendarObject;
			userEvents.add(currentEvent);
		}
		return userEvents;
	}

	private ArrayList<FloatingTask> toFloatingTasks(ArrayList<CalendarObject> list) {
		ArrayList<FloatingTask> userEvents = new ArrayList<FloatingTask>();
		for(int i = 0; i < list.size(); i++) {
			CalendarObject currentCalendarObject = list.get(i);
			FloatingTask currentEvent = (FloatingTask) currentCalendarObject;
			userEvents.add(currentEvent);
		}
		return userEvents;
	}


	private void populateEvents(Page page) {
		//ArrayList<Event> userEvents = calendar.getEventsList();
		ArrayList<CalendarObject> userCalendarObjects = calendar.getEventsList();

		ArrayList<Event> userEvents = toEvents(userCalendarObjects);
		for(int i = 0; i < userEvents.size(); i++) {
			TableEntry entry = newTableEventEntry(userEvents.get(i));
			page.tableEvents.add(entry);
		}

	}


	private void populateTasks(Page page) {
		ArrayList<CalendarObject> userCalendarObjects = calendar.getTasksList();
		ArrayList<Task> userTasks = toTasks(userCalendarObjects);
		//ArrayList<Task> userTasks = calendar.getTasksList();

		for(int i = 0; i < userTasks.size(); i++) {

			TableEntry entry = newTableTaskEntry(userTasks.get(i));
			page.tableTasks.add(entry);
		}
	}

	private TableEntry newTableFloatingTaskEntry(FloatingTask t) {
		TableEntry entry = new TableEntry(t.getIndex(),t.getName(),Boolean.toString(t.isDone())," "," "," "," ");

		return entry;
	}



	private TableEntry newTableTaskEntry(Task t) {
		TableEntry entry = new TableEntry(t.getIndex(),t.getName()," "," "," ",t.getDueDateSimplified()," ");
		return entry;
	}




	private void populateFloatingTasks(Page page) {

		//	ArrayList<FloatingTask> userFloatingTasks = calendar.getFloatingTasksList();

		ArrayList<CalendarObject> userCalendarObjects = calendar.getFloatingTasksList();

		ArrayList<FloatingTask> userFloatingTasks = toFloatingTasks(userCalendarObjects);
		for(int i = 0; i < userFloatingTasks.size(); i++) {
			System.out.println("ADDING "  + userFloatingTasks.get(i) + "to tasks" );
			TableEntry entry = newTableFloatingTaskEntry(userFloatingTasks.get(i));
			page.tableFloatingTasks.add(entry);
			
			
		}
	} 





	private void coalesce(ArrayList<TableEntry> result, ArrayList<TableEntry> copyOne, ArrayList<TableEntry> copyTwo) {

		combine(result,copyOne);
		combine(result,copyTwo);

	}

	private void combine(ArrayList<TableEntry>mergedList,ArrayList<TableEntry>listToCopy) {
		for(int i = 0; i < listToCopy.size(); i++) {
			mergedList.add(listToCopy.get(i));
		}

	}

	public TableEntry newTableEventEntry(Event e) {
		TableEntry entry = new TableEntry(e.getIndex(),e.getName()," ",e.getStartDate(),e.getStartTime(),e.getEndDate(),e.getEndTime());
		return entry;
	}


	private Page fillEvents(Page page,ArrayList<Event> schedule) {

		for(int i = 0; i < schedule.size(); i++) {
			TableEntry entry = newTableEventEntry(schedule.get(i));
			page.tableEvents.add(entry);
		}
		return page;

	}


	private Page fillFloatingTasks(Page page,ArrayList<FloatingTask> schedule) {

		for(int i = 0; i < schedule.size(); i++) {
			TableEntry entry = newTableFloatingTaskEntry(schedule.get(i));
			page.tableFloatingTasks.add(entry);
		}
		return page;

	}



	private Page fillTasks(Page page,ArrayList<Task> schedule) {

		for(int i = 0; i < schedule.size(); i++) {
			TableEntry entry = newTableTaskEntry(schedule.get(i));
			page.tableTasks.add(entry);
		}
		return page;

	}
	private void setViews(String view) {
		String currView = "Viewing: " + view;
		for(int i = 0; i < userviews.size(); i++) {
			userviews.get(i).setText(currView);

		}
	}
	
	private int getNumberOfMissedTasks() {
		Result missed = Display.getInstance().getMissedTasks();
		ArrayList<CalendarObject> missedTasks = missed.getResults().get("Tasks");
		return missedTasks.size();
	}
	private int getNumberOfUpcomingTasks() {
		Result todayEvents = Display.getInstance().getEventsToday();
		ArrayList<CalendarObject> today = todayEvents.getResults().get("Events");
		
		Result tasks = Display.getInstance().getTasksToday();
		ArrayList<CalendarObject> todaysTasks = tasks.getResults().get("Tasks");
		return today.size() + todaysTasks.size();
	}

	private void setStatus(int missed, int upcoming) {
		String displayStatus = "missed "  + missed + ": " + " today: " + upcoming;
		for(int i = 0; i < status.size(); i++) {
			status.get(i).setText(displayStatus);
		}
	}
}
