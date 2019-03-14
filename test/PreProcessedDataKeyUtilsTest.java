import org.junit.Test;
import utils.PreProcessedDataKeyUtils;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

public class PreProcessedDataKeyUtilsTest {
    @Test
    public void sentenceToUnquieHashIdTest() throws NoSuchAlgorithmException {
        final PreProcessedDataKeyUtils processedDataKeyUtils = new PreProcessedDataKeyUtils();
        final String hash = processedDataKeyUtils.sentenceToUnquieHashId("John ate the apple");
        assertEquals(hash, "9d17b62a");

    }
}