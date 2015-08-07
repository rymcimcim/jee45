package pl.jeeweb.zadanie4.util;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pl.jeeweb.zadanie4.entity.Pacjent;

public class CRUDRunner {

    private static Pacjent pacjent;
    private static List pacjentList;
    private static List filterList;

    public static void create(String imie, String nazwisko, String pesel,
            Date data, String adres, String telefon, float waga, float wzrost) {
        pacjent = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            pacjent = new Pacjent();
            pacjent.setImie(imie);
            pacjent.setNazwisko(nazwisko);
            pacjent.setPesel(pesel);
            pacjent.setData(data);
            pacjent.setAdres(adres);
            pacjent.setTelefon(telefon);
            pacjent.setWaga(waga);
            pacjent.setWzrost(wzrost);
            session.save(pacjent);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static List retrieve() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        pacjentList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            pacjentList = session.createCriteria(Pacjent.class).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return pacjentList;
    }

    public static Pacjent retrieveFromPesel(String pesel) {
        pacjent = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String queryString = "from PACJENT where pesel = :pesel";
            Query query = session.createQuery(queryString);
            query.setString("pesel", pesel);
            Object queryResult = query.uniqueResult();
            pacjent = (Pacjent) queryResult;
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return pacjent;
    }

    public static List filterRetrive(String imie, String nazwisko, String pesel,
            Date urOd, Date urDo, int wagaOd, int wagaDo, int wzrostOd, int wzrostDo,
            Date dodOd, Date dodDo) {
        filterList = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Pacjent.class);
            if (imie != null)
                criteria.add(Restrictions.eq("imie", imie));
            if (nazwisko != null)
                criteria.add(Restrictions.eq("nazwisko", nazwisko));
            if (pesel != null)
                criteria.add(Restrictions.eq("pesel", pesel));
            if (urOd != null)
                criteria.add(Restrictions.ge("data", urOd.toString()));
            if (urDo != null)
                criteria.add(Restrictions.le("data", urDo.toString()));
            if (wagaOd > 0)
                criteria.add(Restrictions.ge("waga", wagaOd));
            if (wagaDo > 0)
                criteria.add(Restrictions.le("waga", wagaDo));
            if (wzrostOd > 0)
                criteria.add(Restrictions.gt("wzrost", wzrostOd));
            if (wzrostDo > 0)
                criteria.add(Restrictions.gt("wzrost", wzrostDo));
            if (dodOd != null)
                criteria.add(Restrictions.ge("dataDod", dodOd.toString()));
            if (dodDo != null)
                criteria.add(Restrictions.le("dataDod", dodDo.toString()));
            filterList = criteria.list();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return filterList;
    }

    /*public static Address getAddress(int id) {
     address = null;
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction tx = null;
     try {
     tx = session.beginTransaction();
     Criteria criteria = session.createCriteria(Address.class);
     criteria.add(Restrictions.eq("addr_id", id));
     address = (Address)criteria.uniqueResult();
     tx.commit();
     } catch (HibernateException e) {
     if (tx != null) {
     tx.rollback();
     }
     } finally {
     session.close();
     }
     return address;
     }
    
     public static void deleteAddress(int i) {
     address = null;
     address = getAddress(i);
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction tx = null;
     try {
     tx = session.beginTransaction();
     session.delete(address);
     tx.commit();
     } catch (HibernateException e) {
     if (tx != null) {
     tx.rollback();
     }
     } finally {
     session.close();
     }
     }

     public static List retriveUserAddresses(String username) {
     addressList = null;
     pacjent = null;
     pacjent = (User) retrieveFromUsername(username);
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction tx = null;
     try {
     tx = session.beginTransaction();
     Criteria criteria = session.createCriteria(Address.class);
     criteria.add(Restrictions.eq("user", pacjent));
     addressList = criteria.list();
     tx.commit();
     } catch (HibernateException e) {
     if (tx != null) {
     tx.rollback();
     }
     } finally {
     session.close();
     }
     return addressList;
     }

     public static void updateUser(User user) {
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction tx = null;
     try {
     tx = session.beginTransaction();
     session.update(user);
     tx.commit();
     } catch (HibernateException e) {
     if (tx != null) {
     tx.rollback();
     }
     } finally {
     session.close();
     }
     }
    
     public static void updateAddress(Address address) {
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction tx = null;
     try {
     tx = session.beginTransaction();
     session.update(address);
     tx.commit();
     } catch (HibernateException e) {
     if (tx != null) {
     tx.rollback();
     }
     } finally {
     session.close();
     }
     }

     public static void updateAll() {
     AnnotationConfiguration config
     = new AnnotationConfiguration();
     config.addAnnotatedClass(User.class);
     SessionFactory factory
     = config.configure().buildSessionFactory();
     Session session = factory.getCurrentSession();
     session.beginTransaction();
     List allUsers;
     System.out.println("Updating all records...");
     Query queryResult = session.createQuery("from User");
     allUsers = queryResult.list();
     System.out.println("# of rows: " + allUsers.size());
     for (int i = 0; i < allUsers.size(); i++) {
     User pacjent = (User) allUsers.get(i);
     System.out.println(pacjent);
     pacjent.setPassword("password");
     session.update(pacjent);
     }
     System.out.println("Database contents updated...");
     session.getTransaction().commit();
     }

     public static void deleteAll() {
     AnnotationConfiguration config = new AnnotationConfiguration();
     config.addAnnotatedClass(User.class);
     SessionFactory factory = config.configure().buildSessionFactory();
     Session session = factory.getCurrentSession();
     List allUsers;
     session.beginTransaction();
     Query queryResult = session.createQuery("from User");
     allUsers = queryResult.list();
     for (int i = 0; i < allUsers.size(); i++) {
     User pacjent = (User) allUsers.get(i);
     System.out.println(pacjent);
     session.delete(pacjent);
     }
     session.getTransaction().commit();
     }
     }*/
}
