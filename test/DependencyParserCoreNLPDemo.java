import Exceptions.InputDataErrException;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.logging.Redwood;
import helper.Couchbase;
import model.PreProcessFiles;
import model.SentanceSearchModel;
import org.junit.Test;
import play.Logger;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;


/**
 * Demonstrates how to use the NN dependency
 * parser via a CoreNLP pipeline.
 *
 * @author Christopher Manning
 */
public class DependencyParserCoreNLPDemo {

    /** A logger for this class */
    private static final Redwood.RedwoodChannels log = Redwood.channels(DependencyParserCoreNLPDemo.class);

    private DependencyParserCoreNLPDemo() {} // static main method only

    @Test
    public void name() {
    }

    public static void main(String[] args) throws IOException {
        String text;
        if (args.length > 0) {
            text = IOUtils.slurpFileNoExceptions(args[0], "utf-8");
        } else {
            text = "Bills on ports and immigration were submitted by Senator Brownback Republican of Kansas";
        }
        Annotation ann = new Annotation(text);

        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,depparse",
                "depparse.model", DependencyParser.DEFAULT_MODEL
        );

        AnnotationPipeline pipeline = new StanfordCoreNLP(props);

        pipeline.annotate(ann);

        PreProcessFiles preProcessFiles = new PreProcessFiles();
        Couchbase couchbase = new Couchbase();
        try {
            preProcessFiles.preProcessText(text);
        } catch (NoSuchAlgorithmException | InputDataErrException e) {
            Logger.error(String.format("%s", e.getStackTrace()));
        }

//        for (CoreMap sent : ann.get(CoreAnnotations.SentencesAnnotation.class)) {
//            SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
//            //log.info(IOUtils.eolChar + sg.toString(SemanticGraph.OutputFormat.LIST));
//           // System.out.println(IOUtils.eolChar + sg.toString(SemanticGraph.OutputFormat.LIST));
//            System.out.println(sg.toString(SemanticGraph.OutputFormat.LIST));
//            System.out.println(sg.toString(SemanticGraph.OutputFormat.RECURSIVE));
//            System.out.println(sg.toString(SemanticGraph.OutputFormat.XML));
//            System.out.println(sg.toString(CoreLabel.OutputFormat.ALL));
//            // TODO : uncomment later
//            W2childW1SearchTest(sg);
//            System.out.println("W2realtionW1Search");
//            W2realtionW1Search(sg);
//            System.out.println("W2realtionAnyWordSearch");
//            W2realtionAnyWordSearch(sg);
//            System.out.println("W1andW3childW2Search");
//            // TODO convert all the words to lower case.
//            W1andW3childW2Search(sg);
//            //sg.getNodeByWordPattern("submitted");
//
//            // sg.getParentsWithReln(sg.getNodeByWordPattern("Republican")
//
//            //sg.getNodeByWordPattern("Republican")
//            //PreProcessFiles preProcessFiles = new PreProcessFiles("/Users/Aditya/Desktop/SampleProjects/Play/dps/sentences.txt");
//            //preProcessFiles.readFileAndExtractSentences();
//
//            //Module module = new Module();
//            //module.configure();
//            Couchbase couchbase = new Couchbase();
//            Couchbase.update("Test", "{\"type\":1001, \"name\":\"Test1\"}");
//
//
//            WordRelationUtils wordRelationUtils = WordRelationUtils.getInstance();
//            System.out.println(wordRelationUtils.wordRelationsForSentenceMap(sg));
//
//
//        }


    }

    public static void W2childW1SearchTest(SemanticGraph sg) {
        SentanceSearchModel sentanceSearchModel = new SentanceSearchModel(sg);

        System.out.println(sentanceSearchModel.W2childW1Search("submitted", "immigration"));

        System.out.println(sentanceSearchModel.W2childW1Search("were", "submitted"));


    }

    public static void W2realtionW1Search(SemanticGraph sg) {
        SentanceSearchModel sentanceSearchModel = new SentanceSearchModel(sg);

        // True
        System.out.println(sentanceSearchModel.W2realtionW1Search("submitted", "Bills", "nsubjpass"));

        System.out.println(sentanceSearchModel.W2realtionW1Search("submitted", "were", "auxpass"));

        // False
        System.out.println(sentanceSearchModel.W2realtionW1Search("submitted", "Kansas", "nmod"));

        System.out.println(sentanceSearchModel.W2realtionW1Search("submitted", "Republican", "nsubjpass"));

    }

    public static void W2realtionAnyWordSearch(SemanticGraph sg) {
        SentanceSearchModel sentanceSearchModel = new SentanceSearchModel(sg);

        // True
        System.out.println(sentanceSearchModel.W2realtionAnyWordSearch("Republican",  "nmod"));

        System.out.println(sentanceSearchModel.W2realtionAnyWordSearch("immigration", "conj"));

        // False
        System.out.println(sentanceSearchModel.W2realtionAnyWordSearch("Republican123",  "nmod"));

        System.out.println(sentanceSearchModel.W2realtionAnyWordSearch("immigration", "nmod"));
    }


    public static void W1andW3childW2Search(SemanticGraph sg) {
        SentanceSearchModel sentanceSearchModel = new SentanceSearchModel(sg);

        // True
        System.out.println(sentanceSearchModel.W1andW3childW2Search("bills",  "submitted", "brownback"));

        System.out.println(sentanceSearchModel.W1andW3childW2Search("Kansas", "Republican", "Senator"));

        // False
        System.out.println(sentanceSearchModel.W1andW3childW2Search("Kansas",  "Bills", "immigration"));

        System.out.println(sentanceSearchModel.W1andW3childW2Search("bills123",  "submitted", "brownback"));
    }

}