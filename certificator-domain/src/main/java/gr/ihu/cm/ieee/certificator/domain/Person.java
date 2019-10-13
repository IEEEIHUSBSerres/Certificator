package gr.ihu.cm.ieee.certificator.domain;

import gr.ihu.cm.ieee.certificator.domain.embeddable.FullName;
import lombok.*;

@Data
@Builder
public class Person {

    private FullName fullName;
    private String eMail;
}
