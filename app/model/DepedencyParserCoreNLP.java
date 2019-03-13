package model;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.logging.Redwood;

import java.util.Properties;

public class DepedencyParserCoreNLP {

    private static final Redwood.RedwoodChannels log = Redwood.channels(DepedencyParserCoreNLP.class);

    public String text;

    //public SemanticGraph

    public SemanticGraph getDependencyParseTreeUsingStandfordNlp (String sentence) {

        // TODO: do some pre process to take out puncuations and stop words ??.
        Annotation ann = new Annotation(sentence);

        Properties props = PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,depparse",
                "depparse.model", DependencyParser.DEFAULT_MODEL
        );

        AnnotationPipeline pipeline = new StanfordCoreNLP(props);

        pipeline.annotate(ann);

        for (CoreMap sent : ann.get(CoreAnnotations.SentencesAnnotation.class)) {
            SemanticGraph sg = sent.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
            // Making a bold assumption each sentence will have only 1 parse tree.
            // TODO handle later if not the case.
            return sg;
        }

        return  null;
    }

}
