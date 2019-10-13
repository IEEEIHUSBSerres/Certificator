package gr.ihu.cm.ieee.certificator.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import gr.ihu.cm.ieee.certificator.event.RotateEventImpl;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.File;
import java.io.FileOutputStream;

public class PdfServiceImpl implements PdfService {

    @Override
    public Try<Tuple2<Document, PdfWriter>> createPdf(final Rectangle size, final String filePathToSave) {
        return Option.of(Tuple.of(size, filePathToSave))
                .toTry()
                .map(rectangle -> Tuple.of(
                        new Document(rectangle._1()),
                        Option.of(rectangle)
                                .map(Tuple2::_2)
                                .map(File::new)
                                .map(file -> Try.of(() -> new FileOutputStream(file)))
                                .map(Try::get)
                                .get()
                ))
                .map(documentFileOutputStreamTuple2 -> Tuple.of(
                        documentFileOutputStreamTuple2._1(),
                        Try.of(() -> PdfWriter.getInstance(
                                documentFileOutputStreamTuple2._1(),
                                documentFileOutputStreamTuple2._2()
                        )).get()
                ));
    }

    @Override
    public Try<Tuple2<Document, PdfWriter>> createPdfFromTemplate(final Rectangle size,
                                                                  final String filePathToSave,
                                                                  final String filePathOfTemplate) {
        return openPdfFromFilePath(filePathOfTemplate)
                .flatMap(pdfReader -> createPdfFromTemplate(
                        size,
                        filePathToSave,
                        pdfReader
                ));
    }

    @Override
    public Try<Tuple2<Document, PdfWriter>> createPdfFromTemplate(final Rectangle size,
                                                                  final String filePathToSave,
                                                                  final File fileOfTemplate) {
        return openPdfFromFile(fileOfTemplate)
                .flatMap(pdfReader -> createPdfFromTemplate(
                        size,
                        filePathToSave,
                        pdfReader
                ));
    }

    @Override
    public Try<Tuple2<Document, PdfWriter>> createPdfFromTemplate(final Rectangle size,
                                                                  final String filePathToSave,
                                                                  final PdfReader templatePdf) {
        return createPdf(size, filePathToSave)
                .map(documentPdfWriterTuple2 -> {
                    final Document document = documentPdfWriterTuple2._1();
                    final PdfWriter pdfWriter = documentPdfWriterTuple2._2();

                    pdfWriter.setPageEvent(new RotateEventImpl());

                    document.open();

                    final PdfContentByte directContent = pdfWriter.getDirectContent();

                    final PdfImportedPage importedPage = pdfWriter.getImportedPage(templatePdf, 1);

                    directContent.addTemplate(importedPage, 0, 0);

                    return Tuple.of(
                            document,
                            pdfWriter
                    );
                });
    }

    @Override
    public Try<PdfReader> openPdfFromFilePath(final String filePath) {
        return Try.of(() -> new PdfReader(filePath));
    }

    @Override
    public Try<PdfReader> openPdfFromFile(final File file) {
        return Try.of(file::getAbsolutePath)
                .flatMap(this::openPdfFromFilePath);
    }
}
