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

	private PriorityFormat() {
	}

	public static PriorityFormat getInstance() {
		return INSTANCE;
	}

	public String format(final Priority priority) {

		if (priority == null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		switch (priority) {
		case TRIVIAL:
			append(sb, ONE, PRIORITY_BAR_HTML);
			break;

		case MINOR:
			append(sb, TWO, PRIORITY_BAR_HTML);
			break;

		case NORMAL:
			append(sb, THREE, PRIORITY_BAR_HTML);
			break;

		case MAJOR:
			append(sb, FOUR, PRIORITY_BAR_HTML);
			break;

		case BLOCKER:
			append(sb, FIVE, PRIORITY_BAR_HTML);
			break;

		case CRITICAL:
			append(sb, SIX, PRIORITY_BAR_HTML);
			break;

		default:
			break;
		}

		return sb.toString();
	}

	private void append(final StringBuilder sb, final int numberTimes, final String appendString) {
		for (int i = 0; i < numberTimes; i++) {
			sb.append(appendString);
		}
	}

}
