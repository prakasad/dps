package utils;

import Exceptions.InputDataErrException;
import dbo.WordSentenceIdDao;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import javassist.tools.rmi.ObjectNotFoundException;
import vo.WordSentenceIdReverseMapVo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class StopWordsAndPuncation {

    public static final String puncations = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    //TODO add stop words
    public static final Set<String> stopwords = new HashSet<String>();


    public void updateReveseIndexMap (SemanticGraph sg, String hash) throws ObjectNotFoundException, NoSuchAlgorithmException, InputDataErrException, IOException {
        for (IndexedWord indexedWord : sg.vertexSet()) {
            if (isValidWordToIndex(indexedWord)) {
                updateWordSentenceIdReverseMap(indexedWord, hash);
            }

        }
    }

    public boolean isValidWordToIndex(IndexedWord indexedWord) {
        String word = indexedWord.word();

        if (word == null || puncations.contains(word) || stopwords.contains(word)) {
            return false;
        }
        return true;
    }

    public void updateWordSentenceIdReverseMap (IndexedWord indexedWord, String hash) throws ObjectNotFoundException, NoSuchAlgorithmException, InputDataErrException, IOException {
        WordSentenceIdDao wordSentenceIdsDao = new WordSentenceIdDao();
        String word = indexedWord.word();
        word = WordRelationUtils.tokenizeString(word);
        WordSentenceIdReverseMapVo index = new WordSentenceIdReverseMapVo(word);
        if (wordSentenceIdsDao.exists(word)) {
            index =  wordSentenceIdsDao.getWordSentenceIdReverseMapVo(word);
        }

        Set<String> sentanceIds =  index.getSentenceIds();
        sentanceIds.add(hash);

        wordSentenceIdsDao.insertWordSentenceIdReverseMapVo(index);


    }

}
