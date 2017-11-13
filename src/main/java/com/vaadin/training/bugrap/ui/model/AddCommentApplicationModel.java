package com.vaadin.training.bugrap.ui.model;

import java.io.Serializable;
import java.util.Calendar;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.model.BugrapFacade;
import com.vaadin.training.bugrap.ui.events.CommentAddedEvent;
import com.vaadin.training.bugrap.util.Assert;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class AddCommentApplicationModel implements Serializable {

	private static final long serialVersionUID = -2569593021947767294L;
	private static final String EMPTY_STRING = "";

	private static final String MSG_REPORT_IS_NULL = "Report is null.";
	private static final String MSG_AUTHOR_IS_NULL = "Author is null.";

	private Comment newComment;

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

	private Comment getComment() {
		if (newComment == null) {
			newComment = new Comment();
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
		Assert.notNull(comment::getAuthor, MSG_AUTHOR_IS_NULL);
		Assert.notNull(comment::getReport, MSG_REPORT_IS_NULL);

		final String commentText = comment.getComment();
		if (commentText == null || commentText.isEmpty()) {
			Notification.show(Messages.EMPTY_COMMENT, Type.ERROR_MESSAGE);
			return;
		}

		comment.setTimestamp(Calendar.getInstance().getTime());

		BugrapFacade.getInstance().save(comment);
		UIEventBus.getInstance().publish(new CommentAddedEvent(comment));
		Notification.show(Messages.COMMENT_ADDED_SUCCESS, Type.HUMANIZED_MESSAGE);

		reset();
	}

	public void reset() {
		if (newComment == null) {
			return;
		}

		final Reporter author = newComment.getAuthor();
		final Report report = newComment.getReport();

		newComment = new Comment();
		newComment.setAuthor(author);
		newComment.setReport(report);
	}

	public Reporter getCommentAuthor() {
		return getComment().getAuthor();
	}

	public Report getCommentReport() {
		return getComment().getReport();
	}

}
