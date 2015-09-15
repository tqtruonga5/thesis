package vn.edu.hcmut.emrre.core.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import vn.edu.hcmut.emrre.core.entity.Word;

public class WordDAOImpl implements WordDAO {
    private static WordDAO instance;

    private Map<Long, Word> data;
    private AtomicLong idGenerator;

    public WordDAOImpl() {
        data = new ConcurrentHashMap<Long, Word>();
        idGenerator = new AtomicLong(1);
    }

    public void save(Word entity) {
        if (entity.getId() == null) {
            entity.setId(idGenerator.getAndIncrement());
        }
        data.put(entity.getId(), entity);
    }

    public Word getById(Long id) {
        return data.get(id);
    }

    public List<Word> get3pre() {
        return null;
    }

    public List<Word> get3pos() {
        return null;
    }
}
