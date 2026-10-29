package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("first");
        List<String> secondPredicateKeywordList = List.of("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // Partial keyword at the start of the name
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Ali"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Partial keyword in the middle of a name
        predicate = new NameContainsKeywordsPredicate(List.of("ice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Partial keyword at the end of the name
        predicate = new NameContainsKeywordsPredicate(List.of("ob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one of multiple keywords matches
        predicate = new NameContainsKeywordsPredicate(List.of("not-present", "car"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case partial keyword
        predicate = new NameContainsKeywordsPredicate(List.of("LIcE"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Accented letters match case-insensitively
        predicate = new NameContainsKeywordsPredicate(List.of("ZOË"));
        assertTrue(predicate.test(new PersonBuilder().withName("Zoë Tan").build()));

        // Name punctuation is matched as part of the keyword
        predicate = new NameContainsKeywordsPredicate(List.of("o'b", "s/o"));
        assertTrue(predicate.test(new PersonBuilder().withName("Liam O'Brien").build()));
        assertTrue(predicate.test(new PersonBuilder().withName("Raj s/o Kumar").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(List.of("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keyword longer than the matching part of the name
        predicate = new NameContainsKeywordsPredicate(List.of("Alicee"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keywords match phone, email and address, but do not match name
        predicate = new NameContainsKeywordsPredicate(List.of("12345", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
