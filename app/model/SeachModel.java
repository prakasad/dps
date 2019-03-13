package model;

import Exceptions.InputDataErrException;
import dbo.SentenceDao;
import dbo.WordRelationDao;
import dbo.WordSentenceIdDao;
import javassist.tools.rmi.ObjectNotFoundException;
import play.Logger;
import utils.WordRelationUtils;
import vo.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SeachModel {

    public Set<String> W2childW1Search(String w1, String w2) throws ObjectNotFoundException, InputDataErrException {
        Set<String> matchedSetenceIds = new HashSet<>();
        w1 = WordRelationUtils.tokenizeString(w1);
        w2 = WordRelationUtils.tokenizeString(w2);


        Set<String> sentencesToAnalyse = getIntersectionOfSentences(getHashForWordOccurrence(w1), getHashForWordOccurrence(w2));

        for (String hash : sentencesToAnalyse) {
            if (isW2decendentW1(w1, w2, hash)) {
                matchedSetenceIds.add(hash);
            }
        }
        return matchedSetenceIds;
    }

    public Set<String> W2relationW1Search(String w1, String w2, String rel) throws ObjectNotFoundException, InputDataErrException {
        Set<String> matchedSetenceIds = new HashSet<>();
        w1 = WordRelationUtils.tokenizeString(w1);
        w2 = WordRelationUtils.tokenizeString(w2);

        Set<String> sentencesToAnalyse = getIntersectionOfSentences(getHashForWordOccurrence(w1), getHashForWordOccurrence(w2));

        for (String hash : sentencesToAnalyse) {
            if (isW2relationW1(w1, w2, rel, hash)) {
                matchedSetenceIds.add(hash);
            }
        }
        return matchedSetenceIds;
    }

    public Set<String> W1relationAnyParent(String w1, String rel) throws ObjectNotFoundException, InputDataErrException {
        Set<String> matchedSetenceIds = new HashSet<>();
        w1 = WordRelationUtils.tokenizeString(w1);

        Set<String> sentencesToAnalyse = getHashForWordOccurrence(w1);

        for (String hash : sentencesToAnalyse) {
            if (isW2relationAnyParent(w1, rel, hash)) {
                matchedSetenceIds.add(hash);
            }
        }
        return matchedSetenceIds;
    }

    public Set<String> W1andW3childrenW2Search(String w1, String w2, String w3) throws InputDataErrException, ObjectNotFoundException {
        w1 = WordRelationUtils.tokenizeString(w1);
        w2 = WordRelationUtils.tokenizeString(w2);
        w3 = WordRelationUtils.tokenizeString(w3);

        Set<String> w1andW2 = W2childW1Search(w2, w1);
        Set<String> w3andW2 = W2childW1Search(w2, w3);

        // Intersection of both the values.
        //if (w1andW2 != null) {
        w1andW2.retainAll(w3andW2);
        //}
        return w1andW2;
    }

    public Set<String> matchedSentences (Set<String> hashs) throws ObjectNotFoundException {
        Logger.info(String.format("Matched String hashs %s", hashs));
        Set<String> sentences = new HashSet<>();
        for (String hash : hashs) {
            SentenceVo sentenceVo = SentenceDao.getSentenceVo(hash);
            sentences.add(sentenceVo.getSentence());
        }
        return sentences;
    }

    public boolean isW2decendentW1(String w1, String w2, String hash) throws ObjectNotFoundException, InputDataErrException {
        Map<String, WordRelationNode> wordRelationNodeMap = getWordRelationMap(hash);

        WordRelationNode wordRelationNode = wordRelationNodeMap.get(w1);

        if (wordRelationNode == null) {
            throw new InputDataErrException(String.format("Word %s should be present for sentence %s", w1, hash));
        }
        Set<ChildRelNode> decendents = wordRelationNode.getDecendents();
        for (ChildRelNode decendent : decendents ) {
            if (decendent.tokenziedWord.equals(w2)){
                return true;
            }
        }
        return false;
    }

    public boolean isW2relationW1(String w1, String w2, String rel, String hash) throws ObjectNotFoundException, InputDataErrException {
        Map<String, WordRelationNode> wordRelationNodeMap = getWordRelationMap(hash);

        WordRelationNode wordRelationNode = wordRelationNodeMap.get(w1);

        if (wordRelationNode == null) {
            throw new InputDataErrException(String.format("Word %s should be present for sentence %s", w1, hash));
        }

        Set<ChildRelNode> children = wordRelationNode.getChildren();
        for (ChildRelNode child : children ) {
            if (child.tokenziedWord.equals(w2) && child.childRelation.equals(rel)){
                return true;
            }
        }
        return false;


    }

    private boolean isW2relationAnyParent(String w1, String rel, String hash) throws InputDataErrException, ObjectNotFoundException {
        Map<String, WordRelationNode> wordRelationNodeMap = getWordRelationMap(hash);

        WordRelationNode wordRelationNode = wordRelationNodeMap.get(w1);

        if (wordRelationNode == null) {
            throw new InputDataErrException(String.format("Word %s should be present for sentence %s", w1, hash));
        }

        Set<ParentRelNode> parents = wordRelationNode.getParents();
        for (ParentRelNode parent : parents ) {
            if (parent.parentRelation.equals(rel)){
                return true;
            }
        }
        return false;
    }


    public Set<String> getHashForWordOccurrence(String word) throws ObjectNotFoundException {

        WordSentenceIdReverseMapVo wordSentenceIdReverseMapVo = new WordSentenceIdReverseMapVo();
        if (WordSentenceIdDao.exists(word)) {
            wordSentenceIdReverseMapVo =  WordSentenceIdDao.getWordSentenceIdReverseMapVo(word);
        }
        return wordSentenceIdReverseMapVo.getSentenceIds();
    }

    public Set<String> getIntersectionOfSentences (Set<String> set1, Set<String> set2) {
        set1.retainAll(set2);
        return  set1;
    }

    public Map<String, WordRelationNode> getWordRelationMap(String hash) throws ObjectNotFoundException {

        WordRelationMapVo wordRelationMapVo = WordRelationDao.getWordRelationMapVo(hash);
        // TODO handle null case here.
        return wordRelationMapVo.wordRelationNodeMap;
    }
}
