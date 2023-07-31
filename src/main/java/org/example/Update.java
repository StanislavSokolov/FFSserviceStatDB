package org.example;

import org.example.model.Item;
import org.example.model.Product;
import org.example.model.Stock;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Update extends Thread {

    @Override
    public void run() {
        int count = 0;

        super.run();
        while (true) {
            try {
                update(count);
                sleep(900000);
                if (count > 4) count = 0; else count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(int count) {

        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(User.class).
                addAnnotatedClass(Product.class).
                addAnnotatedClass(Stock.class).
                addAnnotatedClass(Item.class).buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();

//            User user = new User("371119",
//                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6IjUwYmUzZWZiLWZhNmEtNGZhMC1iZGE0LTg4ZGM3NDliNzU5NCJ9.08aYlKx0h1fbWQISH_I9MP68D66Rh8VRXg4M-JNNYTE",
//                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6IjJhZjljODg4LTY3YTMtNGI1NS04NTM5LWRlNmM1OGQwNjgyZCJ9.JXxWyT0eG_PlmgB7cfhkepX0tbvZdqUAywO2ZXkuhXM",
//                    "TOKENWBSTANDART",
//                    "ozon",
//                    "1186106d-3219-4089-bd68-24f2ffed8dfa",
//                    "341256");
//            session.save(user);

//            Item item = new Item("Idiot", person);

//            person.setItems(new ArrayList<>(Collections.singletonList(item)));

//            session.save(person);



//            Person person = new Person("Name", 30, "email@mail.ru", "Kudrovo");
//            Item item = new Item("Test cascading item", person);
//            person.setItems(new ArrayList<>(Collections.singletonList(item)));

//            List<User> users = session.createQuery("FROM Person WHERE name LIKE 'T%'").getResultList();
//            session.createQuery("update Person set name = 'Test' WHERE age > 30").executeUpdate();
//            session.createQuery("delete from Person where age > 30").executeUpdate();
            List<User> users = session.createQuery("FROM User").getResultList();
            for (User user : users) {
                if (user.getNameShopWB() != null) {
                    if (user.getTokenStatisticWB() != null) {
                        URL generetedURL = null;
                        String response = null;
                        generetedURL = URLRequestResponse.generateURL("wb", "info", user.getTokenStandartWB());
                        try {
                            response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
                            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                System.out.println(response);
                                JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                    List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
                                    if (products.isEmpty()) {
                                        Product product = new Product("",
                                                jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString(),
                                                "",
                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("price").toString()),
                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("discount").toString()),
                                                "WB");
                                        session.save(product);
                                    } else {
                                        session.createQuery("update Product set price = "
                                                + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("price").toString())
                                                + " WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();
                                        session.createQuery("update Product set discount = "
                                                + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("discount").toString())
                                                + " WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();

                                    }
                                }
                            }
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }

//                            SQL.upDate1(items, "wb");

//                        generetedURL = URLRequestResponse.generateURL("wb", "stocks", user.getTokenStatisticWB());
//                        try {
//                            response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
//                            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                ArrayList<Item> items = new ArrayList<>();
//                                JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
//                                for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
//                                    boolean coincidence = false;
//                                    if (items.isEmpty()) {
//                                        items.add(new Item(jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
//                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
//                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
//                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()),
//                                                jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString(),
//                                                jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString()));
//                                    } else {
//                                        for (Item itemCurrent : items) {
//                                            if (itemCurrent.getNmId() == parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
//                                                itemCurrent.setQuantity(itemCurrent.getQuantity() + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()));
//                                                itemCurrent.setQuantityFull(itemCurrent.getQuantityFull() + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()));
//                                                itemCurrent.addQuantityWarehouse(parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()), jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString());
//                                                coincidence = true;
//                                            }
//                                        }
//                                        if (!coincidence) {
//                                            items.add(new Item(jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
//                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
//                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
//                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString(),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString()));
//                                        }
//                                        coincidence = false;
//                                    }
//                                }

                    }
                }
            }


            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
