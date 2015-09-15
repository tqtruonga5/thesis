package vn.edu.hcmut.emrre.core.entity;

public class Word {

    private Long id;
    private int index;
    private DocLine docLine;
    private String content;
    private String posTag;

    // private IBOTag iboTag;
    // private String orthTag;
    // private String sessionTag;
    // private int umlsTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DocLine getSentence() {
        return docLine;
    }

    public void setSentence(DocLine docLine) {
        this.docLine = docLine;
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
}
