package vn.edu.hcmut.emrre.core.entity.record;

import java.util.List;

import org.hibernate.Session;

import vn.edu.hcmut.emrre.core.entity.utils.HibernateUtil;

public class RecordDAOImpl implements RecordDAO {
    private Session session;
    public RecordDAOImpl() {
        session = HibernateUtil.getSession();
    }

    public void save(Record record) {
        session.beginTransaction();
        session.save(record);
        session.getTransaction().commit();
    }

    public void update(Record record) {
        session.beginTransaction();
        session.update(record);
        session.getTransaction().commit();
    }

    public Record findById(Long id) {
        Record record = (Record) session.get(Record.class, id);
        return record;
    }

    public Record findByName(String name) {
        String hql = "from Record r where r.name = :name";
        Record record = (Record) session.createQuery(hql).setString("name", name).uniqueResult();
        return record;
    }

    public void delete(Record record) {
        session.beginTransaction();
        session.delete(record);
        session.getTransaction().commit();
    }

    public void delete(Long id) {
        Record record = (Record) session.get(Record.class, id);
        delete(record);
    }

    @SuppressWarnings("unchecked")
    public List<Record> findAll() {
        List<Record> records = (List<Record>) session.createQuery("from Record").list();
        return records;
    }

}
