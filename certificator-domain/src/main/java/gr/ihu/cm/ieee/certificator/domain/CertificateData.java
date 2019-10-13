package gr.ihu.cm.ieee.certificator.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CertificateData {

    private String name;
    private String eMail;

    public static CertificateDataBuilder builder(final String name) {
        return new CertificateDataBuilder().name(name);
    }
}
