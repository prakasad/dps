package model;

import Exceptions.InputDataErrException;
import dbo.SentenceDao;
import dbo.WordRelationDao;
import edu.stanford.nlp.semgraph.SemanticGraph;
import javassist.tools.rmi.ObjectNotFoundException;
import play.Logger;
import utils.PreProcessedDataKeyUtils;
import utils.StopWordsAndPuncation;
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

    public PreProcessFiles() {
    }

    public PreProcessFiles(String filePath) {
        this.filePath = filePath;
    }


    public void readFileAndExtractSentences() throws ObjectNotFoundException, NoSuchAlgorithmException, InputDataErrException, IOException {
        File file = new File(filePath);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Given file path is incorrect " +  e);
        }

        Logger.info("Starting the text pre-processing.");
        while (sc.hasNextLine()) {
            // Note : production code will be firing an event to kafka.
            // another consumer will read the preProsseText method.
            // Rudimentary way to preprocesing the text.
            String line = sc.nextLine();
            preProcessText(line);

            // Keeping these logs as the application as of now runs from a server.
            System.out.println("pre-processing text : " + line);
            Logger.info("pre-processing text : " + line);
            System.out.println("----------------------------------");
            Logger.info("----------------------------------");
        }
    }


    public String preProcessText(String sentence) throws NoSuchAlgorithmException, IOException, InputDataErrException, ObjectNotFoundException {

        // get unique sentence hash
        PreProcessedDataKeyUtils preProcessedDataKeyUtils =  new PreProcessedDataKeyUtils() ;

        String hash = preProcessedDataKeyUtils.sentenceToUnquieHashId(sentence);

        DepedencyParserCoreNLP depedencyParserCoreNLP = new DepedencyParserCoreNLP();;

        // Add WordRelations to Couchbase
        SemanticGraph sg = depedencyParserCoreNLP.getDependencyParseTreeUsingStandfordNlp(sentence);

        // Add sentence hash document.
        SentenceDao.insertSentenceVo(new SentenceVo(sentence));
        updateWordRelations(sentence, sg);

        // Adding reverse indexed information to Couchbase.
        StopWordsAndPuncation stopWordsAndPuncation = new StopWordsAndPuncation();
        stopWordsAndPuncation.updateReveseIndexMap(sg, hash);

        return sentence;


    }

    private void updateWordRelations(String sentence, SemanticGraph sg) throws NoSuchAlgorithmException, IOException, InputDataErrException {

        WordRelationUtils wordRelationUtils = WordRelationUtils.getInstance();

        Map<String, WordRelationNode> wordRelationNodeMap = wordRelationUtils.wordRelationsForSentenceMap(sg);

        WordRelationDao.insertWordRelationMapVo(sentence, new WordRelationMapVo(wordRelationNodeMap));
    }

}


