package com.vaadin.training.bugrap.util;

import java.util.Date;

public class CommentFormat {

	private static final String ANONYMOUS = "Anonymous";

	private static String COMMENT_TEMPLATE = "<div class=\"comment\"><div class=\"author-name-label\">"
			+ "<span class=\"v-icon Vaadin-Icons\"></span><span>%s (%s)</span></div>\n"
			+ "<div class=\"report-description\">%s</div></div>";

	private CommentFormat() {
	}

	public static String format(final String name, final Date timestamp, final String description) {
		return String.format(COMMENT_TEMPLATE, getName(name), ElapsedTimeFormat.format(timestamp), description);
	}

	private static String getName(final String name) {
		if (name == null) {
			return ANONYMOUS;
		}

		return name;
	}

}
