package gr.ihu.cm.ieee.certificator.util;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import gr.ihu.cm.ieee.certificator.domain.CertificateData;
import gr.ihu.cm.ieee.certificator.domain.CertificateTemplate;
import gr.ihu.cm.ieee.certificator.domain.Person;
import gr.ihu.cm.ieee.certificator.service.pdf.PdfService;
import io.vavr.control.Try;

public abstract class CertificatorUtils {

    public static Try<Rectangle> getPdfPageSize(final PdfService pdfService,
                                                final String fileName) {
        return pdfService.openPdfFromFilePath(fileName)
                .flatMap(pdfReader -> Try.of(() -> pdfReader.getPageSize(1)));
    }

    public static Try<CertificateTemplate> getDefaultCertificateTemplate(final PdfService pdfService,
                                                                         final String fileName) {
        return getPdfPageSize(pdfService, fileName)
                .map(rectangle -> {
                    float width = rectangle.getWidth();
                    float height = rectangle.getHeight();

                    return CertificateTemplate.DefaultTemplate(
                            width,
                            height
                    );
                });
    }

    public static Try<String> buildFileName(final Person person,
                                            final String eventName,
                                            final String fileType) {
        return Try.of(person::getFullName)
                .flatMap(fullName -> Try.of(fullName::getAsString))
                .map(nameOfPerson -> String.format(
                        "%s-%s.%s",
                        eventName,
                        nameOfPerson,
                        fileType
                ));
    }


    public static Try<CertificateData> buildCertificateData(final Person person) {
        return Try.of(person::getFullName)
                .flatMap(fullName -> Try.of(fullName::getAsString)
                        .flatMap(fullNameAsString -> Try
                                .of(person::getEMail)
                                .map(eMail -> CertificateData
                                        .builder(fullNameAsString)
                                        .eMail(eMail))))
                .map(CertificateData.CertificateDataBuilder::build);
    }

    public static BaseFont getDefaultBaseFont() {
        return Try.of(() -> BaseFont
                .createFont(
                        BaseFont.HELVETICA,
                        BaseFont.CP1252,
                        BaseFont.NOT_EMBEDDED)
        ).get();
    }

    public static void addCertificateData(final PdfContentByte cb,
                                          final CertificateTemplate certificateTemplate,
                                          final CertificateData certificateData) {
        cb.saveState();
        cb.beginText();
        cb.setFontAndSize(getDefaultBaseFont(), certificateTemplate.getFontSize());
        cb.showTextAligned(
                PdfContentByte.ALIGN_CENTER,
                certificateData.getName(),
                certificateTemplate.getNamePosition().getXAxis(),
                certificateTemplate.getNamePosition().getYAxis(),
                0
        );

        cb.showTextAligned(
                PdfContentByte.ALIGN_CENTER,
                certificateData.getEMail(),
                certificateTemplate.getEMailPosition().getXAxis(),
                certificateTemplate.getEMailPosition().getYAxis(),
                0
        );
        cb.endText();
        cb.restoreState();
    }
}
