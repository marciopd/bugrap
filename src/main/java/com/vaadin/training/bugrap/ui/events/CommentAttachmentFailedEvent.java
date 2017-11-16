package com.vaadin.training.bugrap.ui.events;

import java.io.Serializable;

import org.vaadin.bugrap.domain.entities.Comment;

public class CommentAttachmentFailedEvent implements Serializable {

	private static final long serialVersionUID = -9195748773771882960L;

	private final Comment comment;

	public CommentAttachmentFailedEvent(final Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}

}
