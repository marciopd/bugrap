package com.vaadin.training.bugrap.ui.events;

import java.io.Serializable;

import com.vaadin.training.bugrap.ui.model.CommentUploadModel;
import com.vaadin.ui.Upload;

public class CommentAttachmentStartedEvent implements Serializable {

	private static final long serialVersionUID = -6963823684567948154L;

	private final Upload upload;
	private final CommentUploadModel commentUploadModel;

	public CommentAttachmentStartedEvent(final Upload upload, final CommentUploadModel commentUploadModel) {
		this.upload = upload;
		this.commentUploadModel = commentUploadModel;
	}

	public CommentUploadModel getCommentUploadModel() {
		return commentUploadModel;
	}

	public Upload getUpload() {
		return upload;
	}
}
