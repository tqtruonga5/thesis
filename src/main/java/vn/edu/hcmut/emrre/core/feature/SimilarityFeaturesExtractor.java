package vn.edu.hcmut.emrre.core.feature;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Concept;
import vn.edu.hcmut.emrre.core.entity.Relation;
import vn.edu.hcmut.emrre.core.entity.sentence.Sentence;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAO;
import vn.edu.hcmut.emrre.core.entity.sentence.SentenceDAOImpl;
import vn.edu.hcmut.emrre.core.entity.word.Word;
import vn.edu.hcmut.emrre.core.io.DataReader;

public class SimilarityFeaturesExtractor implements FeatureExtractor {
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
                // word.setContent("[" + concept.getType().name() + "]");
            }
            if (word.getIndex() > concept.getBegin() && word.getIndex() <= concept.getEnd()) {
                word.setLemma(null);
                word.setPosTag(null);
            }
        }
    }

    public String[] posSequence(Relation relation) {
        List<String> result = new ArrayList<String>();
        String recordName = relation.getFileName();
        int lineIndex = relation.getPosConcept().getLine();
        Sentence curSentence = sentenceDAO.findByRecordAndLineIndex(recordName, lineIndex);
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
        System.out.println(curSentence.getContent());
        System.out.println(relation.getPreConcept().getContent());
        System.out.println(relation.getPosConcept().getContent());
        return result.stream().toArray(String[]::new);
    }

    public String[] lemmaSequence(Relation relation) {
        List<String> result = new ArrayList<String>();
        String recordName = relation.getFileName();
        int lineIndex = relation.getPosConcept().getLine();
        Sentence curSentence = sentenceDAO.findByRecordAndLineIndex(recordName, lineIndex);

        List<Word> words = curSentence.getWords();
        Concept pre = relation.getPreConcept();
        Concept pos = relation.getPosConcept();

        int begin = (pre.getBegin() < pos.getBegin()) ? pre.getBegin() : pos.getBegin();
        int end = (pre.getEnd() > pos.getEnd()) ? pre.getEnd() : pos.getEnd();
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

    private static class LevenshteinDistance {
        private static int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }

        public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
            int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

            for (int i = 0; i <= lhs.length(); i++)
                distance[i][0] = i;
            for (int j = 1; j <= rhs.length(); j++)
                distance[0][j] = j;

            for (int i = 1; i <= lhs.length(); i++)
                for (int j = 1; j <= rhs.length(); j++)
                    distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1]
                            + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

            return distance[lhs.length()][rhs.length()];
        }

        public static int computeLevenshteinDistance(String[] lhs, String[] rhs) {
            int[][] distance = new int[lhs.length + 1][rhs.length + 1];

            for (int i = 0; i <= lhs.length; i++)
                distance[i][0] = i;
            for (int j = 1; j <= rhs.length; j++)
                distance[0][j] = j;

            for (int i = 1; i <= lhs.length; i++)
                for (int j = 1; j <= rhs.length; j++)
                    distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1]
                            + ((lhs[i - 1].equals(rhs[j - 1])) ? 0 : 1));

            return distance[lhs.length][rhs.length];
        }
    }

    public static void main(String[] args) {
        String s1 = "Hello, my name's truong";
        // String s2 = "Hello, i am truong";
        // String s3 = "Hello, my name's lap";
        //
        // String s11[] = { "to", "include", "[test]", ",", "which", "on",
        // "{DATE}", "show", "positive", "[problem]",
        // "and", "bacteria" };
        // String s22[] = { "he", "have", "[test]", "which", "show",
        // "[problem]", "and", "unchanged" };
        //
        // System.err.println(LevenshteinDistance.computeLevenshteinDistance(s11,
        // s22));

        String inputConceptFile = "i2b2data/train/beth/concept/record-13.con";
        String inputRelationFile = "i2b2data/train/beth/rel/record-13.rel";
        DataReader dataReader = new DataReader();
        List<Concept> concepts = dataReader.readConcepts(inputConceptFile, 0);
        List<Relation> relations = dataReader.readRelations(concepts, inputRelationFile);

        SimilarityFeaturesExtractor sf = new SimilarityFeaturesExtractor(concepts);

        for (Relation relation : relations) {
            String[] posTags = sf.posSequence(relation);
            for (String string : posTags) {
                System.err.print(string + " ");
            }
            System.out.println();
            posTags = sf.lemmaSequence(relation);
            for (String string : posTags) {
                System.err.print(string + " ");
            }
            System.out.println("=====\n");
        }

        // for (String string : posTags) {
        // System.err.print(string + " ");
        // }

    }
}
