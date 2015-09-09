package vn.edu.hcmut.emrre.core.entity;

public class DocLine {
    private String fileName;
    private String content;
    private int lineIndex;

    public DocLine(String fileName, String content, int lineIndex) {
        this.fileName = fileName;
        this.content = content;
        this.lineIndex = lineIndex;
    }

    @Override
    public String toString() {
        return String.format("in file:%s || content:%s || lineIndex: %d", fileName, content, lineIndex);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

}
