package com.vaadin.training.bugrap.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Comment.Type;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentAddedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentDeletedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentFailedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentStartedEvent;
import com.vaadin.training.bugrap.ui.events.CommentsAddedEvent;
import com.vaadin.training.bugrap.util.Assert;
import com.vaadin.training.bugrap.util.DateUtil;
import com.vaadin.ui.Notification;

public class AddCommentApplicationModel implements Serializable {

	private static final long serialVersionUID = -2569593021947767294L;
	private static final String EMPTY_STRING = "";

	private static final String MSG_REPORT_IS_NULL = "Report is null.";
	private static final String MSG_AUTHOR_IS_NULL = "Author is null.";
	private static final String MSG_TYPE_IS_NULL = "Type is null.";

	private Comment newComment;

	private Queue<Comment> attachmentComments;
	private int uploadingAttachmentCount;

	public void setCommentText(final String text) {
		getComment().setComment(text);
	}

	public String getCommentText() {
		final String commentText = getComment().getComment();
		if (commentText == null) {
			return EMPTY_STRING;
		}
		return commentText;
	}

	public Comment getComment() {
		if (newComment == null) {
			newComment = new Comment();
			newComment.setType(Type.COMMENT);
		}

		return newComment;
	}

	public void setAuthor(final Reporter author) {
		getComment().setAuthor(author);
	}

	public void setReport(final Report report) {
		getComment().setReport(report);
	}

	public void saveComment() {
		final Comment comment = getComment();

		final String commentText = comment.getComment();
		final boolean existsTextComment = commentText != null && commentText.length() > 0;
		final boolean existsAttachmentComment = getAttachmentComments().size() > 0;

		if (!existsTextComment && !existsAttachmentComment) {
			Notification.show(Messages.NO_COMMENTS, com.vaadin.ui.Notification.Type.ERROR_MESSAGE);
			return;
		}

		final List<Comment> savedComments = new ArrayList<>();
		if (existsTextComment) {
			save(comment);
			savedComments.add(comment);
		}

		if (existsAttachmentComment) {
			for (final Comment attachment : attachmentComments) {
				save(attachment);
				savedComments.add(attachment);
			}
		}

		UIEventBus.getInstance().publish(new CommentsAddedEvent(savedComments));
		showCommentAddedMessage();

		reset();
	}

	private void save(final Comment comment) {
		Assert.notNull(comment::getAuthor, MSG_AUTHOR_IS_NULL);
		Assert.notNull(comment::getReport, MSG_REPORT_IS_NULL);
		Assert.notNull(comment::getType, MSG_TYPE_IS_NULL);
		comment.setTimestamp(DateUtil.now());
		BugrapFacade.getInstance().save(comment);
	}

	public void reset() {
		resetTextComment();
		resetAttachments();
	}

	private void resetAttachments() {
		uploadingAttachmentCount = 0;
		if (attachmentComments == null || attachmentComments.isEmpty()) {
			return;
		}
		attachmentComments.clear();
	}

	private void resetTextComment() {
		if (newComment == null) {
			return;
		}

		final Reporter author = newComment.getAuthor();
		final Report report = newComment.getReport();

		newComment = new Comment();
		newComment.setAuthor(author);
		newComment.setReport(report);
		newComment.setType(Type.COMMENT);
	}

	public Reporter getCommentAuthor() {
		return getComment().getAuthor();
	}

	public Report getCommentReport() {
		return getComment().getReport();
	}

	private void showCommentAddedMessage() {
		Notification.show(Messages.COMMENT_ADDED_SUCCESS, com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE);
	}

	private Collection<Comment> getAttachmentComments() {
		if (attachmentComments == null) {
			attachmentComments = new ConcurrentLinkedQueue<>();
		}
		return attachmentComments;
	}

	public void receiveCommentAttachmentStartedEvent(final CommentAttachmentStartedEvent event) {
		uploadingAttachmentCount++;
	}

	public void receiveCommentAttachmentAddedEvent(final CommentAttachmentAddedEvent event) {
		getAttachmentComments().add(event.getAttachment());
		uploadingAttachmentCount--;
	}

	public void receiveCommentAttachmentFailedEvent(final CommentAttachmentFailedEvent event) {
		uploadingAttachmentCount--;
	}

	public void receiveCommentAttachmentDeletedEvent(final CommentAttachmentDeletedEvent event) {
		getAttachmentComments().remove(event.getComment());
	}

	public boolean isUploadInProgress() {
		return uploadingAttachmentCount > 0;
	}

	public boolean isDoneEnabled() {
		final String commentText = getComment().getComment();
		final boolean someAttachmentOrText = !(getAttachmentComments().isEmpty() && (commentText == null || commentText.isEmpty()));
		return !isUploadInProgress() && someAttachmentOrText;
	}
}
