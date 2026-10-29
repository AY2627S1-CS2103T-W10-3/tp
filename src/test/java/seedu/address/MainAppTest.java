package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class MainAppTest {

    private static final String CURRENT_FILE_NAME = "tutorcontacts.json";
    private static final String LEGACY_FILE_NAME = "addressbook.json";

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
}
