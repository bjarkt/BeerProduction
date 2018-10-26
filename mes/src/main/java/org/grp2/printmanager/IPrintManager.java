package org.grp2.printmanager;

import org.grp2.shared.Batch;

public interface IPrintManager {
	/**
	 * Writes a document depending on the objects implementation.
	 * @param path the path to write the document to, including name
	 * @param batch any batch
	 */
	void writeDocument(String path, Batch batch);
}