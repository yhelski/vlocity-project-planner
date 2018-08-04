package com.vlocity.exam.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vlocity.exam.exception.ParentTaskNotDoneException;
import com.vlocity.exam.vo.Project;
import com.vlocity.exam.vo.Task;

public class Planner {

	private static List<Project> projects = new ArrayList<Project>();
	private Project selectedProject;
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public void setSelectedProject(Project project) {
		this.selectedProject = project;
	}
	
	public void setSelectedProject(int index) {
		setSelectedProject(getProjects().get(index));
	}
	
	public Project getSelectedProject() {
		return this.selectedProject;
	}

	/**
	 * This method handles creation of new project
	 * */
	public void createNewProject(String title, String startDate, String endDate) {

		Project project = new Project(title,startDate,endDate);
		project.setStatus("New");
		getProjects().add(project);
		
	}
	
	/**
	 * Entry point of the application
	 * */
	public void startPlan() {
		UIHandler.getInstance(this).startUI();
	}

	/**
	 * This method is used by the planner to assign dependencies to the task
	 * */
	public void assigParentTask(int ansInt,int parent) {
		Task task = this.getSelectedProject().getTaskList().get((ansInt -1));
		task.addParentTask(this.getSelectedProject().getTaskList().get((parent-1)));
	}

	/**
	 * This method sets the status of the task to New
	 * */
	public void modifyTaskStatus(int ansInt, String string) throws ParentTaskNotDoneException {
		Task task = this.getSelectedProject().getTaskList().get((ansInt -1));
			task.setStatus("New");
	}

	/**
	 * This method sets the status of the task to Done
	 * @param ansInt -Index
	 * */
	public void setTaskStatusToDone(int ansInt) throws ParentTaskNotDoneException {
		Task task = this.getSelectedProject().getTaskList().get((ansInt -1));
		task.setDone();
		boolean isProjectDone = true;
		for(int i = 0; i < this.getSelectedProject().getTaskList().size(); i++) {
			
			if(i != (ansInt -1) && !this.getSelectedProject().getTaskList().get(i).getStatus().equalsIgnoreCase("Done")) {
				isProjectDone &= false;
			}
		}
		if(isProjectDone) {
			this.getSelectedProject().setStatus("Done");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			this.getSelectedProject().setEndDate(dateFormat.format(Calendar.getInstance().getTime()));
		}
	}

	/**
	 * This method sets the status of the task to In Progress
	 * @param ansInt -Index
	 * */
	public void setTaskStatusToInProgress(int ansInt) throws ParentTaskNotDoneException {
		Task task = this.getSelectedProject().getTaskList().get((ansInt -1));
		task.setStart();
		if("New" == this.getSelectedProject().getStatus()) {
			this.getSelectedProject().setStatus("In Progress");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			this.getSelectedProject().setStartDate(dateFormat.format(Calendar.getInstance().getTime()));
		}
	}

	/**
	 * This method allows the change of task description
	 * @param ansInt -Index
	 * @param inputScannerStr
	 * */
	public void setTaskDescription(int ansInt, String inputScannerStr) {
		Task task = this.getSelectedProject().getTaskList().get((ansInt -1));
		task.setTaskDescription(inputScannerStr);
	}
	
	public static Date calculateDate(String givenDate, int duration) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date parsedDate;
		try {
			parsedDate = dateFormat.parse(givenDate);
		
	    
		Calendar cal = Calendar.getInstance();
		cal.setTime(parsedDate);
		cal.add(Calendar.DATE, (duration/8));
		date = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
