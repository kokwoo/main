package Tempo.UI.GUI;
//@@author A0145073L
/**
 * 
 * ashish juneja
 * Page object to store information about a TempoTab
 * 
 * 
 */
import java.util.ArrayList;

public class Page {
	ArrayList<TableEntry> entries;
	ArrayList<TableEntry> tableEvents;
	ArrayList<TableEntry> tableTasks;
	ArrayList<TableEntry> tableFloatingTasks;
	
	public Page() {
		this.entries = new ArrayList<TableEntry>(); 
		this.tableEvents = new ArrayList<TableEntry>();
		this.tableTasks = new ArrayList<TableEntry>();
		this.tableFloatingTasks = new ArrayList<TableEntry>();
	}
	
	public ArrayList<TableEntry> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<TableEntry> entries) {
		this.entries = entries;
	}
	public ArrayList<TableEntry> getTableEvents() {
		return tableEvents;
	}
	public void setTableEvents(ArrayList<TableEntry> tableEvents) {
		this.tableEvents = tableEvents;
	}
	public ArrayList<TableEntry> getTableTasks() {
		return tableTasks;
	}
	public void setTableTasks(ArrayList<TableEntry> tableTasks) {
		this.tableTasks = tableTasks;
	}
	public ArrayList<TableEntry> getTableFloatingTasks() {
		return tableFloatingTasks;
	}
	public void setTableFloatingTasks(ArrayList<TableEntry> tableFloatingTasks) {
		this.tableFloatingTasks = tableFloatingTasks;
	}
	

}
