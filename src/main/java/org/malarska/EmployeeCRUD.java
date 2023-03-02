package org.malarska;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeCRUD {
    private SessionFactory sessionFactory;

    public EmployeeCRUD() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(ssr).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
    }

    public Integer saveEmployee(Employee employee) {
        Session session = null;
        Integer id = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            id = (Integer) session.save(employee);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public Employee getEmployee(Integer id) {
        Session session = null;
        Employee employee = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            String hql = "FROM Employee E WHERE E.id=" + id;
            Query query = session.createQuery(hql);
            List results = query.list();
            if (results.size() > 0) employee = (Employee) results.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employee;
    }

    public List getEmployee() {
        Session session = null;
        Employee employee = null;
        List results = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            String hql = "FROM Employee ";
            Query query = session.createQuery(hql);
            results = query.list();
            if (results.size() > 0) employee = (Employee) results.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public void updateEmployee(Integer id, String name) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Employee employee = (Employee) session.get(Employee.class, id);
            employee.setName(name);

            session.update(employee);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public void deleteEmployee(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Employee employee = (Employee) session.get(Employee.class, id);
            session.delete(employee);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public void run() {
        Employee employee = new Employee(0, "Lukasz", "Kowalski",
                "Programmer", "Warszawa", 30, 10000);
        Integer newId = saveEmployee(employee);
        System.out.println("zapis rekord, id: " + newId);
        Employee savedEmployee = getEmployee(newId);
        System.out.println("\nOstatni dodatny pracownik to: " + savedEmployee);

        updateEmployee(newId, "Paweł");

        List<Employee> results = getEmployee();
        System.out.println("liczba rekordów: " + results.size());
        for (int i = 0; i < results.size(); i++) {
            System.out.println("Pracownik: " + i + ": " + results.get(i));
        }
        System.out.println("Skasowanie pracownika o id: "+results.get(0).getId());
        deleteEmployee(results.get(0).getId()); // kasowanie pierwszego resultatu

    }

    public static void main(String[] args) {
        EmployeeCRUD employeeCRUD = new EmployeeCRUD();
        employeeCRUD.run();
    }
}
