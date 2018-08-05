package com.vlocity.exam.ui;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.vlocity.exam.exception.ParentTaskNotDoneException;
import com.vlocity.exam.vo.Task;

public class UIHandler {

	private static UIHandler uiHandler;
	private Planner planner;
	private UIHandler(Planner planner) {
		this.planner = planner;
	}
	public static synchronized UIHandler getInstance(Planner planner) {
		if(null == uiHandler) {
			return new UIHandler(planner);
		}
		
		return uiHandler;
	}

	/**
	 * The UI main entry point
	 * */
	public void startUI() {
		clearScreen();
		boolean isValid = true;
		Integer ansInt = 0;
		do {
			showAvailableProjects();
			System.out.println("* Create New Project      [1]                                           *");
			System.out.println("* Update Existing Project [2]                                           *");
			System.out.println("* Delete a Project        [3]                                           *");
			System.out.println("* Exit                    [4]                                           *");

			ansInt = inputScannerInt("* Please select menu [1-4]: ");
			if (ansInt > 4) {
				System.out.println("Please enter a number range[1-4] only: ");
				isValid = false;
			}else {
				isValid = true;
			}

		} while (!isValid);

		switch (ansInt) {
		case 1:
			showCreateView();
			break;
		case 2:
			showUpdateView();
			break;
		case 3:
			showDeleteView();
			break;
		case 4:
			System.exit(0);
		}
		startUI();
	}
	/**
	 * The banner construction
	 * */
	private void showWelcomeBanner() {
		System.out.println("*************************************************************************");
		System.out.println("********************** Welcome to project planner ***********************");
		System.out.println("*************************************************************************");
	}

	/**
	 * This method handles showing of existing project
	 * */
	private int showAvailableProjects() {
		System.out.println("*   * Project Name        * Start Date    * End Date      * Status      *");
		System.out.println("*************************************************************************");
		
		int projectSize = planner.getProjects().size();
		
		if (projectSize > 0) {
			for (int i = 0; i < projectSize; i++) {
				System.out.println("*" + "[" + (i + 1) + "]*"+ fixLength(planner.getProjects().get(i).getProjectTitle(),21)+fixLength(planner.getProjects().get(i).getStartDate(),15)+ fixLength(planner.getProjects().get(i).getEndDate(),15)+ fixLength(planner.getProjects().get(i).getStatus(),13));
			}
		}else {
			System.out.println("* No project available. Please create first");
		}
		System.out.println("*************************************************************************");
		
		return projectSize;
	}

	/**
	 * This method handles creation of new project*/
	private void showCreateView() {
		clearScreen();
		String title = inputScannerStr("Project title: ");
		String startDate = inputScannerStr("Start date: [yyyy-MM-dd]: ");
		String endDate = inputScannerStr("End date: [yyyy-MM-dd]: ");
		planner.createNewProject(title, startDate, endDate);
		if (confirmationDialog("Project added successfully. Do you want to add another project?[Y/N]: ")) {
			showCreateView();
		}
	}
	
	/**
	 * This handles the update view of the project
	 * */
	private void showUpdateView() {
		clearScreen();
		int totalProjectAvail = showAvailableProjects();
		if(totalProjectAvail > 0) {
			int input = inputScannerInt("Please select project to update [NUMERIC]: ");
		if(input >= totalProjectAvail) {
			planner.setSelectedProject((input-1));
		taskViewer(planner.getSelectedProject().getTaskList(), null);
		}else {
			System.out.println("Please select number 1 to "+totalProjectAvail);
		}
		}
		startUI();
		
	}

	/**
	 * This method handles prompt for user input
	 * @param String confirmation - message confirmation*/
	private boolean confirmationDialog(String confirmation) {
		String inputStr = inputScannerStr(confirmation);
		if(null != inputStr && inputStr.equalsIgnoreCase("Y")) {
			return true;
		}
		return false;
	}

	/**
	 * This method handles the addition of the task into the project
	 * @param String confirmation*/
	private void addTask(String confirmation) {
		if(confirmationDialog(confirmation)) {
			Task task = new Task();
			task.setTaskDescription(inputScannerStr("Please enter task name: "));
			task.setDuration(inputScannerInt("Please enter task duration (hours) :"));
			
			try {
				task.setStatus("New");
			} catch (ParentTaskNotDoneException e) {
				e.printStackTrace();
			}
			task.setProject(planner.getSelectedProject());
			planner.getSelectedProject().addTask(task);
			viewTaskList(planner.getSelectedProject().getTaskList(), null);
			addTask("Task added successfully to the project. Would you like to create another task for this project?[Y/N]: ");
		}
	}

	/**
	 * this method handle modification of task
	 * */
	private void modifyTask(String confirmation) {
		if(confirmationDialog(confirmation)) {
		int	totalTask = viewTaskList(planner.getSelectedProject().getTaskList(), null);
		StringBuilder message = new StringBuilder();
		if(totalTask > 0) {
			
			int ansInt = inputScannerInt("Please select task to modify[NUMERIC]: ");
			
			if(ansInt <= totalTask) {
			System.out.println("Start the task   [1]");
			System.out.println("Set to Done      [2]");
			System.out.println("Set to New       [3]");
			System.out.println("Set Parent Task  [4]");
			System.out.println("Modify Task Title[5]");
			System.out.println("Go to Main       [6]");
			int operInt = inputScannerInt("Please select operation: ");
			try {
			switch(operInt) {
			case 1 : planner.setTaskStatusToInProgress(ansInt);
			break;
			case 2 : planner.setTaskStatusToDone(ansInt);
			break;
			case 3 : planner.modifyTaskStatus(ansInt,"New");
			break;
			case 4 : {
				viewTaskList(planner.getSelectedProject().getTaskList(), ansInt);
				int parent = inputScannerInt("Please enter task to set as parent task[NUMERIC]: ");
				planner.assigParentTask(ansInt, parent);
				};
			break;
			case 5 : planner.setTaskDescription(ansInt,inputScannerStr("New task description: "));
			break;
			case 6 : return;
			}
			viewTaskList(planner.getSelectedProject().getTaskList(), null);
			message.append("Modification successful.");
			} catch (ParentTaskNotDoneException e) {
				System.out.println(e.getMessage());
				message.append("Modification not successful.");
			}
				modifyTask(message.append(" Do you have another task to modify?[Y/N]: ").toString());
			
			}
		}else {
			System.out.println("Please enter value from 1-"+totalTask);
		}
		}
		
	}

	/**
	 * Task menu viewer
	 * */
	private void taskViewer(List<Task> taskList, Integer filter) {
		
		int totalTask = viewTaskList(taskList,filter);
		System.out.println("*************************************************************************");
		
		if(totalTask > 0) {
			System.out.println("Add new task  [1]");
			System.out.println("Update status [2]");
			System.out.println("Delete task   [3]");
			System.out.println("Go to Main    [4]");
			int ans = inputScannerInt("Please select operation to perform[number only]: ");
			switch(ans) {
			case 1 : addTask("Are you sure you want to create task now?[Y/N]: ");
			break;
			case 2 : modifyTask("Are you sure you want to modify task?[Y/N]: ");
			break;
			case 3 : deleteTask("Are you sure you want to delete a task?[Y/N]: ");
			break;
			case 4 : return;
			}
		}else {
			addTask("There is no task available. Would you like to create now?[Y/N]: ");
		}
		
		if(confirmationDialog("Would you like another transaction?[Y/N]: ")) {
			taskViewer(planner.getSelectedProject().getTaskList(), null);
		}
	
	}

	/**
	 * For task deletion
	 * */
	private void deleteTask(String confirmation) {
		if(confirmationDialog(confirmation)) {
			int	totalTask = viewTaskList(planner.getSelectedProject().getTaskList(), null);
			if(totalTask > 0) {
					
				int ansInt = inputScannerInt("Please select task to modify[NUMERIC]: ");
				planner.getSelectedProject().getTaskList().remove((ansInt -1));
				
				int	totalTasks = viewTaskList(planner.getSelectedProject().getTaskList(), null);
				if(totalTasks > 0) {
					deleteTask("Task successfully deleted. Do you want to delete another task?[Y/N]: ");
				}
			}
		}
	}

	/**
	 * Task list viewer
	 * */
	private int viewTaskList(List<Task> taskList, Integer filter) {
		int totalTask = 0;
		clearScreen();
		System.out.println("*************************************************************************");
		System.out.println("** Project Title : "+ fixLength(planner.getSelectedProject().getProjectTitle(),51 )+"*");
		System.out.println("*************************************************************************");
		System.out.println("*NO.* Task Name           * Start Date    * End Date      * Status      *");
		System.out.println("*************************************************************************");
	
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			totalTask += 1;
			if (null == filter || (i + 1) != filter) {
				String startDate = "";
				if(null != task.getStartDate()) {
					startDate = task.getStartDate();
				}
				
				String endDate = "";
				if(null != task.getEndDate()) {
					endDate = task.getEndDate();
				}
				System.out.println(
						"*[" + totalTask + "]*" + fixLength(task.getTaskDescription(), 21)+ fixLength(startDate,15) + fixLength(endDate,15)+ fixLength(task.getStatus(),13));
			}
		}
		System.out.println("*************************************************************************\n");
		
		return totalTask;
	}

	/**
	 * This method handles fixing the length of each project/task when looping, this adds spaces so the cols width is constant vs value length
	 * */
	public String fixLength(String taskDescription, int lengthSpace) {
		if(null == taskDescription) {
			taskDescription = "";
		}
		StringBuilder builder = new StringBuilder(taskDescription);
		if(taskDescription.length() < lengthSpace){
			
			for(int i = 1; i <= (lengthSpace - taskDescription.length()); i++) {
				builder.append(" ");
			}
			builder.append("*");
		}
		return builder.toString();
	}

	/**
	 * This method handles deletion of the project
	 * */
	private void showDeleteView() {
		clearScreen();
		showAvailableProjects();
		planner.getProjects().remove((inputScannerInt("Please select project to delete[NUMERIC]: ")-1));
	}
	
	/**
	 * This method scans user input and return the int value
	 * @param String message - The message show in the prompt
	 * */
	public int inputScannerInt(String message) {
		Integer index = 0;
		boolean isValid = true;
		do {
		try {
			index = Integer.parseInt(inputScannerStr(message));
			isValid = true;
		} catch (Exception e) {
			isValid = false;
			System.out.println("Please enter valid number only: ");
		}
		}while(!isValid);
		return index;
	}
	
	/**
	 *Scans input and return string value
	 *@param String message - the message to show in the prompt*/
	public String inputScannerStr(String message) {
		Scanner scanner = new Scanner(System.in);
		System.out.print(message);
		String input = scanner.nextLine();
		return input;
	}
	
	/**
	 *This method clears the screen for windows only */
	private void clearScreen() {
		 try {
			 new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 showWelcomeBanner();
	}
}
