package vn.edu.hcmut.emrre.core.entity.word;

import java.util.List;

import org.hibernate.Session;

import vn.edu.hcmut.emrre.core.entity.utils.HibernateUtil;

public class WordDAOImpl implements WordDAO {
    private Session session;

    public WordDAOImpl() {
        session = HibernateUtil.getSession();
    }

    public void closeSession() {
        session.close();
    }

    public void save(Word word) {
        session.beginTransaction();
        session.save(word);
        session.getTransaction().commit();
    }

    public void update(Word word) {
        session.beginTransaction();
        session.update(word);
        session.getTransaction().commit();
    }

    public Word findById(Long id) {
        Word word = (Word) session.get(Word.class, id);
        return word;
    }

    public void delete(Word word) {
        session.beginTransaction();
        session.delete(word);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public List<Word> findByIdCondition(Long id) {
        List<Word> words = (List<Word>) session.createQuery("from Word w where w.id >= :id").setLong("id", id).list();
        return words;
    }

    public void delete(Long id) {
        Word word = findById(id);
        delete(word);
    }

    @SuppressWarnings("unchecked")
    public List<Word> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Word> words = session.createQuery("from Word").list();
        return words;
    }

}
