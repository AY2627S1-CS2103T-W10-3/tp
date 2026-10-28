package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_tagsAreRelevantToTutors() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(SampleDataUtil.getTagSet("examPrep"), samplePersons[0].getTags());
        assertEquals(SampleDataUtil.getTagSet("online", "weekend"), samplePersons[1].getTags());
        assertEquals(SampleDataUtil.getTagSet("group"), samplePersons[2].getTags());
        assertEquals(SampleDataUtil.getTagSet("weekend"), samplePersons[3].getTags());
        assertEquals(SampleDataUtil.getTagSet("online"), samplePersons[4].getTags());
        assertEquals(SampleDataUtil.getTagSet(), samplePersons[5].getTags());
    }
}
