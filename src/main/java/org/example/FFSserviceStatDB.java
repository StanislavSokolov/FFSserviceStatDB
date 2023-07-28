package org.example;

import org.example.model.Product;
import org.example.model.Stock;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FFSserviceStatDB {
    public static void main( String[] args ) {

        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(User.class).
                addAnnotatedClass(Product.class).
                addAnnotatedClass(Stock.class).buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

//            Person person = new Person("Nanya", 26, "Oku", "nn@mail.ru");

//            Item item = new Item("Idiot", person);

//            person.setItems(new ArrayList<>(Collections.singletonList(item)));

//            session.save(person);



//            Person person = new Person("Name", 30, "email@mail.ru", "Kudrovo");
//            Item item = new Item("Test cascading item", person);
//            person.setItems(new ArrayList<>(Collections.singletonList(item)));

//            List<Person> people = session.createQuery("FROM Person WHERE name LIKE 'T%'").getResultList();
//            session.createQuery("update Person set name = 'Test' WHERE age > 30").executeUpdate();
//            for (Person p : people) {
//                System.out.println(p.getName());
//            }
//            session.createQuery("delete from Person where age > 30").executeUpdate();


            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }

    }
}


