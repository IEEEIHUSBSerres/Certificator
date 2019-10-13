package gr.ihu.cm.ieee.certificator.service.certificator;

import gr.ihu.cm.ieee.certificator.domain.CertificateTemplate;
import gr.ihu.cm.ieee.certificator.domain.Event;
import gr.ihu.cm.ieee.certificator.domain.Person;
import io.vavr.control.Try;

import java.io.File;
import java.util.List;

public interface CertificatorService {

    Try<File> createCertificate(Person person,
                                String eventName);

    Try<File> createCertificate(Person person,
                                String eventName,
                                CertificateTemplate certificateTemplate);

    Try<List<File>> createCertificates(Event event);

    Try<List<File>> createCertificates(Event event,
                                       CertificateTemplate certificateTemplate);
}
