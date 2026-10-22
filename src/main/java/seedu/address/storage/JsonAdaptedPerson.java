package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.EducationLevel;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String guardianName;
    private final String guardianPhone;
    private final String guardianEmail;
    private final String subject;
    private final String educationLevel;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("guardianName") String guardianName,
            @JsonProperty("guardianPhone") String guardianPhone,
            @JsonProperty("guardianEmail") String guardianEmail,
            @JsonProperty("subject") String subject,
            @JsonProperty("educationLevel") String educationLevel) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.guardianName = guardianName;
        this.guardianPhone = guardianPhone;
        this.guardianEmail = guardianEmail;
        this.subject = subject;
        this.educationLevel = educationLevel;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Constructs a person without guardian fields, for older saved data and focused tests.
     */
    public JsonAdaptedPerson(String name, String phone, String email, String address, List<JsonAdaptedTag> tags) {
        this(name, phone, email, address, tags, null, null, null, null, null);
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().map(value -> value.value).orElse(null);
        email = source.getEmail().map(value -> value.value).orElse(null);
        address = source.getAddress().map(value -> value.value).orElse(null);
        guardianName = source.getGuardian().map(value -> value.getName().fullName).orElse(null);
        guardianPhone = source.getGuardian().flatMap(Guardian::getPhone).map(value -> value.value).orElse(null);
        guardianEmail = source.getGuardian().flatMap(Guardian::getEmail).map(value -> value.value).orElse(null);
        subject = source.getSubject().map(Subject::getValue).orElse(null);
        educationLevel = source.getEducationLevel().map(EducationLevel::getValue).orElse(null);
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone != null && !Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Optional<Phone> modelPhone = Optional.ofNullable(phone).map(Phone::new);

        if (email != null && !Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Optional<Email> modelEmail = Optional.ofNullable(email).map(Email::new);

        if (address != null && !Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Optional<Address> modelAddress = Optional.ofNullable(address).map(Address::new);

        if (guardianName == null && (guardianPhone != null || guardianEmail != null)) {
            throw new IllegalValueException("Guardian name is required when guardian contact details are provided.");
        }
        final Optional<Guardian> modelGuardian;
        if (guardianName == null) {
            modelGuardian = Optional.empty();
        } else {
            if (!Name.isValidName(guardianName)) {
                throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
            }
            if (guardianPhone != null && !Phone.isValidPhone(guardianPhone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            if (guardianEmail != null && !Email.isValidEmail(guardianEmail)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            modelGuardian = Optional.of(new Guardian(new Name(guardianName),
                    Optional.ofNullable(guardianPhone).map(Phone::new),
                    Optional.ofNullable(guardianEmail).map(Email::new)));
        }

        if (subject != null && !Subject.isValidSubject(subject)) {
            throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
        }
        final Optional<Subject> modelSubject = Optional.ofNullable(subject).map(Subject::new);

        if (educationLevel != null && !EducationLevel.isValidEducationLevel(educationLevel)) {
            throw new IllegalValueException(EducationLevel.MESSAGE_CONSTRAINTS);
        }
        final Optional<EducationLevel> modelEducationLevel = Optional.ofNullable(educationLevel)
                .map(EducationLevel::new);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelGuardian, modelSubject,
                modelEducationLevel, Optional.empty(), Optional.empty(), modelTags);
    }

}
