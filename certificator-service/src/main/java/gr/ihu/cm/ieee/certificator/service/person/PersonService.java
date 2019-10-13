package gr.ihu.cm.ieee.certificator.service.person;

import gr.ihu.cm.ieee.certificator.domain.Person;
import io.vavr.control.Try;

import java.io.File;
import java.util.List;

public interface PersonService {

    Try<List<Person>> fetchPersonsFromFilePath(String filePath);

    Try<List<Person>> fetchPersonsFromFile(File file);

    Try<List<Person>> fetchPersonsFromString(String string);

    Try<List<Person>> fetchPersonsFromStringList(List<String> stringList);

    Try<Person> fetchPersonFromString(String string);
}
