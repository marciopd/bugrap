package com.vaadin.training.bugrap.ui.component;

import com.vaadin.training.bugrap.eventbus.EventBus;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.ui.events.CancelCommentEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentAddedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentFailedEvent;
import com.vaadin.training.bugrap.ui.model.CommentUploadModel;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;

public class CommentUploadDisplay extends CommentUploadDisplayDesign {

	private static final long serialVersionUID = 4925504852110936522L;
	private static final int NO_POLLING = -1;
	private static final float FULL_PROGRESS = .99f;
	private static final int POLL_INTERVAL_NOT_SET = 0;
	private static final int ONE_SECOND = 1000;
	private static final String UPLOAD_COMPLETE_STYLE = "comment-attachment-complete";

	private final CommentUploadModel commentAttachmentModel;

	public CommentUploadDisplay(final CommentUploadModel commentAttachment) {
		this.commentAttachmentModel = commentAttachment;

		final EventBus commentModelEventBus = commentAttachmentModel.getEventBus();
		commentModelEventBus.subscribe(CommentAttachmentAddedEvent.class, this::receiveAttachmentAddedEvent);
		commentModelEventBus.subscribe(CommentAttachmentFailedEvent.class, this::receiveAttachmentFailedEvent);

		UIEventBus.getInstance().subscribe(CancelCommentEvent.class, this::receiveCancelCommentEvent);

		initFileNameLabel();
		initRemoveButton();
		initProgressBar();
		startProgressBarUpdaterThread();
	}

	private void initProgressBar() {
		final boolean fileLengthInderterminate = commentAttachmentModel.getFileLength() <= 0;
		progressBar.setIndeterminate(fileLengthInderterminate);
	}

	private void initFileNameLabel() {
		filenameLabel.setValue(commentAttachmentModel.getFileName());
	}

	private void initRemoveButton() {
		removeButton.addClickListener(event -> {
			removeComponent();
		});
	}

	private void removeAttachmentDisplay() {
		setVisible(false);

		final ComponentContainer attachsmentBar = (ComponentContainer) getParent();
		if (attachsmentBar == null) {
			return;
		}

		attachsmentBar.removeComponent(this);
		if (attachsmentBar.getComponentCount() == 0) {
			attachsmentBar.setVisible(false);
			final UI ui = UI.getCurrent();
			ui.setPollInterval(NO_POLLING);
		}
	}

	private void startProgressBarUpdaterThread() {
		final UI currentUI = UI.getCurrent();
		new Thread(() -> {

			while (isUploading()) {

				try {
					currentUI.access(() -> {
						updateProgress();
					});
				} catch (final UIDetachedException e) {
					// stop processing
					return;
				}

				try {
					Thread.sleep(ONE_SECOND);
				} catch (final InterruptedException e) {
					// ignore, nothing to do
				}
			}
		}).start();

		initUIPolling();
	}

	private void initUIPolling() {
		final UI ui = UI.getCurrent();
		if (ui.getPollInterval() < POLL_INTERVAL_NOT_SET) {
			ui.setPollInterval(ONE_SECOND);
		}
	}

	public boolean isUploading() {
		return isVisible() && commentAttachmentModel.getProgress() < FULL_PROGRESS;
	}

	public void updateProgress() {
		progressBar.setValue(commentAttachmentModel.getProgress());
	}

	public void receiveAttachmentAddedEvent(final CommentAttachmentAddedEvent event) {
		progressBar.setVisible(false);
		setStyleName(UPLOAD_COMPLETE_STYLE);
	}

	public void receiveAttachmentFailedEvent(final CommentAttachmentFailedEvent event) {
		removeAttachmentDisplay();
	}

	public void receiveCancelCommentEvent(final CancelCommentEvent event) {
		removeComponent();
	}

	private void removeComponent() {
		commentAttachmentModel.cancelAttachment();
		removeAttachmentDisplay();
	}
}
