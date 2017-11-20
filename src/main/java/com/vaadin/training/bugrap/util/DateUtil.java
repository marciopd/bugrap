package com.vaadin.training.bugrap.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private DateUtil() {
	}

	public static Date now() {
		return Calendar.getInstance().getTime();
	}
}
