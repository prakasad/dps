package model;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SentanceSearchModel {
    //TODO: remvoe later
    public  static final String sentenceID = "asdasdasfasz";

    public SemanticGraph semanticGraph;

    public  SentanceSearchModel(SemanticGraph semanticGraph) {
        this.semanticGraph = semanticGraph;
    }

    public Set<String> W2childW1Search(String w1, String w2) {
        Set<String> matchedSetenceIds = new HashSet<>();
        List<IndexedWord> indexedWords= getAllNodesByWordPattern(w1, semanticGraph.vertexSet());
        for (IndexedWord indexedWord1 : indexedWords) {
            Set<IndexedWord> descendants = semanticGraph.descendants(indexedWord1);
            if (isWordDescendants(w2, descendants)) {
                matchedSetenceIds.add(sentenceID);
            }
        }
        return matchedSetenceIds;

    }

    public Set<String> W2realtionW1Search(String w1, String w2, String rel) {
        Set<String> matchedSetenceIds = new HashSet<>();
        List<IndexedWord> indexedWordW1s= getAllNodesByWordPattern(w1, semanticGraph.vertexSet());
        List<IndexedWord> indexedWordW2s= getAllNodesByWordPattern(w2, semanticGraph.vertexSet());
        for (IndexedWord indexedWord1 : indexedWordW1s) {
            for (IndexedWord indexedWord2 : indexedWordW2s ) {
                GrammaticalRelation grammaticalRelation = semanticGraph.reln(indexedWord1, indexedWord2);
                if (grammaticalRelation != null && grammaticalRelation.getShortName().equals(rel)){
                    matchedSetenceIds.add(sentenceID);
                }
            }
        }
        return matchedSetenceIds;
    }

    public Set<String> W2realtionAnyWordSearch(String w1, String rel) {
        Set<String> matchedSetenceIds = new HashSet<>();
        List<IndexedWord> indexedWords= getAllNodesByWordPattern(w1, semanticGraph.vertexSet());
        for (IndexedWord indexedWord1 : indexedWords) {
            Set<GrammaticalRelation> relns = semanticGraph.relns(indexedWord1);
            if (isRelnPresent(rel, relns)){
                matchedSetenceIds.add(sentenceID);
            }
        }
        return matchedSetenceIds;
    }

    public Set<String> W1andW3childW2Search(String w1, String w2, String w3) {
        Set<String> w1andW2 = W2childW1Search(w2, w1);
        Set<String> w3andW2 = W2childW1Search(w2, w3);

        // Intersection of both the values.
        //if (w1andW2 != null) {
        w1andW2.retainAll(w3andW2);
        //}


        return w1andW2;
    }

    // to util method

    private boolean isWordDescendants(String word, Set<IndexedWord> descendants) {
        for (IndexedWord vertex : descendants) {
            String w = vertex.word().toLowerCase();
            if ((w == null || w != null && w.equals(word.toLowerCase()))) {
                return true;
            }
        }
        return false;
    }

    private boolean isRelnPresent(String rel, Set<GrammaticalRelation> relns) {
        for (GrammaticalRelation grammerRel : relns) {
            String reln = grammerRel.getShortName();
            if ((reln == null || reln != null && reln.equals(rel))) {
                return true;
            }
        }
        return false;
    }

    private List<IndexedWord> getAllNodesByWordPattern(String word,  Set<IndexedWord> vertexes ) {
        List<IndexedWord> nodes = new ArrayList<>();
        for (IndexedWord vertex : vertexes) {
            String w = vertex.word().toLowerCase();
            if ((w == null  || w != null && w.equals(word.toLowerCase()))) {
                nodes.add(vertex);
            }
        }
        return nodes;
    }

}
