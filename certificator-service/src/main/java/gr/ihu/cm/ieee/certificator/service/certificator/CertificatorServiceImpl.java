package gr.ihu.cm.ieee.certificator.service.certificator;

import gr.ihu.cm.ieee.certificator.common.Constants;
import gr.ihu.cm.ieee.certificator.domain.CertificateTemplate;
import gr.ihu.cm.ieee.certificator.domain.Event;
import gr.ihu.cm.ieee.certificator.domain.Person;
import gr.ihu.cm.ieee.certificator.event.RotateEventImpl;
import gr.ihu.cm.ieee.certificator.service.pdf.PdfService;
import gr.ihu.cm.ieee.certificator.util.CertificatorUtils;
import io.vavr.control.Try;

import java.io.File;
import java.util.List;

public class CertificatorServiceImpl implements CertificatorService {

    private final String examplePdf;
    private final String exportPath;
    private final PdfService pdfService;

    public CertificatorServiceImpl(final String templatePdf,
                                   final String exportPath,
                                   final PdfService pdfService) {
        this.examplePdf = templatePdf;
        this.exportPath = exportPath;
        this.pdfService = pdfService;
    }

    @Override
    public Try<File> createCertificate(final Person person,
                                       final String eventName) {
        return CertificatorUtils.getDefaultCertificateTemplate(pdfService, examplePdf)
                .flatMap(certificateTemplate -> createCertificate(person, eventName, certificateTemplate));
    }

    @Override
    public Try<File> createCertificate(final Person person,
                                       final String eventName,
                                       final CertificateTemplate certificateTemplate) {
        return pdfService.openPdfFromFilePath(examplePdf)
                .flatMap(pdfReader -> CertificatorUtils.buildFileName(person, eventName, Constants.FILE_TYPE)
                        .flatMap(exportFile -> Try
                                .of(() -> String.format(
                                        "%s/%s",
                                        exportPath,
                                        exportFile
                                ))
                                .flatMap(exportFileWithPath -> pdfService.createPdf(
                                        pdfReader.getPageSize(1),
                                        exportFileWithPath
                                ).peek(documentPdfWriterTuple2 -> documentPdfWriterTuple2._2()
                                        .setPageEvent(new RotateEventImpl()))
                                        .flatMap(documentPdfWriterTuple2 -> Try
                                                .of(documentPdfWriterTuple2::_1)
                                                .flatMap(document -> Try
                                                        .of(documentPdfWriterTuple2::_2)
                                                        .flatMap(pdfWriter -> Try
                                                                .of(() -> Try.run(document::open))
                                                                .flatMap(ignore -> Try
                                                                        .of(pdfWriter::getDirectContent)
                                                                        .flatMap(pdfContentByte -> Try
                                                                                .of(() -> pdfWriter
                                                                                        .getImportedPage(
                                                                                                pdfReader,
                                                                                                Constants.PDF_FIRST_PAGE
                                                                                        ))
                                                                                .peek(pdfImportedPage -> pdfContentByte.
                                                                                        addTemplate(
                                                                                                pdfImportedPage,
                                                                                                Constants.PDF_X_START,
                                                                                                Constants.PDF_Y_START
                                                                                        ))
                                                                                .flatMap(ignored -> CertificatorUtils
                                                                                        .buildCertificateData(person)
                                                                                        .peek(certificateData -> CertificatorUtils
                                                                                                .addCertificateData(
                                                                                                        pdfContentByte,
                                                                                                        certificateTemplate,
                                                                                                        certificateData
                                                                                                ))))))
                                                        .peek(certificateData -> document.close())
                                                        .flatMap(certificateData -> Try
                                                                .of(() -> new File(exportFileWithPath))))))
                        )
                );

    }

    @Override
    public Try<List<File>> createCertificates(final Event event) {
        return CertificatorUtils.getDefaultCertificateTemplate(pdfService, examplePdf)
                .flatMap(certificateTemplate -> createCertificates(event, certificateTemplate));
    }

    @Override
    public Try<List<File>> createCertificates(final Event event, final CertificateTemplate certificateTemplate) {
        return Try.of(() -> io.vavr.collection.List.ofAll(event.getAttendants())
                .flatMap(person -> createCertificate(person, event.getName(), certificateTemplate))
                .toJavaList());
    }
}
