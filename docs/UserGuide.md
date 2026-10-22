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

   * `add n/John Doe lf/Fractions na/Complete worksheet 2` : Adds a student with optional learning context.

   * `archive 1` : Moves the 1st student shown out of active workflows without deleting the record.

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

* Extraneous parameters for commands that take no parameters, such as `help`, `exit`, and `clear`, are ignored.<br>
  For example, `help 123` is interpreted as `help`.
  `list` accepts only no argument or the exact argument `archived`; for example, `list 1` is rejected.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows the command formats and important rules in the result panel and a help window. The help text is bundled with
TutorContacts, so `help` remains available without internet access and when the data file cannot be loaded. It does not
write to the data file. The help window also has a button to copy the online User Guide link.

Format: `help`


### Adding a student: `add`

Adds a student to TutorContacts. Only the student name is required; the remaining fields are optional.

Format: `add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [l/EDUCATION_LEVEL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [lf/LEARNING_FOCUS] [na/NEXT_ACTION] [t/TAG]`

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
| `lf/` | Current learning focus. Optional; 1–200 characters without control characters. |
| `na/` | Next action. Optional; 1–200 characters without control characters. |
| `t/` | One optional tag; letters and digits only. |

Subject and education level may be supplied independently. Education levels are case-insensitive when entered and are
displayed in uppercase. If either field is omitted, the student card displays `Not provided` for that field.

Learning focus and next action may also be supplied independently. Ordinary spaces and punctuation are accepted.
Leading and trailing whitespace is removed, and repeated ordinary spaces are stored as one space. Tabs, line breaks,
other control characters, blank values, and values longer than 200 characters are rejected. An omitted learning field
is displayed as `Not provided`.

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
* Learning context: `add n/Alex Tan lf/Quadratic graphs na/Complete worksheet 4`
* Two students may share contacts: `add n/Ben Tan p/81234567 gn/Mary Tan gp/91234567` followed by `add n/Chloe Tan p/81234567 gn/Mary Tan gp/91234567`
* Invalid, because `gn/` is missing: `add n/Sam Lee gp/81234567`
* Invalid, because a prefix repeats: `add n/Sam Lee p/81234567 p/92345678`
* Invalid, because the education level is unsupported: `add n/Sam Lee l/University`
* Invalid, because the subject is empty: `add n/Eva Lim s/ l/P6`
* Invalid, because a learning focus supplied to `add` cannot be empty: `add n/Eva Lim lf/`
* Invalid guardian email: `add n/Sam Lee gn/Pat Lee ge/pat.example.com`
* Invalid, because of unexpected text: `add surprise n/Sam Lee` or `add n/Sam Lee x/value`

Rejected `add` commands leave the displayed records and saved data unchanged.

### Listing all students: `list`

Shows all active student records in their saved order. Archived students are excluded. If there are no records,
TutorContacts shows guidance for adding one. If records exist only in the archive, it suggests `list archived` instead.
Student cards show the student's recorded contact, tutoring, guardian, learning-context, and lesson details. Omitted
details are shown as `Not provided`, and a student without a lesson is shown as `Not scheduled`.

After a command is submitted, the command box retains keyboard focus so you can type the next command immediately.
Use the mouse or Tab to focus the list when you want to scroll it or use its normal list navigation keys.

Format: `list`

Example: `list`

### Editing a student: `edit`

Updates or clears details of an existing student without recreating the record.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [l/EDUCATION_LEVEL]`
`[gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [lf/LEARNING_FOCUS] [na/NEXT_ACTION] [t/TAG]…​`

* `INDEX` refers to the index shown in the current result view, including a view produced by `find` or `filter`.
  It **must be a positive integer** 1, 2, 3, …​
* Supply at least one field prefix. A prefix with a value replaces that field; an omitted prefix leaves its field
  unchanged.
* Leave an optional prefix empty to clear that field, for example `e/` to clear the student's email. `n/` cannot be
  empty because every student must have a name.
* Clearing `gn/` removes the guardian name, phone, and email together. `gp/` or `ge/` cannot contain a value unless a
  guardian name already exists or is supplied in the same command.
* Supplying one or more `t/` prefixes replaces all existing tags. Use `t/` by itself to clear all tags.
* Repeated single-valued prefixes, unsupported prefixes, invalid values, and edits that would create a duplicate
  normalized student name are rejected without changing displayed or saved data.
* Enclose a free-text value in double quotes if it contains separate prefix-like text, for example
  `a/"Block n/7"` or `s/"English l/Literature"`.
* After a successful edit the full student list is shown again, so a `find` or `filter` view is cleared. For example,
  `find Betsy` followed by `edit 1 p/91234567` ends with every student displayed rather than only the search results.

Examples:
* `edit 1 p/91234567 s/H2 Mathematics l/JC2` updates three fields of the first displayed student while preserving
  every omitted field.
* `find Betsy` followed by `edit 1 lf/Quadratic equations na/Complete worksheet 4` updates the first student in the
  search results.
* `edit 2 e/ a/ lf/ na/ t/` clears the email, address, learning focus, next action, and tags of the second displayed
  student.
* `edit 1 gn/` clears the guardian and both guardian contact fields.
* Invalid, because the name is required: `edit 1 n/`
* Invalid, because a guardian phone requires a guardian name: `edit 1 gn/ gp/91234567`
* Invalid if `Alex Tan` already belongs to another student: `edit 1 n/Alex Tan`

### Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive; for example, `hans` matches `Hans`.
* Keyword order does not matter; for example, `Hans Bo` matches `Bo Hans`.
* The search considers only names.
* Partial names match; for example, `Han` matches `Hans`.
* Students matching at least one keyword are returned (an `OR` search); for example, `Hans Bo` returns `Hans Gruber` and `Bo Yang`.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Archiving a student: `archive`

Moves a former student out of active workflows while preserving the complete record and its saved position.

Format: `archive INDEX`

* `INDEX` refers to the student shown in the current active result view, including `list`, `find`, or `filter` results.
* Archived students do not appear in active `list`, `find`, or `filter` results.
* The command is rejected from the archived view. Run `list` first to return to an active view.
* Archiving is reversible and is different from permanent `delete`.

Example: `find Alex` followed by `archive 1` archives the first student in those results.

### Listing archived students: `list archived`

Shows every archived student in saved order with consecutive displayed indices. If none exist, TutorContacts explains
that `archive INDEX` can be used from an active view.

Format: `list archived`

### Restoring an archived student: `unarchive`

Restores a student from the archived view to active workflows without changing the student's recorded fields.

Format: `unarchive INDEX`

* Run `list archived` first; `INDEX` refers to that displayed archived list.
* The command is rejected from an active view.
* The restored student returns to the original saved-order position in active results.

Example: `list archived` followed by `unarchive 1` restores the first archived student.

Complete lifecycle example:

1. Run `list` and note the student shown at index 1.
1. Run `archive 1`; that student disappears from active results.
1. Run `list archived`; the archived student appears with a consecutive archived-view index.
1. Run `unarchive 1`, then `list`; the complete record returns to its original saved-order position.

### Deleting a student: `delete`

Permanently deletes the specified student from TutorContacts. The result panel confirms the deleted student's details.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* Deletion works in both active and archived views. Unlike archive, it cannot be reversed.
* The index **must be a positive integer** 1, 2, 3, …​
* Missing, malformed, zero, negative, extra, or out-of-range indices are rejected.

Examples:
* `list` followed by `delete 2` deletes the 2nd student in TutorContacts.
* `find Betsy` followed by `delete 1` deletes the 1st student in the results of the `find` command.
* `list archived` followed by `delete 1` permanently deletes the 1st archived student instead of restoring it.
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
* `find Betsy` followed by `schedule 1 at/2099-11-02 09:30` schedules a lesson for the 1st student in the results of
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
**Add** | `add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [l/EDUCATION_LEVEL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [lf/LEARNING_FOCUS] [na/NEXT_ACTION] [t/TAG]` <br> e.g., `add n/James Ho lf/Fractions na/Complete worksheet 2`
**Archive** | `archive INDEX`<br> e.g., `archive 1`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/SUBJECT] [l/EDUCATION_LEVEL] [gn/GUARDIAN_NAME] [gp/GUARDIAN_PHONE] [ge/GUARDIAN_EMAIL] [lf/LEARNING_FOCUS] [na/NEXT_ACTION] [t/TAG]…​`<br> e.g., `edit 2 p/91234567 s/Mathematics l/S3`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list` or `list archived`
**Unarchive** | `unarchive INDEX`<br> e.g., `unarchive 1`
**Schedule** | `schedule INDEX at/YYYY-MM-DD HH:mm`<br> e.g., `schedule 1 at/2099-10-30 16:00`
**Unschedule** | `unschedule INDEX`<br> e.g., `unschedule 1`
**Help** | `help`
**Exit** | `exit`
