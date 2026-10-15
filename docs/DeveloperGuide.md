---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* TutorContacts is based on the [AddressBook-Level3](https://github.com/se-edu/addressbook-level3) project created by the [SE-EDU initiative](https://se-education.org/).
* TutorContacts uses [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), and [JUnit 5](https://junit.org/junit5/).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in `docs/diagrams`. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. Before executing the command, `LogicManager` takes a snapshot of the address book. The command can then communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. `LogicManager` saves the updated address book after the command succeeds. If saving fails, it restores the snapshot before reporting the existing save-failure message. This keeps the records displayed by the UI consistent with the records that were last saved successfully.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The alternative, arguably more object-oriented, design below keeps a unique list of tags in `AddressBook`, and each `Person` references tags from that list. This lets `AddressBook` maintain one `Tag` object per unique tag instead of each `Person` holding its own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2627S1-CS2103T-W10-3/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add command validation and offline help

`AddCommandTokenizer` scans for supported prefix tokens at the start of an argument or after whitespace. It rejects text before the first prefix and unknown prefix-like tokens, then stores field values in an `ArgumentMultimap`. This lets `AddCommandParser` accept the ten MVP prefixes in any order without treating an unknown token as part of the preceding value.

Validation follows a fixed order: token structure, required `n/`, repeated prefixes, guardian-name dependency, then individual field values. Duplicate-prefix errors list the repeated tokens in a stable order. Names, subjects, and addresses have surrounding whitespace removed and internal whitespace collapsed before validation. `Person#isSamePerson` compares names after the same whitespace normalization and case folding; phone, email, and guardian details do not determine whether two students are duplicates. An invalid command fails before `AddCommand` can update the model or save data.

`HelpCommand` keeps the command formats in bundled text so help is available without a network connection. `LogicManager` returns the help result without saving the address book, which also allows help when the existing data file cannot be loaded. `HelpWindow` displays the same text directly in a scrollable JavaFX control.

### Guardian details

TutorContacts represents a student's guardian as an immutable `Guardian` value object containing a required `Name` and optional `Phone` and `Email` values. `Person` stores the whole value as `Optional<Guardian>`, so the model cannot represent guardian contact details without a guardian name.

The `add` command accepts the feature-specific prefixes `gn/`, `gp/`, and `ge/`. `AddCommandParser` first rejects duplicate single-valued prefixes, then enforces the dependency that `gp/` and `ge/` require `gn/`. `ParserUtil` applies the existing name, phone, and email validation rules before constructing the `Guardian`.

`JsonAdaptedPerson` stores guardian name, phone, and email as separate nullable JSON properties. All three properties may be absent when loading older files. If a saved file contains a guardian phone or email without a guardian name, loading fails rather than creating an invalid domain object.

`PersonCard` displays all three guardian fields. Missing guardian values are shown as `Not provided` only in the UI and command feedback; that display text is never written to the data file. Existing commands that replace a `Person`, such as `edit`, must carry the unchanged guardian value forward.

### Subject and education level

TutorContacts represents tutoring context using immutable `Subject` and `EducationLevel` value objects. `Person` stores each value independently as an `Optional`, allowing a tutor to record either field, both fields, or neither field.

The `add` command accepts `s/` for a subject and `l/` for an education level. `ParserUtil` trims both values before validation. Subjects must be non-blank, contain no control characters, and contain at most 100 characters. Education levels accept `P1`–`P6`, `S1`–`S5`, or `JC1`–`JC2` case-insensitively and are normalized to uppercase.

`JsonAdaptedPerson` stores the two values as separate nullable JSON properties, so valid data files created before these fields were introduced remain loadable. `PersonCard` and command feedback display `Not provided` for an absent value without writing that placeholder to storage. Commands that replace a `Person`, such as `edit`, preserve both values unchanged.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is an independent private tutor managing multiple students
* needs to keep student and guardian contact details together
* needs to record tutoring information such as subjects and education levels
* primarily manages one-to-one tutoring arrangements
* uses a desktop or laptop as the main working device
* prefers typing commands to navigating a mouse-driven interface
* is comfortable using a CLI-style application

**Value proposition**: TutorContacts enables independent private tutors to manage student, guardian, and tutoring information quickly in one place using a typing-oriented interface.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​ | So that I can…​ |
| -------- | -------- | ------------ | ---------------- |
| `* * *` | private tutor | add a student | keep a record of a student I tutor |
| `* * *` | private tutor | record a student's contact details | contact the student when necessary |
| `* * *` | private tutor | record a student's guardian details | contact the guardian when necessary |
| `* * *` | private tutor | add a student even when some details are unavailable | create useful records without waiting for complete information |
| `* * *` | private tutor | record a student's subject and education level | remember the context in which I tutor the student |
| `* * *` | private tutor | list all students with their recorded details | review my student information before a lesson |
| `* * *` | private tutor | delete an obsolete or incorrect student record | keep my records relevant and accurate |
| `* * *` | private tutor | have my data saved locally | retain my records after closing the application |
| `* *` | new user | view command instructions | learn or recall how to use the application |
| `* *` | private tutor | edit an existing student record | correct information without deleting and recreating the record |
| `* *` | private tutor | find a student by name | retrieve a record without scanning the full list |
| `* *` | private tutor | tag and filter students by subject or education level | retrieve a relevant group of students quickly |
| `* *` | private tutor | record upcoming lessons | keep track of tutoring arrangements |
| `* *` | private tutor | record learning focuses and follow-up actions | remember what should be covered in future lessons |
| `*` | private tutor | archive former students | retain old records without showing them in my active list |
| `*` | private tutor | record group tuition arrangements | manage students who attend lessons together |
| `*` | private tutor | associate multiple guardians with a student | retain additional family contact details |

### Use cases

For all use cases below, the **System** is `TutorContacts` and the **Actor** is the private tutor, unless otherwise specified.

**Use case: UC01 — Add a student**

**MSS**

1. User requests to add a student and provides the student's details, which may include a subject and education level.
2. TutorContacts validates the supplied details.
3. TutorContacts adds the student.
4. TutorContacts saves the updated student data.
5. TutorContacts displays the added student and reports that the operation was successful.

    Use case ends.

**Extensions**

* 1a. The user omits the student's name.

  * 1a1. TutorContacts reports that the name is required.

  Use case ends.

* 1b. The user provides an invalid field value.

  * 1b1. TutorContacts reports the invalid field and its required format.

  Use case ends.

* 1c. The user provides guardian contact details without a guardian name.

  * 1c1. TutorContacts reports that a guardian name is required when guardian contact details are provided.

  Use case ends.

* 1d. The user provides an unsupported education level or an invalid subject.

  * 1d1. TutorContacts reports the relevant validation rule.

  Use case ends.

* 2a. A student with the same normalized name already exists.

  * 2a1. TutorContacts reports the duplicate student.
  * 2a2. TutorContacts leaves the existing records unchanged.

  Use case ends.

* 4a. TutorContacts cannot save the updated data.

  * 4a1. TutorContacts reports the save failure.
  * 4a2. TutorContacts leaves the displayed and saved records unchanged.

  Use case ends.

**Use case: UC02 — Review student records**

**MSS**

1. User requests to list students.
2. TutorContacts retrieves the student records.
3. TutorContacts displays all students and their recorded contact, guardian, subject, and education-level details.

    Use case ends.

**Extensions**

* 2a. There are no student records.

  * 2a1. TutorContacts informs the user that no students have been recorded.

  Use case ends.

* 2b. Student data could not be loaded when TutorContacts started.

  * 2b1. TutorContacts informs the user that student data is unavailable.

  Use case ends.

**Use case: UC03 — Delete a student**

**MSS**

1. User requests to list students.
2. TutorContacts displays the students with their current indices.
3. User requests to delete the student at a specified index.
4. TutorContacts validates the specified index.
5. TutorContacts deletes the selected student.
6. TutorContacts saves the updated student data.
7. TutorContacts displays the remaining students and reports that the operation was successful.

    Use case ends.

**Extensions**

* 2a. There are no student records.

  Use case ends.

* 3a. The supplied index is not a positive integer in the required format.

  * 3a1. TutorContacts reports the required command format.

  Use case ends.

* 4a. The supplied index does not identify a displayed student.

  * 4a1. TutorContacts reports that no student exists at that index.

  Use case resumes at step 2.

* 6a. TutorContacts cannot save the updated data.

  * 6a1. TutorContacts reports the save failure.
  * 6a2. TutorContacts leaves the displayed and saved records unchanged.

  Use case ends.

### Non-Functional Requirements

1. TutorContacts should work on Windows, Linux, and macOS systems that have Java `25` installed.
2. TutorContacts should be packaged as a single portable JAR file, require no installer, and not exceed 100 MB.
3. TutorContacts should be designed for use by a single independent private tutor and should not support concurrent or shared-user access.
4. TutorContacts should not require an Internet connection for its core contact-management features.
5. TutorContacts should store user data locally in a human-editable text file.
6. TutorContacts should not require a database management system or a remote server operated by the development team.
7. TutorContacts should be able to manage at least 100 student records without noticeable sluggishness during typical usage.
8. A user with above-average typing speed should be able to perform common contact-management tasks faster using commands than by relying primarily on mouse interactions.
9. TutorContacts should work well at screen resolutions of `1920 × 1080` or higher with display scaling of 100% or 125%.
10. TutorContacts should remain functionally usable at screen resolutions of `1280 × 720` or higher with display scaling of 150%.
11. Student information should remain readable under typical field lengths at supported window sizes.
12. TutorContacts should preserve data integrity when handling invalid commands or data by displaying a clear error message and leaving existing student records unchanged.
13. A failure to load or save data should not cause existing valid data to be silently overwritten.

### Glossary

* **CLI**: Command-line interface, an interface in which users perform actions by typing commands.
* **Education level**: The student's current school level, such as `P6`, `S3`, or `JC1`.
* **Guardian**: A parent or other responsible adult whose contact details are associated with a student.
* **Independent private tutor**: A tutor who manages their own students and tutoring arrangements rather than relying on an organization to manage them.
* **Student record**: The information stored for one student, including the student's name and any recorded contact, guardian, subject, and education-level details.
* **Subject**: The academic subject for which the student is receiving tuition.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Open a terminal in that folder and run `java -jar addressbook.jar`.<br>
      Expected: The GUI opens with a set of sample students. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. Exiting from the command box

   1. Enter `exit`.<br>
      Expected: TutorContacts closes normally.

### Adding a student

1. Adding a student with every MVP field

   1. Test case: `add n/Alex Tan p/81234567 e/alex@example.com a/21 Dover Road s/H2 Mathematics l/jc1 gn/Mary Tan gp/91234567 ge/mary@example.com t/active`<br>
      Expected: The student is added. The card and result panel show all supplied details, and the education level is displayed as `JC1`.

   1. Test case: `add n/Ben Lee`<br>
      Expected: The student is added. Optional fields are displayed as `Not provided`.

1. Rejecting invalid student details

   1. Test case: `add n/Chris Lim gp/81234567`<br>
      Expected: The command is rejected because a guardian phone requires `gn/`. Existing student records remain unchanged.

   1. Test case: `add n/Chris Lim l/University`<br>
      Expected: The command is rejected with the education-level constraint. Existing student records remain unchanged.

### Student-list UI, keyboard, and deletion feedback

1. List refresh and keyboard operation

   1. Prerequisites: At least two students are listed.

   1. Test case: Run `edit 1 n/Amy Tan`, then `delete 1`.<br>
      Expected: The edited card refreshes, then the deleted card disappears immediately. The result panel begins with `Deleted student:` and identifies the deleted student.

   1. Test case: Submit `list`, then immediately type another command.<br>
      Expected: The command box remains focused after the successful command. Submit an invalid command such as `delete 0`; it remains focused and displays an error.

   1. Test case: Focus the student list using the mouse or Tab, then use arrow keys and Page Up/Page Down.<br>
      Expected: Normal list selection and scrolling work; command entry remains available after returning focus to the command box.

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command, with multiple students in the list.

   1. Test case: `delete 1`<br>
      Expected: The first student is deleted from the list. The result panel identifies the deleted student.

   1. Test case: `delete 0`<br>
      Expected: No student is deleted. The result panel shows error details.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Saving data

1. Saving and loading student records

   1. Add a student containing contact, guardian, subject, and education-level details. Close TutorContacts and launch the same JAR again from the same folder.<br>
      Expected: The student and every recorded field are restored from `data/addressbook.json`.

1. Dealing with a missing data file

   1. Close TutorContacts and move `data/addressbook.json` to a backup location. Relaunch TutorContacts.<br>
      Expected: TutorContacts starts with its sample student data. Executing a data-changing command creates a new data file.

1. Dealing with a corrupted data file

   1. Close TutorContacts, back up `data/addressbook.json`, and replace its contents with invalid JSON. Relaunch TutorContacts.<br>
      Expected: TutorContacts starts with an empty student list rather than loading invalid records.

   1. Enter `help`, close TutorContacts, and inspect the corrupted file.<br>
      Expected: Help remains available, and the corrupted file has not been overwritten.
