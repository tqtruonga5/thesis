package vn.edu.hcmut.emrre.core.entity.word;

import java.util.List;

public interface WordDAO {
    public void save(Word word);

    public void update(Word word);

    public Word findById(Long id);

    public List<Word> findBy(String recordName, Long lineIndex);

    public void delete(Word word);

    public void delete(Long id);

    public List<Word> findAll();
}
