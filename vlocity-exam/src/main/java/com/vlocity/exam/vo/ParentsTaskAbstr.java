package com.vlocity.exam.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vlocity.exam.exception.ParentTaskNotDoneException;

abstract class  ParentsTaskAbstr {

	private List<Task> dependencies = new ArrayList<Task>();
	
	abstract Date getStartDateBasedOnDependencies();
	abstract Date getEndDateBasedOnDependencies();
	void addDependency(Task task) {
		dependencies.add(task);
	}
	
	List<Task> getDependencies(){
		return dependencies;
	}
	
	void checkDependencies() throws ParentTaskNotDoneException {
		for(Task task: dependencies) {
			if(null != task && !task.getStatus().equalsIgnoreCase("Done")) {
				throw new ParentTaskNotDoneException("This task is dependent on another task. Please make sure the parent task is done before starting this task.");
			}
		}
	}
	
	
}
