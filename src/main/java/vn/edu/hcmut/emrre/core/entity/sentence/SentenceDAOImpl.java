package vn.edu.hcmut.emrre.core.entity.sentence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import vn.edu.hcmut.emrre.core.entity.utils.HibernateUtil;

public class SentenceDAOImpl implements SentenceDAO {
    private Session session;
    
    public SentenceDAOImpl(){
        session = HibernateUtil.getSession();
    }
    
    public void closeSession(){
        session.close();
    }

    public void save(Sentence sentence) {
        session.beginTransaction();
        session.save(sentence);
        session.getTransaction().commit();
    }

    public void update(Sentence sentence) {
        session.beginTransaction();
        session.update(sentence);
        session.getTransaction().commit();
    }

    public Sentence findById(Long id) {
        Sentence sentence = (Sentence) session.get(Sentence.class, id);
        return sentence;
    }

    public Sentence findByRecordAndLineIndex(String recordName, Integer lineIndex) {
        Query query = session
                .createQuery("from Sentence s where s.record.name = :recordName and s.index = :lineIndex");
        return (Sentence) query.setString("recordName", recordName).setLong("lineIndex", lineIndex).uniqueResult();
    }

    public void delete(Sentence sentence) {
        session.beginTransaction();
        session.delete(sentence);
        session.getTransaction().commit();
    }

    public void delete(Long id) {
        Sentence sentence = findById(id);
        delete(sentence);
    }

    @SuppressWarnings("unchecked")
    public List<Sentence> findAll() {
        List<Sentence> sentences = session.createQuery("from Sentence").list();
        return sentences;
    }

}
