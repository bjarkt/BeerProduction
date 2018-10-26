package org.grp2.printmanager;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;

public class SimplePdfPrinter1 implements IPrintManager {
	public void writeDocument() {
		try {
			PdfWriter writer = new PdfWriter("document.pdf");
			PdfDocument pdf = new PdfDocument(writer);

			Document document = new Document(pdf);

//			FontSet fs = new FontSet();
//			try {
//				fs.addFont(FontProgramFactory.createRegisteredFont("courier"), PdfEncodings.UTF8);
//			} catch(IOException e) {
//				e.printStackTrace();
//			}
//
//			FontProvider fp = new FontProvider(fs, "courier");
//			document.setFontProvider(fp);
//			document.setFont("courier");

			Paragraph title = new Paragraph("Batch Rapport").setBold();
			title.setTextAlignment(TextAlignment.CENTER);
			title.setFontSize(30);

			Table batchInfo = new Table(new UnitValue[] {UnitValue.createPercentValue(20), UnitValue.createPercentValue(80)}, true);
			batchInfo.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

			Cell basicTitle = new Cell(0, 2).add(new Paragraph("Basic Information")
					.setTextAlignment(TextAlignment.CENTER)).setFontSize(16);

			batchInfo.addCell(basicTitle);

			buildRow(batchInfo.startNewRow(), "Order no.:", "33");
			buildRow(batchInfo.startNewRow(), "Batch Id:", "123");
			buildRow(batchInfo.startNewRow(), "Beer name:", "Tuborg");
			buildRow(batchInfo.startNewRow(), "Started:", "2018-5-27 12:12:00");
			buildRow(batchInfo.startNewRow(), "Finished:", "2018-5-28 12:20:00");
			buildRow(batchInfo.startNewRow(), "Accepted:", "260");
			buildRow(batchInfo.startNewRow(), "Defected:", "40");

			batchInfo.setVerticalBorderSpacing(10f);

			Table measurements = new Table(3, true);
			measurements.addHeaderCell(new Cell(0, 3).add(new Paragraph("Measurements").setFontSize(18)).setTextAlignment(TextAlignment.CENTER));
			measurements.startNewRow().setTextAlignment(TextAlignment.CENTER).addCell("Time").addCell("Temperature").addCell("Humidity");
			measurements.startNewRow().addCell("2018-5-27 12:13:00").addCell("5").addCell("12");
			measurements.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

			document.add(title);
			document.add(batchInfo);
			document.add(measurements);

			document.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}


		System.out.println(FontProgramFactory.getRegisteredFonts());
	}

	private Table buildRow(Table table, String text, String value) {
		Cell cellText = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(text).setFontSize(14));
		Cell cellValue = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(value).setFontSize(14));

		return table.addCell(cellText).addCell(cellValue);
	}
}