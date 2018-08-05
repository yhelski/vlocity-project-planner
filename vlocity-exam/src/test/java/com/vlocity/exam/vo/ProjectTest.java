package com.vlocity.exam.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.vlocity.exam.exception.ProjectCannotBeCreatedException;

public class ProjectTest {

	@Test
	public void testProjectCanBeCreatedWithProjectName() throws ProjectCannotBeCreatedException {
		Project project = new Project("Inventory System");
		assertNotNull("Project should not be null", project);
		assertEquals("Project name should be Inventory System", project.getProjectTitle());
	}
	
	@Test(expected = ProjectCannotBeCreatedException.class)
	public void testProjectCannotBeCreatedWithoutProjectName() throws ProjectCannotBeCreatedException {
		new Project(null);
	}
	
	@Test
	public void testProjectCanBeAddedTasks() {
		Task task = new Task();
		Project project = null;
		try {
			project = new Project("Inventory System");
		} catch (ProjectCannotBeCreatedException e) {
			e.printStackTrace();
		}
		project.addTask(task);
		assertEquals("Total number of task is now 1", 1,project.getTaskList().size() );
	}
}
