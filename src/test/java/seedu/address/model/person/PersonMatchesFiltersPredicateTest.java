package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesFiltersPredicateTest {
    private static final Subject MATHEMATICS = new Subject("H2 Mathematics");
    private static final EducationLevel JC1 = new EducationLevel("JC1");

    @Test
    public void constructor_noFilters_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new PersonMatchesFiltersPredicate(Optional.empty(), Optional.empty()));
    }

    @Test
    public void test_subjectOnly_matchesCaseInsensitively() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                Optional.of(new Subject("h2 mathematics")), Optional.empty());

        assertTrue(predicate.test(new PersonBuilder().withSubject("H2 Mathematics").build()));
        assertFalse(predicate.test(new PersonBuilder().withSubject("Physics").build()));
        assertFalse(predicate.test(new PersonBuilder().withoutSubject().build()));
    }

    @Test
    public void test_educationLevelOnly_matchesNormalizedValue() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                Optional.empty(), Optional.of(new EducationLevel("jc1")));

        assertTrue(predicate.test(new PersonBuilder().withEducationLevel("JC1").build()));
        assertFalse(predicate.test(new PersonBuilder().withEducationLevel("S3").build()));
        assertFalse(predicate.test(new PersonBuilder().withoutEducationLevel().build()));
    }

    @Test
    public void test_bothFilters_requiresBothToMatch() {
        PersonMatchesFiltersPredicate predicate = new PersonMatchesFiltersPredicate(
                Optional.of(MATHEMATICS), Optional.of(JC1));

        assertTrue(predicate.test(new PersonBuilder()
                .withSubject("h2 mathematics").withEducationLevel("jc1").build()));
        assertFalse(predicate.test(new PersonBuilder()
                .withSubject("H2 Mathematics").withEducationLevel("S3").build()));
        assertFalse(predicate.test(new PersonBuilder()
                .withSubject("Physics").withEducationLevel("JC1").build()));
    }
}
