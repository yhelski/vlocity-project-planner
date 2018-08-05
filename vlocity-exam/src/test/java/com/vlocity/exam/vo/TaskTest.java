package com.vlocity.exam.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.vlocity.exam.exception.ParentTaskNotDoneException;

public class TaskTest {


	
	@Test
	public void testTaskCanBeSetToDoneWhenStatusIsStillNotDone() throws ParentTaskNotDoneException {
		Task task = new Task();
		task.setStatus("In Progress");
		task.setDone();
		assertEquals("Status should be done","Done", task.getStatus());
		assertNotNull("end date should not be empty",  task.getEndDate());
	}
	
	@Test
	public void testGetStartDateBasedOnDependenciesShouldReturnStartDateBasedOnParentTask() {
		Project project = new Project("Inventory System", "2018-08-20", "2018-08-25");
				
		Task mainTask = new Task();
		mainTask.setProject(project);
		Task parentTask = new Task();
		parentTask.setStartDate("2018-08-20");
		parentTask.setDuration(48);
		parentTask.setProject(project);
		mainTask.addDependency(parentTask);
		assertNotNull("startDate should not be null", mainTask.getStartDate());
		//Start date is calculated based on the parent task start date plus the duration
		assertEquals("Start date should be 2018-08-26", "2018-08-26", mainTask.getStartDate());
	}
	
	@Test
	public void testGetStartDateBasedOnDependenciesShouldReturnStartDateBasedOnParentTaskEndDateWhenDone() {
		Project project = new Project("Inventory System", "2018-08-20", "2018-08-25");
				
		Task mainTask = new Task();
		mainTask.setProject(project);
		Task parentTask = new Task();
		parentTask.setStartDate("2018-08-20");
		parentTask.setEndDate("2018-08-22");
		parentTask.setDuration(48);
		parentTask.setProject(project);
		mainTask.addDependency(parentTask);
		assertNotNull("startDate should not be null", mainTask.getStartDate());
		//Start date is calculated based on the parent task end date when it is already done
		assertEquals("Start date should be 2018-08-22", "2018-08-22", mainTask.getStartDate());
	}
	
	@Test
	public void testGetStartDateBasedOnDependenciesShouldReturnStartDateBasedProjectStartDateWhenNoDependency() {
		Project project = new Project("Inventory System", "2018-08-20", "2018-08-25");
				
		Task mainTask = new Task();
		mainTask.setProject(project);
		
		assertNotNull("startDate should not be null", mainTask.getStartDate());
		//Start date is calculated based on the project's start date
		assertEquals("Start date should be 2018-08-20", "2018-08-20", mainTask.getStartDate());
	}
	
	@Test
	public void testGetEndDateBasedOnDependenciesShouldReturnEndDateBasedOnParentTaskEndDateWhenDone() {
		Project project = new Project("Inventory System", "2018-08-20", "2018-08-25");
				
		Task mainTask = new Task();
		mainTask.setProject(project);
		Task parentTask = new Task();
		parentTask.setStartDate("2018-08-20");
		parentTask.setEndDate("2018-08-22");
		parentTask.setDuration(48);
		parentTask.setProject(project);
		mainTask.addDependency(parentTask);
		mainTask.setDuration(8);
		assertNotNull("startDate should not be null", mainTask.getEndDate());
		//End date is calculated based on the parent task end date when it is already done plus the task duration 
		assertEquals("Start date should be 2018-08-23", "2018-08-23", mainTask.getEndDate());
	}
	@Test
	public void testGetEndDateBasedOnDependenciesShouldReturnEndDateBasedOnParentTaskStartDateWhenNotDone() {
		Project project = new Project("Inventory System", "2018-08-20", "2018-08-25");
				
		Task mainTask = new Task();
		mainTask.setProject(project);
		Task parentTask = new Task();
		parentTask.setStartDate("2018-08-20");
		parentTask.setDuration(48);
		parentTask.setProject(project);
		mainTask.addDependency(parentTask);
		mainTask.setDuration(8);
		assertNotNull("startDate should not be null", mainTask.getEndDate());
		//End date is calculated based on the parent task start date plus its duration plus the task duration 
		assertEquals("Start date should be 2018-08-27", "2018-08-27", mainTask.getEndDate());
	}
	
	@Test
	public void testGetEndDateBasedOnDependenciesShouldReturnEndDateBasedOnProjectEndDateWhenNoDependency() {
		Project project = new Project("Inventory System", "2018-08-20", "2018-08-25");
				
		Task mainTask = new Task();
		mainTask.setDuration(48);
		mainTask.setProject(project);
		
		assertNotNull("startDate should not be null", mainTask.getEndDate());
		//Start date is calculated based on the project's start date
		assertEquals("Start date should be 2018-08-26", "2018-08-26", mainTask.getEndDate());
	}
}
