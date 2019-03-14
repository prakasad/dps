import com.google.common.collect.ImmutableSet;
import model.SeachModel;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SeachModelTest {
    @Test
    public void getIntersectionOfSentencesTest() {
        final SeachModel seachModel = new SeachModel();
        final Set<String> set1 = ImmutableSet.of("john", "ate", "the", "apple");
        final Set<String> set2 = ImmutableSet.of("an", "apple", "a", "day", "keep", "the", "doctor", "away");
        final Set<String> intersected = seachModel.getIntersectionOfSentences(new HashSet<>(set1), new HashSet<>(set2));
        assertEquals(intersected, ImmutableSet.of("the", "apple"));
    }

}