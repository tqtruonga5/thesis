package vn.edu.hcmut.emrre.core.feature.similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.record.App;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.feature.FeatureExtractor;
import vn.edu.hcmut.emrre.core.io.DataReader;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class SimilarityFeaturesExtractor implements FeatureExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(SimilarityFeaturesExtractor.class);
    private static final String TEST = "t.e.s.t";
    private static final String PROBLEM = "p.r.o.b.l.e.m";
    private static final String TREATMENT = "t.r.e.a.t.m.e.n.t";
    SentenceDAO sentenceDAO = new SentenceDAOImpl();
    List<Concept> concepts;

    public SimilarityFeaturesExtractor(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public int getDimension() {
        return 0;
    }

    public double[] buildFeatures(Relation relation) {
        return null;
    }

    private void trimWords(List<Word> words, Concept concept) {
        for (Word word : words) {
            if (word.getIndex() == concept.getBegin()) {
                word.setLemma("[" + concept.getType().name() + "]");
                word.setPosTag("[" + concept.getType().name() + "]");
                word.setContent(concept.getType().name());
            }
            if (word.getIndex() > concept.getBegin() && word.getIndex() <= concept.getEnd()) {
                word.setLemma(null);
                word.setPosTag(null);
                word.setContent(null);
            }
        }
    }

    public String[] posSequence(Relation relation) {
        List<String> result = new ArrayList<String>();
        Sentence curSentence = getSenContainRelation(relation);
        Concept pre = relation.getPreConcept();
        Concept pos = relation.getPosConcept();
        List<Word> words = curSentence.getWords();

        trimWords(words, pre);
        trimWords(words, pos);

        for (Word word : words) {
            if (word.getPosTag() != null) {
                result.add(word.getPosTag());
            }
        }
        return result.stream().toArray(String[]::new);
    }

    public String[] lemmaSequence(Relation relation) {
        List<String> result = new ArrayList<String>();
        Sentence curSentence = getSenContainRelation(relation);
        List<Word> words = curSentence.getWords();
        Concept pre = relation.getPreConcept();
        Concept pos = relation.getPosConcept();

        int begin = pre.getBegin();
        int end = pos.getEnd();
        begin = (begin - 2 > 0) ? begin - 2 : 0;
        end = (end + 2 < words.size()) ? end + 2 : words.size();
        trimWords(words, pre);
        trimWords(words, pos);

        for (int i = begin; i < end; i++) {
            if (words.get(i).getLemma() != null) {
                result.add(words.get(i).getLemma());
            }
        }

        return result.stream().toArray(String[]::new);
    }

    public String[] shortestPath(Relation relation) {
        // List<String> result = new ArrayList<String>();
        Sentence curSentence = getSenContainRelation(relation);
        List<Word> words = curSentence.getWords();
        Concept pre = relation.getPreConcept();
        Concept pos = relation.getPosConcept();

        trimWords(words, pre);
        trimWords(words, pos);

        String input = words.stream().filter(w -> w.getContent() != null).map(t -> t.getContent())
                .collect(Collectors.joining(" "));

        IndexedWord w1 = null;
        IndexedWord w2 = null;

        NLPHelper nlpHelper = NLPHelper.getInstance();
        nlpHelper.setText(input);
        Annotation document = nlpHelper.getAnnotatedDoc();
        CoreMap sen = null;
        for (CoreMap each : document.get(SentencesAnnotation.class)) {
            if (each.toString().contains(pre.getType().name())) {
                sen = each;
                break;
            }
        }

        for (CoreLabel token : sen.get(TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            if (w1 == null && word.equals(pre.getType().name())) {
                w1 = new IndexedWord(token);
                continue;
            }
            if (w1 != null && word.equals(pos.getType().name())) {
                w2 = new IndexedWord(token);
                break;
            }
        }

        // this is the Stanford dependency graph of the current sentence
        SemanticGraph dependencies = sen.get(CollapsedCCProcessedDependenciesAnnotation.class);
        List<IndexedWord> result = dependencies.getShortestUndirectedPathNodes(w1, w2);

        return result.stream().map(p -> p.toString()).toArray(String[]::new);
    }

    public String[] phraseChunkSequence(Relation relation) {
        Sentence curSentence = getSenContainRelation(relation);
        NLPHelper nlpHelper = NLPHelper.getInstance();
        nlpHelper.setText(curSentence.getContent());
        Annotation document = nlpHelper.getAnnotatedDoc();

        CoreMap sen = document.get(SentencesAnnotation.class).get(0);

        // traversing the words in the current sentence
        // a CoreLabel is a CoreMap with additional token-specific methods
        // for (CoreLabel token : sen.get(TokensAnnotation.class)) {
        // // this is the text of the token
        // String word = token.get(TextAnnotation.class);
        // String pos = token.get(PartOfSpeechAnnotation.class);
        // // this is the NER label of the token
        // String ner = token.get(NamedEntityTagAnnotation.class);
        // }

        // this is the parse tree of the current sentence
        Tree tree = sen.get(TreeAnnotation.class);
        System.err.println(tree.getChildrenAsList());

        for (Tree tree2 : tree.getChild(0).getChildrenAsList()) {
            System.out.println(tree2.label());
        }
        // this is the POS tag of the token

        return null;
    }

    private Sentence getSenContainRelation(Relation relation) {
        String recordName = relation.getFileName();
        int lineIndex = relation.getPosConcept().getLine();
        Sentence curSentence = sentenceDAO.findByRecordAndLineIndex(recordName, lineIndex);
        return curSentence;
    }

    public static void main(String[] args) {

        String inputConceptFile = "i2b2data/train/beth/concept/record-14.con";
        String inputRelationFile = "i2b2data/train/beth/rel/record-14.rel";
        DataReader dataReader = new DataReader();
        List<Concept> concepts = dataReader.readConcepts(inputConceptFile, 0);
        List<Relation> relations = dataReader.readRelations(concepts, inputRelationFile);

        SimilarityFeaturesExtractor sf = new SimilarityFeaturesExtractor(concepts);

        for (Relation relation : relations) {
            String[] posTags = sf.posSequence(relation);
            for (String string : posTags) {
                System.out.print(string + " ");
            }
            System.out.println();
            // posTags = sf.lemmaSequence(relation);
            // for (String string : posTags) {
            // System.err.print(string + " ");
            // }
            System.out.println("=====\n");
        }
    }

}
