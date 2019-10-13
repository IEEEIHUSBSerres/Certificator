package gr.ihu.cm.ieee.certificator.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Event {

    private Set<Person> attendants;
    private String name;
}
