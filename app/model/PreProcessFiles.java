package model;

import Exceptions.InputDataErrException;
import dbo.SentenceDao;
import dbo.WordRelationDao;
import edu.stanford.nlp.semgraph.SemanticGraph;
import utils.PreProcessedDataKeyUtils;
import utils.WordRelationUtils;
import vo.SentenceVo;
import vo.WordRelationMapVo;
import vo.WordRelationNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Scanner;

public class PreProcessFiles {

    String filePath;

    public PreProcessFiles(String filePath) {
        this.filePath = filePath;
    }


    public void readFileAndExtractSentences() {
        File file = new File(filePath);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Given file path is incorrect " +  e);
        }

        while (sc.hasNextLine()) {
            // TODO : process stuff here.
            System.out.println(sc.nextLine());
            System.out.println("----");
        }
    }


    public String preProcessText(String sentence) throws NoSuchAlgorithmException, IOException, InputDataErrException {

        // get unique sentence hash
        PreProcessedDataKeyUtils preProcessedDataKeyUtils =  new PreProcessedDataKeyUtils() ;

        String hash = preProcessedDataKeyUtils.sentenceToUnquieHashId(sentence);

        // Add sentence hash document.
        SentenceDao.insertSentenceVo(new SentenceVo(sentence));

        updateWordRelations(sentence);

        return sentence;


    }

    private void updateWordRelations(String sentence) throws NoSuchAlgorithmException, IOException, InputDataErrException {
        DepedencyParserCoreNLP depedencyParserCoreNLP = new DepedencyParserCoreNLP();;
        // Add WordRelations to Couchbase

        SemanticGraph sg = depedencyParserCoreNLP.getDependencyParseTreeUsingStandfordNlp(sentence);

        WordRelationUtils wordRelationUtils = WordRelationUtils.getInstance();

        Map<String, WordRelationNode> wordRelationNodeMap = wordRelationUtils.wordRelationsForSentenceMap(sg);

        WordRelationDao.insertWordRelationMapVo(sentence, new WordRelationMapVo(wordRelationNodeMap));
    }

}


