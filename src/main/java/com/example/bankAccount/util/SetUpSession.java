package com.example.bankAccount.util;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class SetUpSession {

  private static EntityManagerFactory entityManagerFactory = null;

  public SetUpSession(EntityManagerFactory entityManagerFactory) {
    SetUpSession.entityManagerFactory = entityManagerFactory;
  }

  public static Session getSession () {
    SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    return  sessionFactory.openSession();
  }

}
