package com.vlocity.exam.vo;

import java.util.ArrayList;
import java.util.List;

import com.vlocity.exam.exception.ProjectCannotBeCreatedException;
import com.vlocity.exam.vo.Task;

public class Project {

	private String projectTitle;
	private String startDate;
	private String endDate;
	private String status;
	private List<Task> taskList = new ArrayList<Task>();
	public Project(String projectTitle) throws ProjectCannotBeCreatedException {
		if(null == projectTitle) {
			throw new ProjectCannotBeCreatedException();
		}
		this.setProjectTitle(projectTitle);
	}
	
	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public Project(String title, String startDate, String endDate) {
		this.projectTitle = title;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Project() {
	}

	public String getProjectTitle() {
		return projectTitle;
	}
	
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public List<Task> getTaskList(){
		return this.taskList;
	}
	
	public void addTask(Task task) {
		this.taskList.add(task);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
