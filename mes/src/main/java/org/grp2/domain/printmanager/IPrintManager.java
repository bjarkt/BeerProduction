package org.grp2.domain.printmanager;

import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;

public interface IPrintManager {
    /**
     * Sets the path for the stored PDF document.
     *
     * @param path the path to write the document to, including name and extension
     */
    void setPath(String path);

    /**
     * Writes a document depending on the objects implementation.
     *
     * @param batch any batch
     * @param logs  an array ordered on time
     */
    void writeDocument(Batch batch, MeasurementLog... logs);

    /**
     * Gets the created pdf document as an byte array.
     *
     * @return the document as an byte array
     */
    byte[] getDocument();
}