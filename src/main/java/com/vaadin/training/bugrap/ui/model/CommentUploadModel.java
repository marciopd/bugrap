package com.vaadin.training.bugrap.ui.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Comment.Type;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.training.bugrap.eventbus.EventBus;
import com.vaadin.training.bugrap.eventbus.UIEventBus;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentAddedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentDeletedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentFailedEvent;
import com.vaadin.training.bugrap.ui.events.CommentAttachmentStartedEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class CommentUploadModel
		implements Receiver, ProgressListener, SucceededListener, FailedListener, StartedListener {

	private static final long serialVersionUID = -7818752183546603936L;
	private static final int FIFTY_KB = 50000;
	private static final int SLEEP_TIME_HUNDRED_MS = 100;

	private final Comment comment;
	private final Upload uploadButton;

	private ByteArrayOutputStream byteArrayOutputStream;
	private long fileLength;
	private float progress;
	private EventBus eventBus;
	private boolean uploadInterrupted;

	public CommentUploadModel(final Upload uploadButton, final Report report, final Reporter author) {
		this.uploadButton = uploadButton;
		comment = new Comment();
		comment.setType(Type.ATTACHMENT);
		comment.setReport(report);
		comment.setAuthor(author);
	}

	public void setFileLength(final long fileLength) {
		this.fileLength = fileLength;
	}

	public void setFileName(final String fileName) {
		comment.setAttachmentName(fileName);
	}

	private OutputStream getOutputStream() {
		if (byteArrayOutputStream == null) {
			byteArrayOutputStream = new SlowByteArrayOutputStream(FIFTY_KB, SLEEP_TIME_HUNDRED_MS);
		}
		return byteArrayOutputStream;
	}

	@Override
	public OutputStream receiveUpload(final String filename, final String mimeType) {
		return getOutputStream();
	}

	public void flushUploadStream() {
		if (byteArrayOutputStream == null) {
			return;
		}
		try {
			byteArrayOutputStream.flush();
		} catch (final IOException e) {
			throw new IllegalStateException("Error flushing outputstream state.", e);
		}

		comment.setAttachment(byteArrayOutputStream.toByteArray());
	}

	public Comment getComment() {
		return comment;
	}

	public String getFileName() {
		return comment.getAttachmentName();
	}

	public void cancelAttachment() {
		if (uploadButton.isUploading()) {
			uploadButton.interruptUpload();
			uploadInterrupted = true;
		} else {
			publish(new CommentAttachmentDeletedEvent(comment));
		}
	}

	public long getFileLength() {
		return fileLength;
	}

	public float getProgress() {
		return progress;
	}

	@Override
	public void uploadStarted(final StartedEvent event) {
		setFileName(event.getFilename());
		setFileLength(event.getContentLength());
		publish(new CommentAttachmentStartedEvent(event.getUpload(), this));
	}

	@Override
	public void updateProgress(final long readBytes, final long contentLength) {
		if (fileLength <= 0 || readBytes < 1) {
			return;
		}
		this.progress = readBytes / (float) contentLength;
	}

	@Override
	public void uploadFailed(final FailedEvent event) {
		publish(new CommentAttachmentFailedEvent(comment));
		if (!uploadInterrupted) {
			Notification.show(String.format(Messages.ATTACHMENT_UPLOAD_FAILURE, getFileName()),
					com.vaadin.ui.Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void uploadSucceeded(final SucceededEvent event) {
		flushUploadStream();

		final Comment attachmentComment = getComment();
		publish(new CommentAttachmentAddedEvent(attachmentComment));
	}

	private <T> void publish(final T event) {
		getEventBus().publish(event);
		UIEventBus.getInstance().publish(event);
	}

	public EventBus getEventBus() {
		if (eventBus == null) {
			eventBus = new EventBus();
		}
		return eventBus;
	}

	/**
	 * This class is created and used only with the purpose of demonstration.
	 *
	 * It slows down the upload so we can actually see that the progress bar and the
	 * parallel uploads are working as expected.
	 */
	private static class SlowByteArrayOutputStream extends ByteArrayOutputStream {

		private final long sleepTimeMilliseconds;

		public SlowByteArrayOutputStream(final int initialSize, final long sleepTimeMilliseconds) {
			super(initialSize);
			this.sleepTimeMilliseconds = sleepTimeMilliseconds;
		}

		@Override
		public synchronized void write(final int b) {
			sleep();
			super.write(b);
		}

		@Override
		public synchronized void write(final byte[] b, final int off, final int len) {
			sleep();
			super.write(b, off, len);
		}

		@Override
		public void write(final byte[] b) throws IOException {
			sleep();
			super.write(b);
		}

		@Override
		public void flush() throws IOException {
			sleep();
			super.flush();
		}

		private void sleep() {
			try {
				Thread.sleep(sleepTimeMilliseconds);
			} catch (final InterruptedException e) {
				// ignore
			}
		}
	}

}