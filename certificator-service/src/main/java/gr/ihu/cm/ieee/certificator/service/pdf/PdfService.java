package gr.ihu.cm.ieee.certificator.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import io.vavr.Tuple2;
import io.vavr.control.Try;

import java.io.File;

public interface PdfService {

    Try<PdfReader> openPdfFromFile(File file);

    Try<Tuple2<Document, PdfWriter>> createPdf(Rectangle size, String filePathToSave);

    Try<Tuple2<Document, PdfWriter>> createPdfFromTemplate(Rectangle size, String filePathToSave, String filePathOfTemplate);

    Try<Tuple2<Document, PdfWriter>> createPdfFromTemplate(Rectangle size, String filePathToSave, File fileOfTemplate);

    Try<Tuple2<Document, PdfWriter>> createPdfFromTemplate(Rectangle size, String filePathToSave, PdfReader template);

    Try<PdfReader> openPdfFromFilePath(String filePath);
}
