package vn.edu.hcmut.emrre.core.feature.similarity;

import java.io.Serializable;

import vn.edu.hcmut.emrre.core.entity.Relation;

@SuppressWarnings("serial")
public class SimilaritySequence implements Serializable {
    private String[] lemmaSequences;
    private String[] posTagSequences;
    private String[] shortestPaths;
    private String[] phraseChunks;
    private Relation.Type relType;

    public String[] getLemmaSequences() {
        return lemmaSequences;
    }

    public void setLemmaSequences(String[] lemmaSequences) {
        this.lemmaSequences = lemmaSequences;
    }

    public String[] getPosTagSequences() {
        return posTagSequences;
    }

    public void setPosTagSequences(String[] posTagSequences) {
        this.posTagSequences = posTagSequences;
    }

    public String[] getShortestPaths() {
        return shortestPaths;
    }

    public void setShortestPaths(String[] shortestPaths) {
        this.shortestPaths = shortestPaths;
    }

    public String[] getPhraseChunks() {
        return phraseChunks;
    }

    public void setPhraseChunks(String[] phraseChunks) {
        this.phraseChunks = phraseChunks;
    }

    public Relation.Type getRelType() {
        return relType;
    }

    public void setRelType(Relation.Type relType) {
        this.relType = relType;
    }

}