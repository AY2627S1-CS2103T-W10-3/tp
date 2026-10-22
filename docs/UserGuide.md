---
layout: page
title: User Guide
---

TutorContacts is an **offline desktop application for private tutors to manage student and guardian contact details, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI).

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2627S1-CS2103T-W10-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for TutorContacts.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all students.

   * `filter s/Mathematics l/JC1` : Shows JC1 Mathematics students.

   * `add n/John Doe p/98765432 gn/Jane Doe gp/91234567` : Adds a student named `John Doe` with optional student and guardian contact details.

   * `delete 3` : Deletes the 3rd student shown in the current list.

   * `clear` : Deletes all student records.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* The prefixes in `add` can be in any order, with whitespace between fields.<br>
  For example, `add p/81234567 n/Alex Tan` is accepted. Each `add` prefix can appear only once.

* Prefixes contain one or two lower-case characters followed by `/`. If a field value contains separate text that
  looks like a prefix, wrap the complete value in double quotes.<br>
  For example, use `a/"Block n/7"`. The quotes are not stored. Longer words such as `Level/` do not need quotes.

* Extraneous parameters for commands that take no parameters, such as `help`, `list`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows the command formats and important rules in the result panel and a help window. The help text is bundled with
TutorContacts, so `help` remains available without internet access and when the data file cannot be loaded. It does not
write to the data file. The help window also has a button to copy the online User Guide link.

Format: `help`


### Adding a student: `add`

Adds a student to TutorContacts. Only the student name is required; the remaining fields are optional.

Format: `add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [l/EDUCATION_LEVEL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [t/TAG]`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
Only `n/` is required. Every other prefix is optional; each supported prefix can be supplied once, in any order.
</div>

| Prefix | Meaning and validation |
|--------|------------------------|
| `n/` | Student name. Required; letters, digits, and spaces; cannot be blank. |
| `p/` | Student phone. Optional; digits only, at least 3 digits. |
| `e/` | Student email. Optional; a valid email address. |
| `a/` | Address. Optional; nonblank text. |
| `s/` | Tutoring subject. Optional; nonblank text of at most 100 characters, without control characters. |
| `l/` | Education level. Optional; `P1`–`P6`, `S1`–`S5`, or `JC1`–`JC2`, regardless of input case. Displayed in uppercase. |
| `gn/` | Guardian name. Optional; uses the student-name rules. |
| `gp/` | Guardian phone. Optional; uses the student-phone rules and requires `gn/`. |
| `ge/` | Guardian email. Optional; uses the student-email rules and requires `gn/`. |
| `t/` | One optional tag; letters and digits only. |

Subject and education level may be supplied independently. Education levels are case-insensitive when entered and are
displayed in uppercase. If either field is omitted, the student card displays `Not provided` for that field.

Leading and trailing whitespace around field values is ignored. Repeated whitespace inside names, guardian names,
subjects, and addresses is stored as one space. Whitespace within phone numbers, email addresses, education levels, and
tags is not accepted. Text before the first prefix and unknown prefix-like tokens such as `x/value` are rejected.
If a subject or address contains a separate one- or two-character token followed by `/`, enclose the complete value in
double quotes. For example, `add n/Alex s/"A l/Mathematics" a/"Block n/7"` stores the values without the quotes.
Words such as `Level/` are ordinary value text and do not need quotes. An unquoted unsupported token is still rejected.
When an input breaks several rules, TutorContacts reports one error predictably: unexpected preamble, unsupported
prefix, missing `n/`, repeated prefix, missing guardian name, then invalid field value. Repeated prefixes are listed in
alphabetical order in the error.

Student names are unique regardless of letter case and repeated spaces. For example, after adding `Alex Tan`,
`add n/ alex   tan` is rejected as a duplicate. Different students may share phone, email, and guardian details.
A guardian name may be supplied without guardian contact information.

Examples:
* Name only: `add n/Alex Tan`
* Prefix-like text in values: `add n/Alice s/A Level/Mathematics a/"Block n/7"`
* All tutoring and guardian details, in a different prefix order: `add l/jc1 gn/Mary Tan s/H2 Mathematics p/98765432 ge/mary@example.com gp/81234567 e/alex@example.com n/Alex Tan`
* Two students may share contacts: `add n/Ben Tan p/81234567 gn/Mary Tan gp/91234567` followed by `add n/Chloe Tan p/81234567 gn/Mary Tan gp/91234567`
* Invalid, because `gn/` is missing: `add n/Sam Lee gp/81234567`
* Invalid, because a prefix repeats: `add n/Sam Lee p/81234567 p/92345678`
* Invalid, because the education level is unsupported: `add n/Sam Lee l/University`
* Invalid, because the subject is empty: `add n/Eva Lim s/ l/P6`
* Invalid guardian email: `add n/Sam Lee gn/Pat Lee ge/pat.example.com`
* Invalid, because of unexpected text: `add surprise n/Sam Lee` or `add n/Sam Lee x/value`

Rejected `add` commands leave the displayed records and saved data unchanged.

### Listing all students: `list`

Shows all student records in their saved order. If there are no records, TutorContacts shows guidance for adding one.
Student cards show the student's recorded contact, tutoring, guardian, and lesson details. Omitted details are shown as
`Not provided`, and a student without a lesson is shown as `Not scheduled`.

After a command is submitted, the command box retains keyboard focus so you can type the next command immediately.
Use the mouse or Tab to focus the list when you want to scroll it or use its normal list navigation keys.

Format: `list`

Example: `list`

`list` also resets any `find` or `filter` result and restores the complete active-student list.

### Understanding result views and indices

`list`, `find`, and `filter` display an active-student result view, while `list archived` displays the archived-student
result view. Every result view is numbered consecutively from 1 in the order shown. An index-based command acts on the
student currently displayed with that index, rather than the student at the same position in the complete saved data.

`edit`, `archive`, `schedule`, and `unschedule` are active-only commands. If the archived view is showing, run `list`
before using one of them. `unarchive` works only in the archived view. `delete` works in either view and permanently
removes the displayed record. Run `list` whenever you want to restore the complete active-student view.

### Editing a student: `edit`

Edits an existing student in TutorContacts.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, all of the student's existing tags are removed; adding tags is not cumulative.
* To remove all of a student's tags, enter `t/` without a tag after it.
* Subject, education-level, and guardian details cannot currently be changed using `edit`. To change one of these
  details, delete the student record and add it again with the corrected information.
* Enclose an address in double quotes if it contains separate prefix-like text, for example `a/"Block n/7"`.

Examples:
* `edit 1 p/91234567 e/johndoe@example.com` edits the phone number and email address of the 1st student.
* `edit 2 n/Betsy Crower t/` changes the name of the 2nd student to `Betsy Crower` and clears all existing tags.

### Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* Matching is case-insensitive and partial; for example, `han` matches `Hans` and `Johanna`.
* Keyword order does not matter; each supplied keyword is matched independently.
* The search considers only names.
* Students matching at least one keyword are returned (an `OR` search); for example, `Hans Bo` returns `Hans Gruber` and `Bo Yang`.
* Each matching student appears once in the same order as the complete active list, even if several or repeated
  keywords match that student.
* If no student matches, TutorContacts displays `No students found.` and shows an empty result view.

Examples:
* `find jo` returns `John Doe` and `Marjorie Tan`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Filtering students by tutoring details: `filter`

Filters active students by subject, education level, or both.

Format: `filter [s/SUBJECT] [l/EDUCATION_LEVEL]`

* At least one of `s/` or `l/` must be supplied, and each can appear only once.
* Subject and education-level matching is case-insensitive and uses the complete normalized value. For example,
  `s/math` does not match `Mathematics`, while `s/mathematics` does.
* When both fields are supplied, a student must match both (`AND`).
* A student without a supplied filter field does not match that filter.
* Results remain in the same order as the complete active list and receive consecutive displayed indices.
* If no student matches, TutorContacts displays `No students match the filters.`
* Run `list` to restore the complete active-student list.

Examples:
* `filter s/H2 Mathematics` shows students whose subject is H2 Mathematics.
* `filter l/jc1` shows JC1 students; education-level input is case-insensitive.
* `filter s/H2 Mathematics l/JC1` shows only students matching both criteria.
* `filter`, `filter s/`, and `filter l/University` are rejected.

### Deleting a student: `delete`

Deletes the specified student from TutorContacts. The result panel confirms the deleted student's details.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​
* Missing, malformed, zero, negative, extra, or out-of-range indices are rejected.

Examples:
* `list` followed by `delete 2` deletes the 2nd student in TutorContacts.
* `find Bernice` followed by `delete 1` deletes the 1st student in the results of the `find` command.
* `delete 0` and `delete 1 2` are rejected.

### Scheduling a lesson: `schedule`

Sets the next lesson for the specified student. Each student has at most one scheduled lesson.

Format: `schedule INDEX at/YYYY-MM-DD HH:mm`

* Schedules a lesson for the student at the specified `INDEX`, which refers to the index number shown in the displayed
  student list. The index **must be a positive integer** 1, 2, 3, …​
* Works only on the active student list. If `list archived` is showing, run `list` first.
* The date and time use this computer's local time, with a 24-hour clock. For example, `2099-10-30 16:00` is 4pm on
  30 October 2099.
* The lesson must be in the future. A time in the current minute or earlier is rejected.
* If the student already has a lesson, the new lesson replaces it. The result panel shows the previous lesson time.
* Dates that do not exist, such as `2099-02-30`, and times outside `00:00`–`23:59` are rejected.
* Extra spaces around the value and between the date and time are ignored.
* The student card shows the lesson as `Lesson: 2099-10-30 16:00`, or `Lesson: Not scheduled` when the student has no
  lesson.
* Archiving a student keeps their lesson. The lesson is shown again when the student is restored with `unarchive`.

Examples:
* `list` followed by `schedule 2 at/2099-10-30 16:00` schedules a lesson for the 2nd student.
* `find Bernice` followed by `schedule 1 at/2099-11-02 09:30` schedules a lesson for the 1st student in the results of
  the `find` command.
* Invalid, because the time has passed: `schedule 1 at/2020-01-01 10:00`
* Invalid, because the date does not exist: `schedule 1 at/2099-02-30 10:00`
* Invalid, because the format is different: `schedule 1 at/30-10-2099 16:00` or `schedule 1 at/2099-10-30 4pm`
* Invalid, because the time is missing: `schedule 1 at/2099-10-30`
* Invalid, because the archived list is showing: `list archived` followed by `schedule 1 at/2099-10-30 16:00`

<div markdown="span" class="alert alert-info">:information_source: **Note:**
TutorContacts records only one upcoming lesson per student. Recurring lessons, reminders, lesson history, attendance,
calendar integration, and time-zone conversion are not supported.
</div>

### Clearing a lesson: `unschedule`

Clears the scheduled lesson of the specified student.

Format: `unschedule INDEX`

* Clears the lesson of the student at the specified `INDEX` in the displayed student list.
* Works only on the active student list. If `list archived` is showing, run `list` first.
* The index **must be a positive integer** 1, 2, 3, …​
* The result panel shows the lesson time that was cleared. If the student has no scheduled lesson, TutorContacts
  reports this and nothing changes.

Examples:
* `list` followed by `unschedule 2` clears the lesson of the 2nd student.
* `unschedule 0` and `unschedule 1 2` are rejected.

### Clearing all entries: `clear`

Clears all student records from TutorContacts.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

Example: `exit`

### Saving the data

TutorContacts automatically saves data changes. You do not need to save manually. The `help` command can be used even
when the data file cannot be read or written, and does not change that file.

### Editing the data file

TutorContacts data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes make the data file invalid, TutorContacts starts with an empty student list at the next run. The invalid file remains on disk when you use `help`; a data-changing command can replace it. We recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause TutorContacts to behave unexpectedly if a value does not satisfy its validation rules. Edit the data file only if you are confident that you can update it correctly.
</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install TutorContacts on the other computer, close it, and replace its `data/addressbook.json` file with the file from your previous TutorContacts home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
3. **Exceptionally long student details may be truncated in the student list.** This display limitation does not affect
   the stored value.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [l/EDUCATION_LEVEL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [t/TAG]` <br> e.g., `add n/James Ho p/22224444 s/Mathematics l/S3 gn/Mary Ho gp/81234567`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g., `edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Filter** | `filter [s/SUBJECT] [l/EDUCATION_LEVEL]`<br> e.g., `filter s/Mathematics l/S3`
**List** | `list`
**Schedule** | `schedule INDEX at/YYYY-MM-DD HH:mm`<br> e.g., `schedule 1 at/2099-10-30 16:00`
**Unschedule** | `unschedule INDEX`<br> e.g., `unschedule 1`
**Help** | `help`
**Exit** | `exit`
