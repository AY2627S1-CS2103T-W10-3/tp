package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.Storage;
import seedu.address.testutil.PersonBuilder;

public class MainAppTest {

    private static final String CURRENT_FILE_NAME = "tutorcontacts.json";
    private static final String LEGACY_FILE_NAME = "addressbook.json";
    private static final Path DATA_FILE = Path.of("data", "tutorcontacts.json");

    @TempDir
    private Path dataDirectory;

    @Test
    public void resolveAddressBookFilePath_noExistingFile_returnsTutorContactsPath() {
        assertEquals(dataDirectory.resolve(CURRENT_FILE_NAME), MainApp.resolveAddressBookFilePath(dataDirectory));
    }

    @Test
    public void resolveAddressBookFilePath_tutorContactsFileExists_returnsTutorContactsPath() throws IOException {
        Path tutorContactsFile = Files.createFile(dataDirectory.resolve(CURRENT_FILE_NAME));

        assertEquals(tutorContactsFile, MainApp.resolveAddressBookFilePath(dataDirectory));
    }

    @Test
    public void resolveAddressBookFilePath_onlyLegacyFileExists_returnsLegacyPath() throws IOException {
        Path legacyFile = Files.createFile(dataDirectory.resolve(LEGACY_FILE_NAME));

        assertEquals(legacyFile, MainApp.resolveAddressBookFilePath(dataDirectory));
    }

    @Test
    public void resolveAddressBookFilePath_bothFilesExist_prefersCurrentWithoutChanges() throws IOException {
        Path tutorContactsFile = dataDirectory.resolve(CURRENT_FILE_NAME);
        Path legacyFile = dataDirectory.resolve(LEGACY_FILE_NAME);
        Files.writeString(tutorContactsFile, "current data");
        Files.writeString(legacyFile, "legacy data");

        assertEquals(tutorContactsFile, MainApp.resolveAddressBookFilePath(dataDirectory));
        assertEquals("current data", Files.readString(tutorContactsFile));
        assertEquals("legacy data", Files.readString(legacyFile));
    }

    @Test
    public void loadInitialData_validData_returnsDataWithoutWarning() {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(new PersonBuilder().withName("Valid Student").build());
        Storage storage = new DataLoadingStorage(Optional.of(addressBook), null);

        MainApp.InitialData result = MainApp.loadInitialData(storage);

        assertSame(addressBook, result.addressBook());
        assertTrue(result.warning().isEmpty());
    }

    @Test
    public void loadInitialData_missingFile_returnsSampleDataWithoutWarning() {
        Storage storage = new DataLoadingStorage(Optional.empty(), null);

        MainApp.InitialData result = MainApp.loadInitialData(storage);

        assertEquals(SampleDataUtil.getSampleAddressBook(), result.addressBook());
        assertTrue(result.warning().isEmpty());
    }

    @Test
    public void loadInitialData_invalidData_returnsEmptyDataWithStartupWarning() {
        DataLoadingException loadFailure = new DataLoadingException(new IOException("Invalid JSON"));
        Storage storage = new DataLoadingStorage(Optional.empty(), loadFailure);

        MainApp.InitialData result = MainApp.loadInitialData(storage);

        assertTrue(result.addressBook().getPersonList().isEmpty());
        assertEquals(Optional.of(String.format(Messages.MESSAGE_DATA_LOAD_FAILED, DATA_FILE)), result.warning());
    }

    /** Storage stub whose student-data result or failure can be configured for startup tests. */
    private static class DataLoadingStorage implements Storage {
        private final Optional<ReadOnlyAddressBook> data;
        private final DataLoadingException loadFailure;

        DataLoadingStorage(Optional<ReadOnlyAddressBook> data, DataLoadingException loadFailure) {
            this.data = data;
            this.loadFailure = loadFailure;
        }

        @Override
        public Path getUserPrefsFilePath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getAddressBookFilePath() {
            return DATA_FILE;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            if (loadFailure != null) {
                throw loadFailure;
            }
            return data;
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) {
            throw new UnsupportedOperationException();
        }
    }
}
