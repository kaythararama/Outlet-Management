/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.icube.skilltest.outletmanagement.db;



import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author moethantoo
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    private static final ThreadLocal threadSession = new ThreadLocal();
    private static final ThreadLocal threadTransaction = new ThreadLocal();

    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            //sessionFactory = new AnnotationConfiguration().configure("hibernate.cfg_lb.xml").buildSessionFactory();
            sessionFactory = new AnnotationConfiguration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        Session s = (Session) threadSession.get();
        // Open a new Session, if this thread has none yet
        try {
            if (s == null) {
                s = sessionFactory.openSession();
                threadSession.set(s);
            }
        } catch (HibernateException ex) {
        //throw new InfrastructureException(ex);
        }
        return s;
    }

    public static void closeSession() {
        try {
            Session s = (Session) threadSession.get();
            threadSession.set(null);
            if (s != null && s.isOpen())
                    s.close();
            } catch (HibernateException ex) {
            //throw new InfrastructureException(ex);
        }
    }
    public static void beginTransaction() {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            if (tx == null) {
            tx = getSession().beginTransaction();
            threadTransaction.set(tx);
            }
            } catch (HibernateException ex) {
            //throw new InfrastructureException(ex);
        }
    }
    public static void commitTransaction() {
    Transaction tx = (Transaction) threadTransaction.get();
        try {
            if ( tx != null && !tx.wasCommitted()
            && !tx.wasRolledBack() )
                    tx.commit();
            threadTransaction.set(null);
            } catch (HibernateException ex) {
            rollbackTransaction();
            //throw new InfrastructureException(ex);
        }
    }
    public static void rollbackTransaction() {
    Transaction tx = (Transaction) threadTransaction.get();
    try {
        threadTransaction.set(null);
        if ( tx != null && !tx.wasCommitted()
        && !tx.wasRolledBack() ) {
            tx.rollback();
        }
        } catch (HibernateException ex) {
        //throw new InfrastructureException(ex);
        } finally {
        closeSession();
        }
    }

public static org.hibernate.stat.Statistics getStatistics(){
    return sessionFactory.getStatistics();
}

}
