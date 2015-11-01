package vn.edu.hcmut.emrre.core.entity.sentence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import vn.edu.hcmut.emrre.core.entity.record.Record;
import vn.edu.hcmut.emrre.core.entity.word.Word;

@Entity
@Table(name = "sentence")
public class Sentence {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "position")
    private Long index;

    @Column(name = "content")
    @Type(type = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    private Record record;

    @OneToMany(mappedBy = "sentence")
    private List<Word> words = new ArrayList<Word>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {

        return String.format("record: %s|index:%d | content: %s", record.getName(), index, content);
    }
}
