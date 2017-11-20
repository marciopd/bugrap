package com.vaadin.training.bugrap.ui.component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import org.vaadin.bugrap.domain.entities.Comment;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.training.bugrap.util.ElapsedTimeFormat;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

public class CommentPanel extends CommentPanelDesign {

	private static final long serialVersionUID = -7113423443746306442L;
	private static final String MSG_UNKNOWN_TYPE = "Unknown kind of comment type.";
	private static final String BUTTON_LINK_STYLE = "link";

	private static final String AUTHOR_TEMPLATE = "%s (%s)";
	private static final String ANONYMOUS = "Anonymous";

	public CommentPanel(final String authorName, final Date timestamp, final String description) {
		userNameLabel
				.setCaption(String.format(AUTHOR_TEMPLATE, getName(authorName), ElapsedTimeFormat.format(timestamp)));
		addDescritionLabel(description);
	}

	private void addDescritionLabel(final String description) {
		final Label descriptionLabel = new Label(description);
		descriptionLabel.setSizeFull();
		descriptionContainer.addComponent(descriptionLabel);
	}

	public CommentPanel(final Comment comment) {
		userNameLabel
				.setCaption(String.format(AUTHOR_TEMPLATE, getName(comment.getAuthor().getName()),
						ElapsedTimeFormat.format(comment.getTimestamp())));
		initDescription(comment);
	}

	private void initDescription(final Comment comment) {
		if (comment.getType() == null) {
			return;
		}

		switch (comment.getType()) {
		case COMMENT:
			addDescritionLabel(comment.getComment());
			break;
		case ATTACHMENT:
			addAttachmentDownloadLink(comment);
			break;
		default:
			throw new IllegalArgumentException(MSG_UNKNOWN_TYPE);
		}

	}

	private void addAttachmentDownloadLink(final Comment comment) {
		final String attachmentName = comment.getAttachmentName();
		final byte[] attachmentBytes = comment.getAttachment();
		final Button attachmentDownloadButton = new Button(attachmentName);
		attachmentDownloadButton.setStyleName(BUTTON_LINK_STYLE);
		final FileDownloader fileDownloader = new FileDownloader(getResource(attachmentName, attachmentBytes));
		fileDownloader.extend(attachmentDownloadButton);
		descriptionContainer.addComponent(attachmentDownloadButton);
	}

	private Resource getResource(final String attachmentName, final byte[] attachmentBytes) {
		return new StreamResource(new StreamSource() {

			private static final long serialVersionUID = 4339947955035789799L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(attachmentBytes);
			}
		}, attachmentName);
	}

	private static String getName(final String name) {
		if (name == null) {
			return ANONYMOUS;
		}
		return name;
	}

}
