package gr.ihu.cm.ieee.certificator.domain.embeddable;

import gr.ihu.cm.ieee.certificator.common.Constants;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.stream.Stream;

@Builder
@Data
public class FullName {

    private String firstName;
    private String middleName;
    private String lastName;

    public String getAsString() {
        return Stream
                .of(
                        Optional.of(firstName),
                        Optional.of(middleName),
                        Optional.of(lastName)
                ).map(string -> string.orElse(Constants.EMPTY_STRING))
                .reduce((result, currentString) -> (currentString.isEmpty())
                        ? result
                        : result.concat(Constants.SPACE_STRING).concat(currentString))
                .orElse(Constants.EMPTY_STRING);
    }
}
