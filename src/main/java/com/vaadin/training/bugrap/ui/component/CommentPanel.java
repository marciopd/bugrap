package com.vaadin.training.bugrap.ui.component;

import java.util.Date;

import org.vaadin.bugrap.domain.entities.Comment;

import com.vaadin.training.bugrap.util.ElapsedTimeFormat;

public class CommentPanel extends CommentPanelDesign {

	private static final long serialVersionUID = -7113423443746306442L;

	private static final String AUTHOR_TEMPLATE = "%s (%s)";
	private static final String ANONYMOUS = "Anonymous";

	public CommentPanel(final String authorName, final Date timestamp, final String description) {
		userNameLabel
				.setCaption(String.format(AUTHOR_TEMPLATE, getName(authorName), ElapsedTimeFormat.format(timestamp)));
		descriptionLabel.setValue(description);
	}

	public CommentPanel(final Comment comment) {
		this(comment.getAuthor().getName(), comment.getTimestamp(), comment.getComment());
	}

	private static String getName(final String name) {
		if (name == null) {
			return ANONYMOUS;
		}
		return name;
	}

}
