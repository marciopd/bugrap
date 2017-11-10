package com.vaadin.training.bugrap.ui.component;

import java.util.Date;

import com.vaadin.training.bugrap.util.ElapsedTimeFormat;
import com.vaadin.ui.CustomComponent;

public class Comment extends CustomComponent {

	private static final long serialVersionUID = -7113423443746306442L;

	private static final String AUTHOR_TEMPLATE = "%s (%s)";
	private static final String ANONYMOUS = "Anonymous";

	private final CommentDesign commentDesign;

	public Comment(final String authorName, final Date timestamp, final String description) {

		commentDesign = new CommentDesign();
		commentDesign.userNameLabel.setCaption(String.format(AUTHOR_TEMPLATE, getName(authorName), ElapsedTimeFormat.format(timestamp)));
		commentDesign.descriptionLabel.setValue(description);

		setCompositionRoot(commentDesign);
	}

	private static String getName(final String name) {
		if (name == null) {
			return ANONYMOUS;
		}
		return name;
	}

}
