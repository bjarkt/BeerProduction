package org.grp2.printmanager;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SimplePdfPrinter2 implements IPrintManager {

	private String path;

	public SimplePdfPrinter2()
	{
		setPath("PdfDocument.pdf");
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeDocument(Batch batch, MeasurementLog... logs) {
		try {
			PdfWriter writer = new PdfWriter(path);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			document.add(getHeader());
			document.add(getInfoTable(batch));
			document.add(getMeasurementTable(logs));

			document.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] getDocument()
	{
		byte[] array = null;

		if(path != null) {
			try {
				array = Files.readAllBytes(Paths.get(path));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		return array;
	}

	private Paragraph getHeader()
	{
		Paragraph p = new Paragraph("Batch Report");

		p.setTextAlignment(TextAlignment.CENTER);
		p.setFontSize(30);
		p.setBold();

		return p;
	}

	private Table getInfoTable(Batch batch)
	{
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu   HH:mm:ss");
		float[] columnWidths = {100, 200};
		Table infoTable = new Table(columnWidths);
		infoTable.setWidth(UnitValue.createPercentValue(100));

		Cell cellNoBorder = new Cell().setMinHeight(15).setBorder(Border.NO_BORDER);
		Cell cellBold = new Cell().setMinHeight(15).setBold();

		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Batch ID")));
		infoTable.addCell(String.valueOf(batch.getBatchId()));

		infoTable.addCell(cellNoBorder.clone(true));
		infoTable.addCell(cellNoBorder.clone(true));

		infoTable.addCell(new Cell(1,2).add(new Paragraph("Production").setFontSize(15).setBold()).setMinHeight(15).setBold().setTextAlignment(TextAlignment.CENTER));
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Product type")));
		infoTable.addCell(batch.getBeerName());
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Beers produced")));
		infoTable.addCell(String.valueOf(batch.getAccepted() + batch.getDefect()));
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Beers accepted")));
		infoTable.addCell(String.valueOf(batch.getAccepted()));
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Beers defective")));
		infoTable.addCell(String.valueOf(batch.getDefect()));
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Batch started")));
		infoTable.addCell(batch.getStarted().format(dateTimeFormatter));
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Batch finished")));
		infoTable.addCell(batch.getFinished().format(dateTimeFormatter));
		infoTable.addCell(cellBold.clone(true).add(new Paragraph("Time elapsed (min:sec)")));
		long secondsDiff = ChronoUnit.SECONDS.between(batch.getStarted(), batch.getFinished());
		infoTable.addCell(LocalTime.MIN.plusSeconds(secondsDiff).toString());

		infoTable.addCell(cellNoBorder.clone(true));
		infoTable.addCell(cellNoBorder.clone(true));

		return infoTable;
	}

	private Table getMeasurementTable(MeasurementLog... logs)
	{
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu   HH:mm:ss");
		Cell cellBold = new Cell().setMinHeight(15).setBold();

		UnitValue[] measurementColumnWidth = {UnitValue.createPercentValue(25), UnitValue.createPercentValue(25), UnitValue.createPercentValue(25),UnitValue.createPercentValue(25)};
		Table measurementTable = new Table(measurementColumnWidth);
		measurementTable.setWidth(UnitValue.createPercentValue(100));
		measurementTable.addCell(new Cell(1,4).add(new Paragraph("Measurements").setFontSize(15).setBold()).setMinHeight(15).setBold().setTextAlignment(TextAlignment.CENTER));
		measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Time")).setTextAlignment(TextAlignment.CENTER));
		measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Temperature")).setTextAlignment(TextAlignment.CENTER));
		measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Humidity")).setTextAlignment(TextAlignment.CENTER));
		measurementTable.addCell(cellBold.clone(true).add(new Paragraph("Vibration")).setTextAlignment(TextAlignment.CENTER));

		for (MeasurementLog log : logs) {
			measurementTable.addCell(log.getMeasurementTime().format(dateTimeFormatter)).setTextAlignment(TextAlignment.CENTER);
			measurementTable.addCell(String.valueOf(Math.round(log.getMeasurements().getTemperature() * 100) / 100.)).setTextAlignment(TextAlignment.CENTER);
			measurementTable.addCell(String.valueOf(Math.round(log.getMeasurements().getHumidity() * 100) / 100.)).setTextAlignment(TextAlignment.CENTER);
			measurementTable.addCell(String.valueOf(Math.round(log.getMeasurements().getVibration() * 100) / 100.)).setTextAlignment(TextAlignment.CENTER);
		}

		return measurementTable;
	}
}