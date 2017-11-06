package com.vaadin.training.bugrap.util;

import org.vaadin.bugrap.domain.entities.Report.Priority;

public class PriorityFormat {

	private static final PriorityFormat INSTANCE = new PriorityFormat();

	private static final String PRIORITY_BAR_HTML = "<div class=\"priority-bar\"></div>";

	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int SIX = 6;

	private static final String TRIVIAL_HTML = repeat(ONE, PRIORITY_BAR_HTML);
	private static final String MINOR_HTML = repeat(TWO, PRIORITY_BAR_HTML);
	private static final String NORMAL_HTML = repeat(THREE, PRIORITY_BAR_HTML);
	private static final String MAJOR_HTML = repeat(FOUR, PRIORITY_BAR_HTML);
	private static final String BLOCKER_HTML = repeat(FIVE, PRIORITY_BAR_HTML);
	private static final String CRITICAL_HTML = repeat(SIX, PRIORITY_BAR_HTML);

	private PriorityFormat() {
	}

	public static PriorityFormat getInstance() {
		return INSTANCE;
	}

	public String format(final Priority priority) {

		if (priority == null) {
			return null;
		}

		switch (priority) {
		case TRIVIAL:
			return TRIVIAL_HTML;
		case MINOR:
			return MINOR_HTML;
		case NORMAL:
			return NORMAL_HTML;
		case MAJOR:
			return MAJOR_HTML;
		case BLOCKER:
			return BLOCKER_HTML;
		case CRITICAL:
			return CRITICAL_HTML;
		}

		return null;
	}

	private static String repeat(final int numberTimes, final String stringToRepeat) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numberTimes; i++) {
			sb.append(stringToRepeat);
		}
		return sb.toString();
	}

}
