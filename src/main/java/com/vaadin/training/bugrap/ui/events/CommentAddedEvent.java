package com.vaadin.training.bugrap.ui.events;

import java.io.Serializable;

import org.vaadin.bugrap.domain.entities.Comment;

public class CommentAddedEvent implements Serializable {

	private static final long serialVersionUID = 8492563895804281484L;

	private final Comment comment;

	public CommentAddedEvent(final Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}
}
