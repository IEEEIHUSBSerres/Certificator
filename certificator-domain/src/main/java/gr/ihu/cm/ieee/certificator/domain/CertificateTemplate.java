package gr.ihu.cm.ieee.certificator.domain;

import gr.ihu.cm.ieee.certificator.common.Constants;
import gr.ihu.cm.ieee.certificator.domain.embeddable.PositionPair;
import lombok.Data;

@Data
public class CertificateTemplate {

    private final PositionPair namePosition;
    private final Float fontSize;
    private PositionPair eMailPosition;

    public CertificateTemplate(final PositionPair namePosition, final Float fontSize) {
        this.namePosition = namePosition;
        this.eMailPosition = calculateEMailPosition(
                namePosition.getXAxis(),
                namePosition.getYAxis(),
                fontSize
        );
        this.fontSize = fontSize;
    }

    public static CertificateTemplate DefaultTemplate(final Float pageWidth, float pageHeight) {
        return new CertificateTemplate(
                PositionPair.builder()
                        .xAxis(pageWidth / 2)
                        .yAxis(pageHeight / 2)
                        .build(),
                Constants.FONT_SIZE
        );
    }

    private static PositionPair calculateEMailPosition(final Float nameXAxis, final Float nameYAxis, final Float fontSize) {
        return PositionPair.builder()
                .xAxis(nameXAxis)
                .yAxis((nameYAxis) - (fontSize + 10))
                .build();
    }
}
