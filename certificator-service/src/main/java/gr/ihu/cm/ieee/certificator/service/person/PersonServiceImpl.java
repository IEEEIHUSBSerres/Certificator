package gr.ihu.cm.ieee.certificator.service.person;

import gr.ihu.cm.ieee.certificator.domain.embeddable.FullName;
import gr.ihu.cm.ieee.certificator.domain.Person;
import gr.ihu.cm.ieee.certificator.service.file.FileService;
import io.vavr.Value;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    final FileService fileService;

    @Override
    public Try<Person> fetchPersonFromString(final String string) {
        return Try.of(() -> string.split(","))
                .map(Arrays::asList)
                .map(person -> {
                    final String firstName = person.get(0);
                    final String middleName = person.get(1);
                    final String lastName = person.get(2);
                    final String eMail = person.get(3);

                    final FullName fullName = FullName.builder()
                            .firstName(firstName)
                            .middleName(middleName)
                            .lastName(lastName)
                            .build();

                    return Person.builder()
                            .fullName(fullName)
                            .eMail(eMail);
                })
                .map(Person.PersonBuilder::build);
    }

    @Override
    public Try<List<Person>> fetchPersonsFromStringList(final List<String> stringList) {
        return Try.of(() -> stringList)
                .map(strings -> io.vavr.collection.List.ofAll(stringList)
                        .flatMap(this::fetchPersonFromString))
                .map(Value::toJavaList);
    }

    @Override
    public Try<List<Person>> fetchPersonsFromString(final String string) {
        return Try.of(() -> string.split("\r\n"))
                .flatMap(strings -> Try.of(() -> Arrays.asList(strings))
                        .flatMap(this::fetchPersonsFromStringList));
    }

    @Override
    public Try<List<Person>> fetchPersonsFromFile(final File file) {
        return fileService.readFile(file)
                .flatMap(this::fetchPersonsFromString);
    }

    @Override
    public Try<List<Person>> fetchPersonsFromFilePath(final String filePath) {
        return Try.of(() -> new File(filePath))
                .flatMap(this::fetchPersonsFromFile);
    }
}
