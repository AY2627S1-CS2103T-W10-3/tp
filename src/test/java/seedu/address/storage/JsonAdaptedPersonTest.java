package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Address;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Email;
import seedu.address.model.person.LearningFocus;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Name;
import seedu.address.model.person.NextAction;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Subject;
import seedu.address.testutil.PersonBuilder;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_GUARDIAN_NAME = "Jane&";
    private static final String INVALID_GUARDIAN_PHONE = "812a";
    private static final String INVALID_GUARDIAN_EMAIL = "guardian.example.com";
    private static final String INVALID_SUBJECT = " ";
    private static final String INVALID_EDUCATION_LEVEL = "University";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().orElseThrow().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().orElseThrow().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().orElseThrow().toString();
    private static final String VALID_GUARDIAN_NAME = "Jane Meier";
    private static final String VALID_GUARDIAN_PHONE = "81234567";
    private static final String VALID_GUARDIAN_EMAIL = "jane.meier@example.com";
    private static final String VALID_SUBJECT = "H2 Mathematics";
    private static final String VALID_EDUCATION_LEVEL = "JC2";
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_archivedPerson_preservesArchiveState() throws Exception {
        Person archived = new PersonBuilder(BENSON).withArchived(true).build();

        assertEquals(archived, new JsonAdaptedPerson(archived).toModelType());
    }

    @Test
    public void toModelType_missingArchivedProperty_defaultsToActive() throws Exception {
        JsonAdaptedPerson person = JsonUtil.fromJsonString(
                "{\"name\":\"Old Record\"}", JsonAdaptedPerson.class);

        assertFalse(person.toModelType().isArchived());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_returnsPersonWithoutPhone() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(Optional.empty(), person.toModelType().getPhone());
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_returnsPersonWithoutEmail() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_TAGS);
        assertEquals(Optional.empty(), person.toModelType().getEmail());
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_returnsPersonWithoutAddress() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS);
        assertEquals(Optional.empty(), person.toModelType().getAddress());
    }

    @Test
    public void toModelType_nameOnly_returnsPersonWithoutOptionalDetails() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, null, null, null);
        assertEquals(new PersonBuilder().withName(VALID_NAME).withoutPhone().withoutEmail().withoutAddress()
                .withTags().build(), person.toModelType());
    }

    @Test
    public void toModelType_missingJsonProperties_returnsNameOnlyPerson() throws Exception {
        JsonAdaptedPerson person = JsonUtil.fromJsonString("{\"name\":\"Name Only\"}", JsonAdaptedPerson.class);
        assertEquals(new PersonBuilder().withName("Name Only").withoutPhone().withoutEmail()
                .withoutAddress().withTags().build(), person.toModelType());
        assertEquals(Optional.empty(), person.toModelType().getLearningFocus());
        assertEquals(Optional.empty(), person.toModelType().getNextAction());
        assertEquals(Optional.empty(), person.toModelType().getLesson());
    }

    @Test
    public void toModelType_validLesson_returnsPersonWithLesson() throws Exception {
        Person expected = new PersonBuilder(BENSON).withLesson("2026-10-30 16:00").build();

        assertEquals(expected, new JsonAdaptedPerson(expected).toModelType());
        assertEquals(expected, JsonUtil.fromJsonString(JsonUtil.toJsonString(new JsonAdaptedPerson(expected)),
                JsonAdaptedPerson.class).toModelType());
    }

    @Test
    public void toModelType_validRemark_returnsPersonWithRemark() throws Exception {
        Person expected = new PersonBuilder(BENSON).withRemark("Prefers evening lessons.").build();

        assertEquals(expected, JsonUtil.fromJsonString(JsonUtil.toJsonString(new JsonAdaptedPerson(expected)),
                JsonAdaptedPerson.class).toModelType());
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() throws Exception {
        JsonAdaptedPerson person = JsonUtil.fromJsonString(
                "{\"name\":\"Student\",\"remark\":\" \"}", JsonAdaptedPerson.class);

        assertThrows(IllegalValueException.class, Remark.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_archivedStudentWithLesson_keepsBoth() throws Exception {
        Person expected = new PersonBuilder(BENSON).withLesson("2026-10-30 16:00").withArchived(true).build();

        assertEquals(expected, JsonUtil.fromJsonString(JsonUtil.toJsonString(new JsonAdaptedPerson(expected)),
                JsonAdaptedPerson.class).toModelType());
    }

    @Test
    public void toModelType_pastLesson_returnsPersonWithLesson() throws Exception {
        JsonAdaptedPerson person = JsonUtil.fromJsonString(
                "{\"name\":\"Student\",\"lesson\":\"2020-01-01 09:00\"}", JsonAdaptedPerson.class);

        assertEquals(Optional.of(new Lesson("2020-01-01 09:00")), person.toModelType().getLesson());
    }

    @Test
    public void toModelType_invalidLesson_throwsIllegalValueException() throws Exception {
        JsonAdaptedPerson invalidFormat = JsonUtil.fromJsonString(
                "{\"name\":\"Student\",\"lesson\":\"30-10-2026 16:00\"}", JsonAdaptedPerson.class);
        JsonAdaptedPerson invalidDate = JsonUtil.fromJsonString(
                "{\"name\":\"Student\",\"lesson\":\"2026-02-30 16:00\"}", JsonAdaptedPerson.class);

        assertThrows(IllegalValueException.class, Lesson.MESSAGE_CONSTRAINTS, invalidFormat::toModelType);
        assertThrows(IllegalValueException.class, Lesson.MESSAGE_CONSTRAINTS, invalidDate::toModelType);
    }

    @Test
    public void toModelType_validLearningContext_returnsPersonWithLearningContext() throws Exception {
        Person expected = new PersonBuilder(BENSON).withLearningFocus("Fractions and ratios")
                .withNextAction("Complete worksheet 2").build();

        assertEquals(expected, new JsonAdaptedPerson(expected).toModelType());
    }

    @Test
    public void toModelType_invalidLearningContext_throwsIllegalValueException() throws Exception {
        JsonAdaptedPerson invalidLearningFocus = JsonUtil.fromJsonString(
                "{\"name\":\"Student\",\"learningFocus\":\"\"}", JsonAdaptedPerson.class);
        JsonAdaptedPerson invalidNextAction = JsonUtil.fromJsonString(
                "{\"name\":\"Student\",\"nextAction\":\"\"}", JsonAdaptedPerson.class);

        assertThrows(IllegalValueException.class, LearningFocus.MESSAGE_CONSTRAINTS,
                invalidLearningFocus::toModelType);
        assertThrows(IllegalValueException.class, NextAction.MESSAGE_CONSTRAINTS,
                invalidNextAction::toModelType);
    }

    @Test
    public void toModelType_validGuardianDetails_returnsPersonWithGuardian() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_GUARDIAN_NAME, VALID_GUARDIAN_PHONE, VALID_GUARDIAN_EMAIL, null, null);
        assertEquals(new PersonBuilder(BENSON)
                .withGuardian(VALID_GUARDIAN_NAME, VALID_GUARDIAN_PHONE, VALID_GUARDIAN_EMAIL).build(),
                person.toModelType());
    }

    @Test
    public void toModelType_guardianNameOnly_returnsPersonWithGuardianWithoutContacts() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_GUARDIAN_NAME, null, null, null, null);
        assertEquals(new PersonBuilder(BENSON).withGuardianName(VALID_GUARDIAN_NAME).build(), person.toModelType());
    }

    @Test
    public void toModelType_validTutoringDetails_returnsPersonWithTutoringDetails() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, null, null, null, VALID_SUBJECT, VALID_EDUCATION_LEVEL);
        assertEquals(new PersonBuilder(BENSON).withSubject(VALID_SUBJECT)
                .withEducationLevel(VALID_EDUCATION_LEVEL).build(), person.toModelType());
    }

    @Test
    public void toModelType_invalidTutoringDetails_throwIllegalValueException() {
        JsonAdaptedPerson invalidSubject = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, null, null, null, INVALID_SUBJECT, VALID_EDUCATION_LEVEL);
        assertThrows(IllegalValueException.class, Subject.MESSAGE_CONSTRAINTS, invalidSubject::toModelType);

        JsonAdaptedPerson invalidEducationLevel = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS, null, null, null, VALID_SUBJECT, INVALID_EDUCATION_LEVEL);
        assertThrows(IllegalValueException.class, EducationLevel.MESSAGE_CONSTRAINTS,
                invalidEducationLevel::toModelType);
    }

    @Test
    public void toModelType_guardianContactWithoutName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, null, VALID_GUARDIAN_PHONE, VALID_GUARDIAN_EMAIL, null, null);
        assertThrows(IllegalValueException.class,
                "Guardian name is required when guardian contact details are provided.", person::toModelType);
    }

    @Test
    public void toModelType_invalidGuardianFields_throwIllegalValueException() {
        JsonAdaptedPerson invalidName = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, INVALID_GUARDIAN_NAME, VALID_GUARDIAN_PHONE, VALID_GUARDIAN_EMAIL, null, null);
        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, invalidName::toModelType);

        JsonAdaptedPerson invalidPhone = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_GUARDIAN_NAME, INVALID_GUARDIAN_PHONE, VALID_GUARDIAN_EMAIL, null, null);
        assertThrows(IllegalValueException.class, Phone.MESSAGE_CONSTRAINTS, invalidPhone::toModelType);

        JsonAdaptedPerson invalidEmail = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_GUARDIAN_NAME, VALID_GUARDIAN_PHONE, INVALID_GUARDIAN_EMAIL, null, null);
        assertThrows(IllegalValueException.class, Email.MESSAGE_CONSTRAINTS, invalidEmail::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
