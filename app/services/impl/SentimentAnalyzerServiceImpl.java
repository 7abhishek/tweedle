/**
 * 
 */
package services.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import akka.event.slf4j.Logger;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import services.SentimentAnalyzerService;

public class SentimentAnalyzerServiceImpl implements SentimentAnalyzerService , Serializable {
    
    private static final long serialVersionUID = 1L;
    org.slf4j.Logger logger  = LoggerFactory.getLogger(SentimentAnalyzerServiceImpl.class);
    /* (non-Javadoc)
     * @see services.SentimentAnalyzerService#analyze(java.lang.String)
     */
    @Override
    public Long analyze(String tweet) { 
        try {
            logger.info("analyze is called......");
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            Annotation document = new Annotation(tweet);
            pipeline.annotate(document);
            Long mainSentiment = 0L;
            int longest = 0;
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);
            for(CoreMap sentence: sentences) {
                logger.info("sentence : {} ", sentence.toShorterString());
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                  // this is the text of the token
                    
                  String word = token.get(TextAnnotation.class);
                  // this is the POS tag of the token
                  String pos = token.get(PartOfSpeechAnnotation.class);
                  // this is the NER label of the token
                  String ne = token.get(NamedEntityTagAnnotation.class);
                  
                  String sentiment = token.get(SentimentCoreAnnotations.SentimentClass.class);
                  logger.info("Sentiment of the Tweet : {} ", sentiment);
                  Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                  Integer sentimentIntValue = RNNCoreAnnotations.getPredictedClass(tree);
                  String partText = sentence.toString();
                  if (partText.length() > longest) {
                      mainSentiment = sentimentIntValue.longValue();
                      longest = partText.length();
                  } 
                }

                // this is the parse tree of the current sentence
                Tree tree = sentence.get(TreeAnnotation.class);                
                int mainTreeSentiment = RNNCoreAnnotations.getPredictedClass(tree);
                logger.info("mainTreeSentiment : {}", mainTreeSentiment);
                // this is the Stanford dependency graph of the current sentence
                SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
              }
            logger.info("mainSentiment : {} ", mainSentiment);
            return mainSentiment;
        } catch (Exception e) {
            logger.error("Exception Occurred  while Analyzing : {} ", e.getMessage(), e);
            e.printStackTrace();
            return 0L;
        }
    }
}
