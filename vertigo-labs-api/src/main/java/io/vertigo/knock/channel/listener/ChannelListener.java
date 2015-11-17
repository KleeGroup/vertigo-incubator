package io.vertigo.knock.channel.listener;

import io.vertigo.knock.document.model.Document;
import io.vertigo.knock.document.model.DocumentVersion;

/**
 * Created by sbernard on 12/02/2015.
 */
public interface ChannelListener {
	// Exception may be null if there is no error.
	void onDocumentCrawled(final DocumentVersion documentVersion, final long duration, final Exception e);

	void onDocumentEnhanced(final Document document, final long duration, final Exception exception);

	void onDocumentIndexed(final Document document, final long duration);
}
