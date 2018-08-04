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
}
