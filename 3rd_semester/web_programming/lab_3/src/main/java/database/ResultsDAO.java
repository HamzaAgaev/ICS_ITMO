package database;

import beans.Result;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import java.util.ArrayList;
import org.hibernate.query.Query;

public class ResultsDAO {
    private ArrayList<Result> resultArrayList = new ArrayList<>();
    public ArrayList<Result> getResultArrayList() {
        Session session = null;
        try {
            String HQLQuery = "FROM Result";
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(HQLQuery);
            resultArrayList = new ArrayList<>(query.list());
        } catch (HibernateException hibernateException) {
            System.err.println("getResultArrayList: Something went wrong with Hibernate: " + hibernateException);
        }
        finally {
            if (session != null) {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
        return resultArrayList;
    }

    public void addNewResult(Result result) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.persist(result);
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            System.err.println("addNewResult: Something went wrong with Hibernate : " + hibernateException);
        }
        finally {
            if (session != null) {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
    }

    public void clear() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String HQLQuery = "DELETE FROM Result";
            session.createQuery(HQLQuery).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException hibernateException) {
            System.err.println("clear: Something went wrong with Hibernate : " + hibernateException);
        }
        finally {
            if (session != null) {
                if (session.isOpen()) {
                    session.close();
                }
            }
        }
    }
}
