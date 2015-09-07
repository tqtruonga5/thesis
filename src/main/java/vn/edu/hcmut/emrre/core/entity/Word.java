package vn.edu.hcmut.emrre.core.entity;

public class Word {
    // private long id;
     private int index;
    private DocLine sentence;
    private String content;
    // private IBOTag iboTag;
    private String posTag;
//    private String orthTag;
//    private String sessionTag;
//    private int umlsTag;

    public DocLine getSentence() {
        return sentence;
    }

    public void setSentence(DocLine sentence) {
        this.sentence = sentence;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

//    public String getOrthTag() {
//        return orthTag;
//    }
//
//    public void setOrthTag(String orthTag) {
//        this.orthTag = orthTag;
//    }
//
//    public String getSessionTag() {
//        return sessionTag;
//    }
//
//    public void setSessionTag(String sessionTag) {
//        this.sessionTag = sessionTag;
//    }
//
//    public int getUmlsTag() {
//        return umlsTag;
//    }
//
//    public void setUmlsTag(int umlsTag) {
//        this.umlsTag = umlsTag;
//    }
}
