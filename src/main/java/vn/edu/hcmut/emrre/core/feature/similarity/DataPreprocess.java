package vn.edu.hcmut.emrre.core.feature.similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.io.DataReader;
import vn.edu.hcmut.emrre.training.EMRTrain2;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class DataPreprocess {
    private static final Logger LOG = LoggerFactory.getLogger(DataPreprocess.class);
    private static SentenceDAO sentenceDAO;
    static {
        sentenceDAO = new SentenceDAOImpl();
    }

    public static String[] posSequence(Relation relation) {
        List<String> result = Collections.emptyList();
        try {
            Sentence curSentence = getSenContainRelation(relation);
            List<Word> words = curSentence.getWords();
            Concept pre = relation.getPreConcept();
            Concept pos = relation.getPosConcept();
            int begin = pre.getBegin();
            int end = pos.getEnd() + 1;
            begin = (begin - 2 > 0) ? begin - 2 : 0;
            end = (end + 2 < words.size()) ? end + 2 : words.size();

            result = words.stream().map(w -> w.getPosTag()).collect(Collectors.toCollection(ArrayList::new));

            List<String> preConceptString = result.subList(pre.getBegin(), pre.getEnd() + 1);
            preConceptString.clear();
            preConceptString.add(pre.getType().name());
            for (int i = pre.getBegin(); i < pre.getEnd(); i++) {
                preConceptString.add(null);
            }

            List<String> posConceptString = result.subList(pos.getBegin(), pos.getEnd() + 1);
            posConceptString.clear();
            posConceptString.add(pos.getType().name());
            for (int i = pos.getBegin(); i < pos.getEnd(); i++) {
                posConceptString.add(null);
            }

            // result = result.subList(begin, end);

        } catch (Exception e) {
            LOG.error(relation.getFileName() + "\t" + relation.toString(), e);
        }
        return result.stream().filter(w -> w != null).toArray(String[]::new);
    }

    public static String[] lemmaSequence(Relation relation) {
        List<String> result = Collections.emptyList();
        try {
            Sentence curSentence = getSenContainRelation(relation);
            List<Word> words = curSentence.getWords();
            Concept pre = relation.getPreConcept();
            Concept pos = relation.getPosConcept();
            int begin = pre.getBegin();
            int end = pos.getEnd() + 1;
            begin = (begin - 2 > 0) ? begin - 2 : 0;
            end = (end + 2 < words.size()) ? end + 2 : words.size();

            result = words.stream().map(w -> w.getLemma()).collect(Collectors.toCollection(ArrayList::new));

            List<String> preConceptString = result.subList(pre.getBegin(), pre.getEnd() + 1);
            preConceptString.clear();
            preConceptString.add(pre.getType().name());
            for (int i = pre.getBegin(); i < pre.getEnd(); i++) {
                preConceptString.add(null);
            }

            List<String> posConceptString = result.subList(pos.getBegin(), pos.getEnd() + 1);
            posConceptString.clear();
            posConceptString.add(pos.getType().name());
            for (int i = pos.getBegin(); i < pos.getEnd(); i++) {
                posConceptString.add(null);
            }

            result = result.subList(begin, end);

        } catch (Exception e) {
            LOG.error(relation.getFileName() + "\t" + relation.toString(), e);
        }
        return result.stream().filter(w -> w != null).toArray(String[]::new);
    }

    public static String[] shortestPath(Relation relation) {
        List<IndexedWord> result = Collections.emptyList();
        try {
            Sentence curSentence = getSenContainRelation(relation);
            List<Word> words = curSentence.getWords();
            Concept pre = relation.getPreConcept();
            Concept pos = relation.getPosConcept();

            List<String> tmp = words.stream().map(w -> w.getContent()).collect(Collectors.toCollection(ArrayList::new));

            List<String> preConceptString = tmp.subList(pre.getBegin(), pre.getEnd() + 1);
            preConceptString.clear();
            preConceptString.add(pre.getType().name());
            for (int i = pre.getBegin(); i < pre.getEnd(); i++) {
                preConceptString.add(null);
            }

            List<String> posConceptString = tmp.subList(pos.getBegin(), pos.getEnd() + 1);
            posConceptString.clear();
            posConceptString.add(pos.getType().name());
            for (int i = pos.getBegin(); i < pos.getEnd(); i++) {
                posConceptString.add(null);
            }

            String input = tmp.stream().filter(w -> w != null).collect(Collectors.joining(" "));
            IndexedWord w1 = null;
            IndexedWord w2 = null;

            NLPHelper nlpHelper = NLPHelper.getInstance();
            nlpHelper.setText(input);
            Annotation document = nlpHelper.getAnnotatedDoc();
            CoreMap sen = null;
            for (CoreMap each : document.get(SentencesAnnotation.class)) {
//                System.out.println(each.toString());
                if (each.toString().contains(pre.getType().name()) && each.toString().contains(pos.getType().name())) {
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
            SemanticGraph dependencies = sen.get(BasicDependenciesAnnotation.class);
            List<IndexedWord> preResult = dependencies.getShortestUndirectedPathNodes(w1, w2);
            if (preResult != null) {
                result = preResult;
            }
            else{
                LOG.error("null shortest path");
            }
        } catch (Exception e) {
            LOG.error(relation.getFileName() + "\t" + relation.toString(), e);
        }

        return result.stream().map(p -> p.word()).toArray(String[]::new);
    }

    /*
     * public static String[] phraseChunkSequence(Relation relation) { Sentence
     * curSentence = getSenContainRelation(relation); NLPHelper nlpHelper =
     * NLPHelper.getInstance(); List<Word> words = curSentence.getWords();
     * 
     * Concept pre = relation.getPreConcept(); Concept pos =
     * relation.getPosConcept();
     * 
     * String input = words.stream() .filter(w -> (w.getIndex() > pre.getEnd()
     * && w.getIndex() < pos.getBegin())) .map(t -> t.getContent())
     * .collect(Collectors.joining(" "));
     * 
     * nlpHelper.setText(input); Annotation document =
     * nlpHelper.getAnnotatedDoc(); CoreMap sen = null; for (CoreMap each :
     * document.get(SentencesAnnotation.class)) { if
     * (each.toString().contains(pre.getType().name())) { sen = each; break; } }
     * 
     * if (sen != null) { // this is the parse tree of the current sentence Tree
     * tree = sen.get(TreeAnnotation.class);
     * System.out.println(tree.getChildrenAsList());
     * 
     * for (Tree tree2 : tree.getChild(0).getChildrenAsList()) {
     * System.out.println(tree2.label()); } } return null; }
     */

    public static String[] phraseChunkSequence(Relation relation) {
        List<String> result = Collections.emptyList();
        try {
            Sentence curSentence = getSenContainRelation(relation);
            List<Word> words = curSentence.getWords();
            Concept pre = relation.getPreConcept();
            Concept pos = relation.getPosConcept();
            int begin = pre.getEnd() + 1;
            int end = pos.getBegin();

            result = words.stream().map(w -> w.getPosTag()).collect(Collectors.toCollection(ArrayList::new));
            result = result.subList(begin, end);

        } catch (Exception e) {
            LOG.error(relation.getFileName() + "\t" + relation.toString(), e);
        }
        return result.stream().filter(w -> w != null).toArray(String[]::new);
    }

    public static String[] conceptTypeSequence(Relation relation) {
        return null;
    }

    private static Sentence getSenContainRelation(Relation relation) {
        String recordName = relation.getFileName();
        int lineIndex = relation.getPosConcept().getLine();
        Sentence curSentence = sentenceDAO.findByRecordAndLineIndex(recordName, lineIndex);
        return curSentence;
    }

    public static void main(String[] args) {
        // String inputConceptFile =
        // "i2b2data/train/beth/concept/record-13.con";
        // String inputRelationFile = "i2b2data/train/beth/rel/record-13.rel";
        // DataReader dataReader = new DataReader();
        // List<Concept> concepts = dataReader.readConcepts(inputConceptFile,
        // 0);
        // List<Relation> relations = dataReader.readRelations(concepts,
        // inputRelationFile);
        EMRTrain2 train = new EMRTrain2(0);
        train.getConceptData();
        train.getRelationData();
        List<Concept> concepts = EMRTrain2.getConcepts();
        List<Relation> relations = EMRTrain2.getRelations();
        System.out.println(relations.size());
        int count = 1;
        for (Relation relation : relations) {
            String[] tmps = DataPreprocess.shortestPath(relation);
            for (String string : tmps) {
                System.out.print(" " + string);
            }
            System.out.println();
            tmps = DataPreprocess.posSequence(relation);
            for (String string : tmps) {
                System.out.print(" " + string);
            }
//            System.out.println(" \n >>> >>>>>> >>>>>>>>");
            System.out.println(count++);
        }
    }

}
