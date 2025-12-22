package com.hotelfx.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create the SessionFactory from persistence.xml (JPA) or default hibernate.cfg.xml
                // Here we are using JPA persistence.xml, but Hibernate's SessionFactory is useful.
                // However, since we used persistence.xml, we usually use EntityManagerFactory.
                // Let's stick to pure JPA standard for 'GenericDao' later, or just Hibernate native.
                // The POM includes hibernate-core, so we can use native Hibernate if preferred, 
                // but the prompt asked for "JavaFX + Hibernate" usually implies JPA or native.
                // Let's use pure JPA EntityManagerFactory for simpler standard compliance.
                
                // Use Persistence.createEntityManagerFactory("hotelPU"); for JPA.
                // But for this Util, let's keep it simple if we switch to native Hibernate.
                // Actually, let's just make this a JPA Util.
                
                return null; // Refactored to JpaUtil below
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
