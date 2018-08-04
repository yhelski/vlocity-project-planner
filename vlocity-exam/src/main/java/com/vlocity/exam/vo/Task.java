package com.vlocity.exam.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vlocity.exam.exception.ParentTaskNotDoneException;
import com.vlocity.exam.ui.Planner;

public class Task {

	private String status;
	private int duration;
	private String taskDescription;
	private String startDate;
	private String endDate;
	private List<Task> parentTasks = new ArrayList<Task>();
	private Project project;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) throws ParentTaskNotDoneException {
		for(Task task: parentTasks) {
			if(null != task && !task.getStatus().equalsIgnoreCase("Done")) {
				throw new ParentTaskNotDoneException();
			}
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
		Date startDate = null;

		if (null != this.startDate) {
			return this.startDate;
		} else if (null != parentTasks && !parentTasks.isEmpty()) {
			for (Task parentTask : parentTasks) {
				Date tempDate = null;
				if (null != parentTask && null != parentTask.getEndDate()) {
					tempDate = Planner.calculateDate(parentTask.getEndDate(), 0);
				} else if (null != parentTask && null != parentTask.getStartDate()) {
					tempDate = Planner.calculateDate(parentTask.getStartDate(), parentTask.getDuration());
				}
				if (null == startDate) {
					startDate = tempDate;
				} else {
					if (tempDate.compareTo(startDate) >= 1) {
						startDate = tempDate;
					}
					;
				}
			}
		} else if (null != project && null != project.getStartDate()) {

			return project.getStartDate();

		}

		return dateFormat.format(startDate);
	}
	
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		Date endDate = null;
		int duration = 0;
		if (null == this.endDate) {

			if (null != parentTasks && !parentTasks.isEmpty()) {
				for (Task parentTask : parentTasks) {
					String baseDate = null;

					if (null != parentTask && null != parentTask.getEndDate()) {
						// if the parent task is done already
						baseDate = parentTask.getEndDate();
						duration = this.getDuration();
					} else if (null != parentTask && null != parentTask.getStartDate()) {
						// if the parent task is still in progress
						baseDate = parentTask.getStartDate();
						duration = parentTask.getDuration() + this.getDuration();
					}
					endDate = Planner.calculateDate(baseDate, duration);

				}
			}
			if (null == endDate && null != project && null != project.getStartDate()) {
				// if all parent task dont have date use the project start date
				endDate = Planner.calculateDate(project.getStartDate(), this.getDuration());
			}
		} else {
			return this.endDate;
		}
		return dateFormat.format(endDate);
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public void addParentTask(Task task) {
		parentTasks.add(task);
	}
	
}
