package services;

import Exceptions.InputDataErrException;
import javassist.tools.rmi.ObjectNotFoundException;
import model.SeachModel;

import java.util.Set;

public class SearchService {

    public SeachModel searchModel;

    private static SearchService ourInstance = new SearchService();

    public static SearchService getInstance() {
        return ourInstance;
    }

    private  SearchService() {
        searchModel = new SeachModel();
    }

    public Set<String> W2childW1Search(String w1, String w2) throws ObjectNotFoundException, InputDataErrException {
        Set<String> matchedSetenceIds = searchModel.W2childW1Search(w1, w2);
        return searchModel.matchedSentences(matchedSetenceIds);
    }

    public Set<String> W2relationW1Search(String w1, String w2, String rel) throws ObjectNotFoundException, InputDataErrException {
        Set<String> matchedSetenceIds = searchModel.W2relationW1Search(w1, w2, rel);
        return searchModel.matchedSentences(matchedSetenceIds);
    }

    public Set<String> W1relationAnyParent(String w1, String rel) throws ObjectNotFoundException, InputDataErrException {
        Set<String> matchedSetenceIds = searchModel.W1relationAnyParent(w1, rel);
        return searchModel.matchedSentences(matchedSetenceIds);
    }

    public Set<String> W1andW3childrenW2Search(String w1, String w2, String w3) throws InputDataErrException, ObjectNotFoundException {
        Set<String> matchedSetenceIds = searchModel.W1andW3childrenW2Search(w1, w2, w3);
        return searchModel.matchedSentences(matchedSetenceIds);
    }

}
