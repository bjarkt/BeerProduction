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

	/**
	 * Creates a pdf document and return it as an byte array.
	 * @param batch any batch
	 * @param logs and array ordered on time
	 * @return the document as an byte array
	 */
	byte[] getDocument(Batch batch, MeasurementLog... logs);
}