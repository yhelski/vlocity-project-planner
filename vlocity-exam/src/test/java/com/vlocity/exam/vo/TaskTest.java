package com.vlocity.exam.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.vlocity.exam.exception.ParentTaskNotDoneException;

public class TaskTest {

	@Test(expected=ParentTaskNotDoneException.class)
	public void testTaskCannotSetToStartIfDependencyIsStillNotDone() throws ParentTaskNotDoneException {
		
		Task task = new Task();
		Task parentTask = new Task();
		parentTask.setStatus("In Progress");
		task.setParentTask(parentTask);
		task.setStatus("In Progress");
	}

	
	@Test
	public void testTaskCanBeSetToDoneWhenStatusIsStillNotDone() throws ParentTaskNotDoneException {
		Task task = new Task();
		task.setStatus("In Progress");
		task.setDone();
		assertEquals("Status should be done","Done", task.getStatus());
		assertNotNull("end date should not be empty",  task.getEndDate());
	}
}
