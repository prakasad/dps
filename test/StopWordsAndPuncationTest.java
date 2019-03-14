import edu.stanford.nlp.ling.IndexedWord;
import org.junit.Test;
import utils.StopWordsAndPuncation;

import static org.junit.Assert.assertEquals;

public class StopWordsAndPuncationTest {
    @Test
    public void isValidWordToIndexTest() {
        final StopWordsAndPuncation stopWordsAndPuncation = new StopWordsAndPuncation();
        final IndexedWord indexedWord = new IndexedWord();
        assertEquals(stopWordsAndPuncation.isValidWordToIndex(indexedWord), false);
    }
}