package org.grp2.printmanager;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;

public class PrintManager implements IPrintManager {
	public void writeDocument() {
		try {
			PdfWriter writer = new PdfWriter("document.pdf");
			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf);

			Paragraph p = new Paragraph("Hello World! This is my first PDF DOCUMENT!!!!");

			document.add(p);

			document.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}