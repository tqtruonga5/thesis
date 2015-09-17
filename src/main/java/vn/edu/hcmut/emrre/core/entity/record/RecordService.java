//package vn.edu.hcmut.emrre.core.entity.record;
//
//import java.util.List;
//
//import javax.transaction.HeuristicMixedException;
//import javax.transaction.HeuristicRollbackException;
//import javax.transaction.RollbackException;
//import javax.transaction.SystemException;
//
//public class RecordService {
//    private static RecordDAOImpl recordDAO;
//
//    public RecordService() {
//        recordDAO = new RecordDAOImpl();
//    }
//
//    public void persist(Record entity) throws SecurityException, HeuristicMixedException, HeuristicRollbackException,
//            RollbackException, SystemException {
//        recordDAO.openCurrentSessionwithTransaction();
//        recordDAO.persist(entity);
//        recordDAO.closeCurrentSessionwithTransaction();
//    }
//
//    public void update(Record entity) throws SecurityException, HeuristicMixedException, HeuristicRollbackException,
//            RollbackException, SystemException {
//        recordDAO.openCurrentSessionwithTransaction();
//        recordDAO.update(entity);
//        recordDAO.closeCurrentSessionwithTransaction();
//    }
//
//    public Record findById(Long id) {
//        recordDAO.openCurrentSession();
//        Record record = recordDAO.findById(id);
//        recordDAO.closeCurrentSession();
//        return record;
//    }
//
//    public void delete(Long id) throws SecurityException, HeuristicMixedException, HeuristicRollbackException,
//            RollbackException, SystemException {
//        recordDAO.openCurrentSessionwithTransaction();
//        Record record = recordDAO.findById(id);
//        recordDAO.delete(record);
//        recordDAO.closeCurrentSessionwithTransaction();
//    }
//
//    public List<Record> findAll() {
//        recordDAO.openCurrentSession();
//        List<Record> records = recordDAO.findAll();
//        recordDAO.closeCurrentSession();
//        return records;
//    }
//}
