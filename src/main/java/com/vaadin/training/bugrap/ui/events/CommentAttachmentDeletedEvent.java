package com.vaadin.training.bugrap.ui.events;

import java.io.Serializable;

import org.vaadin.bugrap.domain.entities.Comment;

public class CommentAttachmentDeletedEvent implements Serializable {

	private static final long serialVersionUID = -8348020320768070925L;

	private final Comment comment;

	public CommentAttachmentDeletedEvent(final Comment comment) {
		this.comment = comment;
	}

	public Comment getComment() {
		return comment;
	}

}
