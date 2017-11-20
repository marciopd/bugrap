package com.vaadin.training.bugrap.ui.events;

import java.io.Serializable;

import org.vaadin.bugrap.domain.entities.Comment;

public class CommentAttachmentAddedEvent implements Serializable {

	private static final long serialVersionUID = 8155833574673888017L;

	private final Comment attachment;

	public CommentAttachmentAddedEvent(final Comment attachment) {
		this.attachment = attachment;
	}

	public Comment getAttachment() {
		return attachment;
	}

}
