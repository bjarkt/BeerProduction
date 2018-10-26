package org.grp2.printmanager;

import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;

public interface IPrintManager {
	/**
	 * Writes a document depending on the objects implementation.
	 * @param path the path to write the document to, including name
	 * @param batch any batch
	 * @param logs an array ordered on time
	 */
	void writeDocument(String path, Batch batch, MeasurementLog... logs);
}