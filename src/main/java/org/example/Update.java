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
        int count = 1;

        super.run();
        while (true) {
            try {
                update(count);
                sleep(300000);
                if (count > 4) count = 0;
                else count++;
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
            URL generetedURL = null;
            String response = null;
            List<User> users = session.createQuery("FROM User").getResultList();
            if (count == 0) {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStandartWB() != null) {
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
                        }

                        if (user.getTokenStatisticWB() != null) {
                            generetedURL = URLRequestResponse.generateURL("wb", "stocks", user.getTokenStatisticWB());
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
                                System.out.println(response);
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
                                        if (!products.isEmpty()) {
                                            if (products.get(0).getSupplierArticle().equals(""))
                                                session.createQuery("update Product set supplierArticle = '"
                                                        + jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString()
                                                        + "' WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();
                                            if (products.get(0).getSubject().equals(""))
                                                session.createQuery("update Product set subject = '"
                                                        + jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString()
                                                        + "' WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();
                                            List<Stock> stocks = session.createQuery("FROM Stock WHERE product_id LIKE " + products.get(0).getId() + " and warehouseName LIKE '" + jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString() + "'").getResultList();
                                            if (stocks.isEmpty()) {
                                                Stock stock = new Stock(jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
                                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
                                                        products.get(0));
                                                session.save(stock);
                                            } else {
                                                session.createQuery("update Stock set quantity = "
                                                        + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString())
                                                        + " WHERE id = '"
                                                        + stocks.get(0).getId()
                                                        + "'").executeUpdate();
                                                session.createQuery("update Stock set quantityFull = "
                                                        + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString())
                                                        + " WHERE id = '"
                                                        + stocks.get(0).getId()
                                                        + "'").executeUpdate();
                                            }
                                        }
                                    }
                                }
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStatisticWB() != null) {
                            generetedURL = URLRequestResponse.generateURL("wb", "sales", user.getTokenStatisticWB());
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    System.out.println(response);
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Item> items = session.createQuery("FROM Item WHERE odid LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("odid").toString()).getResultList();
                                        if (items.isEmpty()) {
                                            List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
                                            Item item = new Item(jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
                                                    (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString())),
                                                    (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("odid").toString(),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("oblastOkrugName").toString(),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                    "sale",
                                                    products.get(0));
                                            session.save(item);
                                        }
                                    }
                                }
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
