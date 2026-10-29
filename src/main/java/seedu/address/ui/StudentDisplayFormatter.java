package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.Messages;
import seedu.address.model.person.Lesson;
import seedu.address.model.person.Person;

/** Converts student data into the text and states shown by the read-only UI. */
final class StudentDisplayFormatter {

    private static final String OVERDUE_SUFFIX = " (overdue)";

    private StudentDisplayFormatter() {
    }

    /** Returns the lesson text and whether it should use overdue styling. */
    static LessonDisplay formatLesson(Optional<Lesson> lesson, LocalDateTime now) {
        requireNonNull(lesson);
        requireNonNull(now);
        if (lesson.isEmpty()) {
            return new LessonDisplay(Messages.NOT_SCHEDULED, false);
        }
        boolean isOverdue = lesson.get().isOverdue(now);
        String suffix = isOverdue ? OVERDUE_SUFFIX : "";
        return new LessonDisplay(lesson.get().getValue() + suffix, isOverdue);
    }

    /** Returns every value shown in the student-details panel. */
    static StudentDetails formatDetails(Person person, LocalDateTime now) {
        requireNonNull(person);
        requireNonNull(now);
        return new StudentDetails(
                person.getName().fullName,
                sortedTagNames(person),
                person.getPhone().map(value -> value.value).orElse(Messages.NOT_PROVIDED),
                person.getEmail().map(value -> value.value).orElse(Messages.NOT_PROVIDED),
                person.getAddress().map(value -> value.value).orElse(Messages.NOT_PROVIDED),
                person.getSubject().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED),
                person.getEducationLevel().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED),
                person.getLearningFocus().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED),
                person.getNextAction().map(value -> value.getValue()).orElse(Messages.NOT_PROVIDED),
                formatLesson(person.getLesson(), now),
                person.getGuardian().map(value -> value.getName().fullName).orElse(Messages.NOT_PROVIDED),
                person.getGuardian().flatMap(value -> value.getPhone())
                        .map(value -> value.value).orElse(Messages.NOT_PROVIDED),
                person.getGuardian().flatMap(value -> value.getEmail())
                        .map(value -> value.value).orElse(Messages.NOT_PROVIDED));
    }

    /** Returns tag names in the order used by both student views. */
    static List<String> sortedTagNames(Person person) {
        requireNonNull(person);
        return person.getTags().stream()
                .map(tag -> tag.tagName)
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    /** Text and style state for a lesson. */
    record LessonDisplay(String text, boolean isOverdue) {
        LessonDisplay {
            requireNonNull(text);
        }
    }

    /** Text and ordered tags shown in the student-details panel. */
    record StudentDetails(String name, List<String> tags, String phone, String email, String address,
                          String subject, String educationLevel, String learningFocus, String nextAction,
                          LessonDisplay lesson, String guardianName, String guardianPhone, String guardianEmail) {
        StudentDetails {
            requireNonNull(name);
            tags = List.copyOf(tags);
            requireNonNull(phone);
            requireNonNull(email);
            requireNonNull(address);
            requireNonNull(subject);
            requireNonNull(educationLevel);
            requireNonNull(learningFocus);
            requireNonNull(nextAction);
            requireNonNull(lesson);
            requireNonNull(guardianName);
            requireNonNull(guardianPhone);
            requireNonNull(guardianEmail);
        }
    }
}
