package com.klef.exam.jfsd;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ClientDemo {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        // Initialize Hibernate
        sessionFactory = new Configuration().configure().buildSessionFactory();

        // Insert records
        insertProjects();

        // Perform aggregate operations
        performAggregateFunctions();

        // Close SessionFactory
        sessionFactory.close();
    }

    private static void insertProjects() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Project p1 = new Project();
            p1.setProjectName("AI Development");
            p1.setDuration(12);
            p1.setBudget(100000);
            p1.setTeamLead("Alice");

            Project p2 = new Project();
            p2.setProjectName("Web Application");
            p2.setDuration(6);
            p2.setBudget(50000);
            p2.setTeamLead("Bob");

            Project p3 = new Project();
            p3.setProjectName("Mobile App");
            p3.setDuration(9);
            p3.setBudget(75000);
            p3.setTeamLead("Charlie");

            session.save(p1);
            session.save(p2);
            session.save(p3);

            transaction.commit();
            System.out.println("Projects inserted successfully.");
        }
    }

    private static void performAggregateFunctions() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();

            // Aggregate functions on Budget
            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            Root<Project> root = countQuery.from(Project.class);
            countQuery.select(builder.count(root));
            long count = session.createQuery(countQuery).getSingleResult();
            System.out.println("Count of Projects: " + count);

            CriteriaQuery<Double> maxQuery = builder.createQuery(Double.class);
            maxQuery.select(builder.max(root.get("budget")));
            double maxBudget = session.createQuery(maxQuery).getSingleResult();
            System.out.println("Maximum Budget: " + maxBudget);

            CriteriaQuery<Double> minQuery = builder.createQuery(Double.class);
            minQuery.select(builder.min(root.get("budget")));
            double minBudget = session.createQuery(minQuery).getSingleResult();
            System.out.println("Minimum Budget: " + minBudget);

            CriteriaQuery<Double> sumQuery = builder.createQuery(Double.class);
            sumQuery.select(builder.sum(root.get("budget")));
            double sumBudget = session.createQuery(sumQuery).getSingleResult();
            System.out.println("Sum of Budgets: " + sumBudget);

            CriteriaQuery<Double> avgQuery = builder.createQuery(Double.class);
            avgQuery.select(builder.avg(root.get("budget")));
            double avgBudget = session.createQuery(avgQuery).getSingleResult();
            System.out.println("Average Budget: " + avgBudget);
        }
    }
}
