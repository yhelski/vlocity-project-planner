package com.vlocity.exam.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UIHandlerTest {

	@Test
	public void testFixLength() {
		assertEquals(" Task Name           *", UIHandler.getInstance(null).fixLength(" Task Name", 21));
	}
}
