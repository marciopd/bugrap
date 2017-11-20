package com.vaadin.training.bugrap.ui.events;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Comment;

public class CommentsAddedEvent implements Serializable {

	private static final long serialVersionUID = 8492563895804281484L;

	private final List<Comment> comments;

	public CommentsAddedEvent(final List<Comment> savedComments) {
		this.comments = savedComments;
	}

	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}
}
