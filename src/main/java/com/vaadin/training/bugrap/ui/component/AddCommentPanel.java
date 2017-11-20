package com.vaadin.training.bugrap.ui.component;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.training.bugrap.eventbus.EventBus;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.scope.UIScope;
import com.vaadin.training.bugrap.ui.events.CancelCommentEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentAddedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentDeletedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentFailedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentStartedEvent;
import com.vaadin.training.bugrap.ui.model.AddCommentApplicationModel;
import com.vaadin.training.bugrap.ui.model.CommentUploadModel;
import com.vaadin.training.bugrap.ui.model.Models;
import com.vaadin.ui.Upload;

public class AddCommentPanel extends AddCommentPanelDesign {

	private static final long serialVersionUID = 101936797233120615L;
	private static final String EMPTY_STRING = "";
	private static final String ATTACHMENT_CAPTION = "Attachment...";

	private Binder<Comment> commentBinder;

	public AddCommentPanel() {
		initCommentBinder();
		initCommentTextArea();
		initDoneButton();
		initCancelButton();
	}

	private void initCommentBinder() {
		commentBinder = new Binder<Comment>();
		commentBinder.forField(commentTextArea).bind(Comment::getComment, Comment::setComment);
	}

	private void initCancelButton() {
		cancelButton.addClickListener(event -> {
			getApplicationModel().reset();
			clearPanel();
		});
	}

	private void clearPanel() {
		clearAttachmentsBar();
		clearInputFields();
	}

	private void initDoneButton() {
		doneButton.setEnabled(false);
		doneButton.addClickListener(event -> {
			writeBean();
			getApplicationModel().saveComment();
			clearPanel();
		});
	}

	private void writeBean() {
		try {
			final AddCommentApplicationModel applicationModel = getApplicationModel();
			commentBinder.writeBean(applicationModel.getComment());
		} catch (final ValidationException e) {
			throw new IllegalStateException("Unexpected validation error.", e);
		}
	}

	private void initCommentTextArea() {
		commentTextArea.addValueChangeListener(event -> {
			writeBean();
			updateDoneButtonEnabledProperty();
		});
	}

	private void updateDoneButtonEnabledProperty() {
		doneButton.setEnabled(getApplicationModel().isDoneEnabled());
	}

	public void initialize(final Reporter author, final Report report) {

		final AddCommentApplicationModel applicationModel = getApplicationModel();
		applicationModel.reset();
		applicationModel.setAuthor(author);
		applicationModel.setReport(report);

		initAttachmentButton();
		createNewUploadModel(attachmentButton);

		clearPanel();
	}

	private void initAttachmentButton() {
		attachmentButton = new Upload();
		attachmentButton.setButtonCaption(ATTACHMENT_CAPTION);
		attachmentButtonContainer.removeAllComponents();
		attachmentButtonContainer.addComponent(attachmentButton);
	}

	private CommentUploadModel createNewUploadModel(final Upload uploadButton) {
		final AddCommentApplicationModel applicationModel = getApplicationModel();
		final CommentUploadModel attachmentUploadModel = new CommentUploadModel(uploadButton, applicationModel.getCommentReport(),
				applicationModel.getCommentAuthor());
		uploadButton.addStartedListener(attachmentUploadModel);
		uploadButton.addSucceededListener(attachmentUploadModel);
		uploadButton.addFailedListener(attachmentUploadModel);
		uploadButton.setReceiver(attachmentUploadModel);
		uploadButton.addProgressListener(attachmentUploadModel);
		final EventBus uploadModelEventBus = attachmentUploadModel.getEventBus();
		uploadModelEventBus.subscribe(CommentAttachmentStartedEvent.class, this::receiveCommentUploadStarted);
		uploadModelEventBus.subscribe(CommentAttachmentAddedEvent.class, this::receiveCommentAttachmentAddedEvent);
		uploadModelEventBus.subscribe(CommentAttachmentFailedEvent.class, this::receiveCommentAttachmentFailedEvent);
		uploadModelEventBus.subscribe(CommentAttachmentDeletedEvent.class, this::receiveCommentAttachmentDeletedEvent);

		return attachmentUploadModel;
	}

	private void clearAttachmentsBar() {
		UIEventBus.getInstance().publish(new CancelCommentEvent());
		attachmentsBar.setVisible(false);
	}

	private void clearInputFields() {
		commentTextArea.setValue(EMPTY_STRING);
	}

	private AddCommentApplicationModel getApplicationModel() {
		AddCommentApplicationModel applicationModel = UIScope.getCurrent().getProperty(Models.ADD_COMMENT_PANEL_MODEL);
		if (applicationModel == null) {
			applicationModel = new AddCommentApplicationModel();
			UIScope.getCurrent().setProperty(Models.ADD_COMMENT_PANEL_MODEL, applicationModel);

		}
		return applicationModel;
	}

	public void receiveCommentUploadStarted(final CommentAttachmentStartedEvent event) {

		final Upload currentUploadButton = event.getUpload();

		final CommentUploadDisplay commentAttachment = new CommentUploadDisplay(event.getCommentUploadModel());
		attachmentsBar.addComponent(commentAttachment);
		attachmentsBar.setVisible(true);

		final Upload newAttachmentButton = createNewAttachmentButton(currentUploadButton);
		replaceAttachmentButton(currentUploadButton, newAttachmentButton);

		getApplicationModel().receiveCommentAttachmentStartedEvent(event);
		updateDoneButtonEnabledProperty();
	}

	public void receiveCommentAttachmentAddedEvent(final CommentAttachmentAddedEvent event) {
		getApplicationModel().receiveCommentAttachmentAddedEvent(event);
		updateDoneButtonEnabledProperty();
	}

	public void receiveCommentAttachmentFailedEvent(final CommentAttachmentFailedEvent event) {
		getApplicationModel().receiveCommentAttachmentFailedEvent(event);
		updateDoneButtonEnabledProperty();
	}

	public void receiveCommentAttachmentDeletedEvent(final CommentAttachmentDeletedEvent event) {
		getApplicationModel().receiveCommentAttachmentDeletedEvent(event);
		updateDoneButtonEnabledProperty();
	}

	private void replaceAttachmentButton(final Upload currentUploadButton, final Upload newAttachmentButton) {
		currentUploadButton.setVisible(false);
		attachmentButtonContainer.removeAllComponents();
		attachmentButtonContainer.addComponent(newAttachmentButton);
		attachmentButton = newAttachmentButton;
	}

	private Upload createNewAttachmentButton(final Upload currentUploadButton) {
		final Upload newAttachmentButton = new Upload();
		newAttachmentButton.setButtonCaption(currentUploadButton.getButtonCaption());
		newAttachmentButton.setWidth(currentUploadButton.getWidth(), currentUploadButton.getWidthUnits());
		newAttachmentButton.setHeight(currentUploadButton.getHeight(), currentUploadButton.getHeightUnits());
		newAttachmentButton.setStyleName(currentUploadButton.getStyleName());
		createNewUploadModel(newAttachmentButton);
		return newAttachmentButton;
	}

}
