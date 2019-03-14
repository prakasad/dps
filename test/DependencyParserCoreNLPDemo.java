import Exceptions.InputDataErrException;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.logging.Redwood;
import helper.Couchbase;
import javassist.tools.rmi.ObjectNotFoundException;
import model.PreProcessFiles;
import model.SeachModel;
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

        PreProcessFiles preProcessFiles = new PreProcessFiles("testSentences.txt");
        Couchbase couchbase = new Couchbase();
        try {

            preProcessFiles.readFileAndExtractSentences();
            //preProcessFiles.preProcessText(text);
        } catch (NoSuchAlgorithmException | InputDataErrException e) {
            Logger.error(String.format("%s", e.getStackTrace()));
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("W2realtionW1Search");
            w2childW1SearchTest();
            System.out.println("W2realtionW1Search");
            w2relationW1Search();
            System.out.println("W2realtionAnyWordSearch");
            w2relationAnyWordSearch();
            System.out.println("W1andW3childW2Search");
             w1andW3childW2Search();
        } catch (InputDataErrException e) {
            e.printStackTrace();
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void w2childW1SearchTest() throws InputDataErrException, ObjectNotFoundException {
        SeachModel searchModel = new SeachModel();

        System.out.println(searchModel.matchedSentences(searchModel.W2childW1Search("submitted", "immigration")));

        System.out.println(searchModel.matchedSentences(searchModel.W2childW1Search("were", "submitted")));


    }

    public static void w2relationW1Search() throws InputDataErrException, ObjectNotFoundException {
        SeachModel searchModel = new SeachModel();

        // True
        System.out.println(searchModel.matchedSentences(searchModel.W2relationW1Search("submitted", "Bills", "nsubjpass")));

        System.out.println(searchModel.matchedSentences(searchModel.W2relationW1Search("submitted", "were", "auxpass")));

        // False
        System.out.println(searchModel.matchedSentences(searchModel.W2relationW1Search("submitted", "Kansas", "nmod")));

        System.out.println(searchModel.matchedSentences(searchModel.W2relationW1Search("submitted", "Republican", "nsubjpass")));

    }

    public static void w2relationAnyWordSearch() throws InputDataErrException, ObjectNotFoundException {
        SeachModel searchModel = new SeachModel();


        // True
        System.out.println(searchModel.matchedSentences(searchModel.W1relationAnyParent("Republican",  "nmod")));

        System.out.println(searchModel.matchedSentences(searchModel.W1relationAnyParent("immigration", "conj")));

        // False
        System.out.println(searchModel.matchedSentences(searchModel.W1relationAnyParent("Republican123",  "nmod")));

        System.out.println(searchModel.matchedSentences(searchModel.W1relationAnyParent("immigration", "nmod")));
    }


    public static void w1andW3childW2Search() throws InputDataErrException, ObjectNotFoundException {
        SeachModel searchModel = new SeachModel();

        // True
        System.out.println(searchModel.matchedSentences(searchModel.W1andW3childrenW2Search("bills",  "submitted", "brownback")));

        System.out.println(searchModel.matchedSentences(searchModel.W1andW3childrenW2Search("Kansas", "Republican", "Senator")));

        // False
        System.out.println(searchModel.matchedSentences(searchModel.W1andW3childrenW2Search("Kansas",  "Bills", "immigration")));

        System.out.println(searchModel.matchedSentences(searchModel.W1andW3childrenW2Search("bills123",  "submitted", "brownback")));
    }

}