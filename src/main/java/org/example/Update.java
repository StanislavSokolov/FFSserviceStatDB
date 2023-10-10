package org.example;

import org.example.model.Item;
import org.example.model.Product;
import org.example.model.Stock;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
                sleep(10000);
                if (count > 1) count = 0;
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
//                                    System.out.println(response);
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
                                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
                                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString()),
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
                                                session.createQuery("update Stock set inWayFromClient = "
                                                        + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString())
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
                            generetedURL = URLRequestResponse.generateURL("wb", "orders", user.getTokenStatisticWB());
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                    System.out.println(response);
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Item> items = session.createQuery("FROM Item WHERE odid LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("odid").toString()).getResultList();
                                        if (items.isEmpty()) {
                                            List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
                                            String status = "ordered";
                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("isCancel").toString().equals("true")) status = "cancelled";
                                            Item item = new Item(jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
                                                    "",
                                                    (int) ((Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString())) * (1 - (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("discountPercent").toString()))/100)),
                                                    0,
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("odid").toString(),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("oblast").toString(),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                    status,
                                                    products.get(0));
                                            session.save(item);
                                        } else {
                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("isCancel").toString().equals("true"))
                                                session.createQuery("update Item set status = 'cancelled' WHERE id = '"
                                                    + items.get(0).getId()
                                                    + "'").executeUpdate();
                                        }
                                    }
                                }
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                            generetedURL = URLRequestResponse.generateURL("wb", "sales", user.getTokenStatisticWB());
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                    System.out.println(response);
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Item> items = session.createQuery("FROM Item WHERE odid LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("odid").toString()).getResultList();
                                        if (items.isEmpty()) {
                                            List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
                                            Item item = new Item("",
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
                                                    (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString())),
                                                    (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("odid").toString(),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("oblastOkrugName").toString(),
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                    "sold",
                                                    products.get(0));
                                            session.save(item);
                                        } else {
                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("saleID").toString().substring(0, 0).equals("S"))
                                                session.createQuery("update Item set status = 'sold' WHERE id = '"
                                                    + items.get(0).getId()
                                                    + "'").executeUpdate();
                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("saleID").toString().substring(0, 0).equals("R")) {
                                                session.createQuery("update Item set status = 'returned' WHERE id = '"
                                                        + items.get(0).getId()
                                                        + "'").executeUpdate();
                                                session.createQuery("update Item set finishedPrice = '0' WHERE id = '"
                                                        + items.get(0).getId()
                                                        + "'").executeUpdate();
                                                session.createQuery("update Item set forPay = '0' WHERE id = '"
                                                        + items.get(0).getId()
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
            }

            for (User user : users) {
                if (user.getNameShopOzon() != null) {
                    if (user.getTokenClientOzon() != null) {
                        if (user.getTokenStatisticOzon() != null) {
                            generetedURL = URLRequestResponse.generateURL("ozon", "list", "");
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenClientOzon(),  user.getTokenStatisticOzon(), "list", "", "");
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject.get("result")));
                            generetedURL = URLRequestResponse.generateURL("ozon", "info", "0");
                            String answerString = "";
                            for (int i = 0; i < jsonObject1.getJSONArray("items").length(); i++) {
//                                System.out.println(jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id"));
                                try {
                                    response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenClientOzon(), user.getTokenStatisticOzon(), "info", jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id").toString(), "0");
                                } catch (IOException | URISyntaxException e) {
                                    e.printStackTrace();
                                }
                                JSONObject jsonObject2 = new JSONObject(response);
                                JSONObject jsonObject3 = new JSONObject(String.valueOf(jsonObject2.get("result")));
                                JSONObject jsonObject4 = new JSONObject(String.valueOf(jsonObject3.get("stocks")));

                                List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject3.get("id").toString()).getResultList();
                                if (products.isEmpty()) {
                                    Product product = new Product(jsonObject3.get("offer_id").toString(),
                                            jsonObject3.get("id").toString(),
                                            jsonObject3.get("name").toString(),
                                            (int) Float.parseFloat(jsonObject3.get("old_price").toString()),
                                            (int) (100 * (1 - (Float.parseFloat(jsonObject3.get("price").toString()))/(Float.parseFloat(jsonObject3.get("old_price").toString())))),
                                            "OZON");
                                    session.save(product);
                                } else {
                                    session.createQuery("update Product set price = "
                                            + (int) Float.parseFloat(jsonObject3.get("old_price").toString())
                                            + " WHERE nmId = '" + jsonObject3.get("id").toString() + "'").executeUpdate();
                                    session.createQuery("update Product set discount = "
                                            + (int) (100 * (1 - (Float.parseFloat(jsonObject3.get("price").toString()))/(Float.parseFloat(jsonObject3.get("old_price").toString()))))
                                            + " WHERE nmId = '" + jsonObject3.get("id").toString() + "'").executeUpdate();
                                }
//                                  answerString = answerString
//                                        + "Наименование: "
//                                        + jsonObject3.get("name").toString()
//                                        + "\n"
//                                        + "Артикул: "
//                                        + jsonObject3.get("id").toString()
//                                        + "\n"
//                                        + "Цена: "
//                                        + jsonObject3.get("old_price").toString()
//                                        + "\n"
//                                        + "С учетом скидки: "
//                                        + jsonObject3.get("price").toString()
//                                        + "\n"
//                                        + "Остаток на складе: "
//                                        + jsonObject4.get("present").toString()
//                                        + "\n"
//                                        + "Товары в пути: "
//                                        + jsonObject4.get("reserved").toString()
//                                        + "\n"
//                                        + "Ожидаемая выручка: "
//                                        + "\n"
//                                        + "\n";
                            }
//                            System.out.println(answerString);
                        }
                    }


                }
            }
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }

    private int delimiter(String s, String delimiter) {
        String str = s;
        String[] subStr;
        subStr = str.split(delimiter); // Разделения строки str с помощью метода split()
        return Integer.parseInt(subStr[0]);
    }
}
