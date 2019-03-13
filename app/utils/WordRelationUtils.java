package utils;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import vo.ChildRelNode;
import vo.ParentRelNode;
import vo.WordRelationNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordRelationUtils {

    private static WordRelationUtils ourInstance = new WordRelationUtils();

    public static WordRelationUtils getInstance() {
        return ourInstance;
    }

    private  WordRelationUtils() {

    }


    public Map<String, WordRelationNode> wordRelationsForSentenceMap (SemanticGraph sg) {

        Map<String, WordRelationNode> wordRelationsMap = new HashMap<>();

        Set<IndexedWord> indexedWords = sg.vertexSet();

        for (IndexedWord indexedWord : indexedWords) {
            String word = tokenizeString(indexedWord.word());
            if (wordRelationsMap.containsKey(word)) {
                updateWordRelationNode(wordRelationsMap.get(word), indexedWord, sg);
            } else {
                wordRelationsMap.put(word, createWordReationNode(indexedWord, sg));
            }

            System.out.println(wordRelationsMap.get(word).toJsonNode());
        }



        return wordRelationsMap;

    }

    public WordRelationNode createWordReationNode (IndexedWord indexedWord, SemanticGraph sg) {

        String word = indexedWord.word();
        WordRelationNode wordRelationNode = new WordRelationNode(word, tokenizeString(word));
        Set<ChildRelNode> descendants = descendantsSet(indexedWord, sg.descendants(indexedWord));
        Set<ChildRelNode> children = chidrenSetWithRel(indexedWord, sg.getChildren(indexedWord), sg);
        Set<ParentRelNode> parents = parentSetWithRel(sg.relns(indexedWord));

        wordRelationNode.setDecendents(descendants);
        wordRelationNode.setChildren(children);
        wordRelationNode.setParents(parents);

        return  wordRelationNode;
    }

    public WordRelationNode updateWordRelationNode (WordRelationNode wordRelationNode, IndexedWord indexedWord, SemanticGraph sg) {

        Set<ChildRelNode> descendants = descendantsSet(indexedWord, sg.descendants(indexedWord));
        Set<ChildRelNode> children = chidrenSetWithRel(indexedWord, sg.getChildren(indexedWord), sg);
        Set<ParentRelNode> parents = parentSetWithRel(sg.relns(indexedWord));

        wordRelationNode.getChildren().addAll(children);
        wordRelationNode.getDecendents().addAll(descendants);
        wordRelationNode.getParents().addAll(parents);

        return  wordRelationNode;
    }

    public Set<ChildRelNode> descendantsSet(IndexedWord indexedWord, Set<IndexedWord> descendants) {
        Set<ChildRelNode> descendantsSet = new HashSet<>();
        for (IndexedWord descendant : descendants) {
            if (! descendant.equals(indexedWord)) {
                descendantsSet.add(descendantsFromIndexedWord(descendant));
            }
        }

        return descendantsSet;
    }

    public Set<ChildRelNode> chidrenSetWithRel (IndexedWord indexedWord, Set<IndexedWord> children, SemanticGraph sg) {
        Set<ChildRelNode> childrenSet = new HashSet<>();
        for (IndexedWord child : children) {
            childrenSet.add(childRelFromIndexedWord(child, indexedWord, sg));
        }

        return childrenSet;
    }

    Set<ParentRelNode> parentSetWithRel (Set<GrammaticalRelation> relns) {
        Set<ParentRelNode> parentRelNodes = new HashSet<>();
        for (GrammaticalRelation rel : relns) {
            parentRelNodes.add(getParentRelNode(rel));
        }
        return parentRelNodes;
    }

    public ChildRelNode descendantsFromIndexedWord (IndexedWord indexedWord) {
        String word = indexedWord.word();
        return new ChildRelNode(word, tokenizeString(word));
    }

    public ChildRelNode childRelFromIndexedWord (IndexedWord child, IndexedWord indexedWord, SemanticGraph sg) {
        String word = child.word();
        GrammaticalRelation grammaticalRelation = sg.reln(indexedWord, child);
        return new ChildRelNode(word, tokenizeString(word), getRelationString(grammaticalRelation));
    }

    public ParentRelNode getParentRelNode (GrammaticalRelation rel) {
        return new ParentRelNode(rel.getShortName());
    }

    private String tokenizeString (String string) {
        return string != null ? string.toLowerCase() : null;
    }

    private String getRelationString (GrammaticalRelation grammaticalRelation) {
        return grammaticalRelation != null ?  grammaticalRelation.getShortName() : null;
    }
}
