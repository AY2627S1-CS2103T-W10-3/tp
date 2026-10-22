package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person} matches the supplied subject and education-level filters.
 * Every supplied filter must match.
 */
public class PersonMatchesFiltersPredicate implements Predicate<Person> {
    private final Optional<String> normalizedSubject;
    private final Optional<EducationLevel> educationLevel;

    /** Creates a predicate containing at least one filter. */
    public PersonMatchesFiltersPredicate(Optional<Subject> subject, Optional<EducationLevel> educationLevel) {
        requireAllNonNull(subject, educationLevel);
        if (subject.isEmpty() && educationLevel.isEmpty()) {
            throw new IllegalArgumentException("At least one filter must be supplied");
        }
        normalizedSubject = subject.map(value -> value.getValue().toLowerCase(Locale.ROOT));
        this.educationLevel = educationLevel;
    }

    @Override
    public boolean test(Person person) {
        boolean subjectMatches = normalizedSubject
                .map(expected -> person.getSubject()
                        .map(value -> value.getValue().toLowerCase(Locale.ROOT))
                        .filter(expected::equals)
                        .isPresent())
                .orElse(true);
        boolean educationLevelMatches = educationLevel
                .map(expected -> person.getEducationLevel().filter(expected::equals).isPresent())
                .orElse(true);
        return subjectMatches && educationLevelMatches;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PersonMatchesFiltersPredicate otherPredicate)) {
            return false;
        }
        return normalizedSubject.equals(otherPredicate.normalizedSubject)
                && educationLevel.equals(otherPredicate.educationLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizedSubject, educationLevel);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("subject", normalizedSubject)
                .add("educationLevel", educationLevel)
                .toString();
    }
}
