package vn.edu.hcmut.emrre.core.entity.word;

import java.util.List;

import org.hibernate.Session;

import vn.edu.hcmut.emrre.core.entity.HibernateUtil;

public class WordDAOImpl implements WordDAO {

    public void save(Word word) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(word);
        session.getTransaction().commit();
    }

    public void update(Word word) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.update(word);
        session.getTransaction().commit();
    }

    public Word findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Word word = (Word) session.get(Word.class, id);
        return word;
    }

    public List<Word> findBy(String recordName, Long lineIndex) {
        return null;
    }

    public void delete(Word word) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(word);
        session.getTransaction().commit();
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
