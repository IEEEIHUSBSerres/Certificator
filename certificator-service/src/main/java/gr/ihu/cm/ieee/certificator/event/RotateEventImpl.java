package gr.ihu.cm.ieee.certificator.event;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import io.vavr.control.Try;

public class RotateEventImpl extends PdfPageEventHelper implements RotateEvent {

    @Override
    public void onStartPage(final PdfWriter writer, final Document document) {
        Try.run(() -> writer.addPageDictEntry(PdfName.ROTATE, PdfPage.PORTRAIT));
    }
}