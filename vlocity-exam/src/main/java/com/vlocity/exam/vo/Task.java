package com.vlocity.exam.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vlocity.exam.exception.ParentTaskNotDoneException;

public class Task {

	private String status;
	private int duration;
	private String taskDescription;
	private String startDate;
	private String endDate;
	private Task parentTask;
	private Project project;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) throws ParentTaskNotDoneException {
		if(null != parentTask && !parentTask.getStatus().equalsIgnoreCase("Done")) {
			throw new ParentTaskNotDoneException();
		}
		if("In Progress".equalsIgnoreCase(status)) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(Calendar.getInstance().getTime());
		setStartDate(date);
		
		}
		this.status = status;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getStartDate() {
		String tempStartDate = null;
		if(null != this.startDate) {
			return this.startDate;
		} else if(null != parentTask && null != parentTask.getEndDate()) {
			tempStartDate = calculateDate(parentTask.getEndDate(), 0);
		} else if(null != parentTask && null != parentTask.getStartDate()) {
			tempStartDate = calculateDate(parentTask.getStartDate(), parentTask.getDuration());
		}else if(null != project && null != project.getStartDate()){
			tempStartDate = project.getStartDate();
		}
		return tempStartDate;
	}
	
	private String calculateDate(String date, int duration) {
		String dateStr = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    java.util.Date parsedDate;
		try {
			parsedDate = dateFormat.parse(date);
		
	    
		Calendar cal = Calendar.getInstance();
		cal.setTime(parsedDate);
		cal.add(Calendar.DATE, (duration/8));
		dateStr = dateFormat.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		String temporaryEndDate = null;
		int duration = 0;
		if (null == this.endDate) {
			String baseDate = null;

			if (null != startDate) {
				// if the task is inprogress
				baseDate = startDate;
				duration = this.getDuration();
			} else if (null != parentTask && null != parentTask.getEndDate()) {
				// if the parent task is done already
				baseDate = parentTask.getEndDate();
				duration = this.getDuration();
			} else if (null != parentTask && null != parentTask.getStartDate()) {
				// if the parent task is still in progress
				baseDate = parentTask.getStartDate();
				duration = parentTask.getDuration() + this.getDuration();
			}else if (null != project && null != project.getStartDate()) {
				// if all parent task dont have date use the project start date
				baseDate = project.getStartDate();
				duration = this.getDuration();
			}
			
			temporaryEndDate = calculateDate(baseDate, duration);
		}else {
			return this.endDate;
		}
		return temporaryEndDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Task getParentTask() {
		return parentTask;
	}
	public void setParentTask(Task parentTask) {
		this.parentTask = parentTask;
	}
	
	public void setDone() throws ParentTaskNotDoneException {
		
		if(!status.equalsIgnoreCase("Done")) {
			this.setStatus("Done");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			this.endDate = dateFormat.format(Calendar.getInstance().getTime());
		}
	}
	
	public void setStart() throws ParentTaskNotDoneException {
		
		if(!status.equalsIgnoreCase("In Progress")) {
			this.setStatus("In Progress");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			this.startDate = dateFormat.format(Calendar.getInstance().getTime());
			if(null != this.endDate) {
				this.endDate = null;
			}
		}
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
}
