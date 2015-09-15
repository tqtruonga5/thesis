package vn.edu.hcmut.emrre.core.dao;

import java.util.List;

import vn.edu.hcmut.emrre.core.entity.Word;

public interface WordDAO {
    void save(Word entity);
    Word getById(Long id);
    List<Word> get3pre();
    List<Word> get3pos();
}
