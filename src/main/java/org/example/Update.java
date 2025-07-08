package org.example;

import org.example.com.Key;
import org.example.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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
                System.out.println("count: " + count);
                update(count);
                sleep(100000);
                if (count > 2) count = 0;
                else count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(int count) {

        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().addAnnotatedClass(User.class).
                    addAnnotatedClass(Product.class).
                    addAnnotatedClass(Stock.class).
                    addAnnotatedClass(Item.class).
                    addAnnotatedClass(Media.class).
                    setProperty("hibernate.driver_class", Settings.getProperties("hibernate.driver_class")).
                    setProperty("hibernate.connection.url", Settings.getProperties("hibernate.connection.url")).
                    setProperty("hibernate.connection.username", Settings.getProperties("hibernate.connection.username")).
                    setProperty("hibernate.connection.password", Settings.getProperties("hibernate.connection.password")).
                    setProperty("hibernate.dialect", Settings.getProperties("hibernate.dialect")).
                    setProperty("hibernate.current_session_context_class", Settings.getProperties("hibernate.current_session_context_class")).
                    setProperty("hibernate.show_sql", Settings.getProperties("hibernate.show_sql")).
                    buildSessionFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                            generetedURL = URLRequestResponse.generateURL("wb", "info", user.getTokenStandartWB(), null);
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
                                System.out.println(response);
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    JSONObject jsonObject1 = new JSONObject(response);
                                    JSONObject jsonObject = jsonObject1.getJSONObject("data");
                                    for (int i = 0; i < jsonObject.getJSONArray("listGoods").length(); i++) {
                                        List<Product> products = user.getProducts();
                                        if (products.isEmpty()) {
                                            Product product = new Product("",
                                                    jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString(),
                                                    "",
                                                    parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).getJSONArray("sizes").getJSONObject(0).get("price").toString()),
                                                    parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("discount").toString()),
                                                    "wb", "", "", user);
                                            session.save(product);
                                        } else {
                                            boolean coincidence = false;
                                            for (Product p : products) {
                                                if (p.getNmId().equals(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString())) {
                                                    session.createQuery("update Product set price = "
                                                            + parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).getJSONArray("sizes").getJSONObject(0).get("price").toString())
                                                            + " WHERE nmId = '" + jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString() + "'").executeUpdate();
                                                    session.createQuery("update Product set discount = "
                                                            + parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("discount").toString())
                                                            + " WHERE nmId = '" + jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString() + "'").executeUpdate();

                                                    coincidence = true;
                                                }
                                            }
                                            if (!coincidence) {
                                                Product product = new Product("",
                                                        jsonObject.getJSONArray("listGoods").getJSONObject(i).get("nmID").toString(),
                                                        "",
                                                        parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).getJSONArray("sizes").getJSONObject(0).get("price").toString()),
                                                        parseInt(jsonObject.getJSONArray("listGoods").getJSONObject(i).get("discount").toString()),
                                                        "wb", "", "", user);
                                                session.save(product);
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                e.getMessage();
                            }
                        }
                    }
                }
            } else if (count == 1) {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStandartWB() != null) {
                            generetedURL = URLRequestResponse.generateURL("wb", "stocks", user.getTokenStandartWB(), null);
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
                                System.out.println(response);
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Product> products = user.getProducts();
                                        if (!products.isEmpty()) {
                                            for (Product p: products) {
                                                if (p.getNmId().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
                                                    if (p.getSupplierArticle().equals(""))
                                                        session.createQuery("update Product set supplierArticle = '"
                                                                + jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString()
                                                                + "' WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();
                                                    if (p.getSubject().equals(""))
                                                        session.createQuery("update Product set subject = '"
                                                                + jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString()
                                                                + "' WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();
                                                    List<Stock> stocks = p.getStocks();
                                                    if (stocks.isEmpty()) {
                                                        Stock stock = new Stock(jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
                                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
                                                                parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString()),
                                                                p);
                                                        session.save(stock);
                                                    } else {
                                                        boolean coincidence = false;
                                                        for (Stock s: stocks) {
                                                            if (s.getWarehouseName().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString())) {
                                                                session.createQuery("update Stock set quantity = "
                                                                        + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString())
                                                                        + " WHERE id = '"
                                                                        + s.getId()
                                                                        + "'").executeUpdate();
                                                                session.createQuery("update Stock set quantityFull = "
                                                                        + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString())
                                                                        + " WHERE id = '"
                                                                        + s.getId()
                                                                        + "'").executeUpdate();
                                                                session.createQuery("update Stock set inWayFromClient = "
                                                                        + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString())
                                                                        + " WHERE id = '"
                                                                        + s.getId()
                                                                        + "'").executeUpdate();
                                                                coincidence = true;
                                                            }
                                                        }
                                                        if (!coincidence) {
                                                            Stock stock = new Stock(jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantity").toString()),
                                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("quantityFull").toString()),
                                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("inWayFromClient").toString()),
                                                                    p);
                                                            session.save(stock);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (count == 2) {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStandartWB() != null) {
                            generetedURL = URLRequestResponse.generateURL("wb", "getCard", user.getTokenStandartWB(), null);
                            List<Product> products = user.getProducts();
                            try {
                                response = URLRequestResponse.getResponseFromURLandBodyRequest(generetedURL, user.getTokenStandartWB(), "");
//                                System.out.println(response);
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getJSONArray("cards").length() != 0) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("cards");
                                        if (!products.isEmpty()) {
                                            for (Product p : products) {
                                                if (!p.getSupplierArticle().equals("")) {
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        String str = jsonArray.getJSONObject(i).get("nmID").toString();
                                                        if (p.getNmId().equals(str)) {
                                                            session.createQuery("UPDATE Product set description = '" + jsonArray.getJSONObject(i).get("description") + "' WHERE id = " + p.getId()).executeUpdate();

                                                            List<Media> medias = p.getMedias();
                                                            if (medias.isEmpty()) {
                                                                JSONArray photos = jsonArray.getJSONObject(i).getJSONArray("photos");
                                                                for (int j = 0; j < photos.length(); j++) {
                                                                    Media media = new Media(photos.getJSONObject(j).get("big").toString(), j, p);
                                                                    session.save(media);
                                                                }
                                                            } else {
                                                                if (medias.size() != jsonArray.getJSONObject(i).getJSONArray("photos").length()) {
                                                                    session.createQuery("DELETE Media WHERE product_id = " + p.getId()).executeUpdate();
                                                                    JSONArray photos = jsonArray.getJSONObject(i).getJSONArray("photos");
                                                                    for (int j = 0; j < photos.length(); j++) {
                                                                        Media media = new Media(photos.getJSONObject(j).get("big").toString(), j, p);
                                                                        session.save(media);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {

                                    }
                                } else {

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (count == 3) {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStandartWB() != null) {
                            List<Product> products = user.getProducts();
                            if (!products.isEmpty()) {
                                for (Product p : products) {
                                    if (!p.getSupplierArticle().equals("")) {
                                        ArrayList<Key> keys = new ArrayList<>();
                                        keys.add(new Key("nmId", p.getNmId()));
                                        generetedURL = URLRequestResponse.generateURL("wb", "getRating", user.getTokenStandartWB(), keys);
                                        try {
                                            response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
                                            System.out.println(response);
                                            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                                JSONObject jsonObject = new JSONObject(response);
                                                session.createQuery("UPDATE Product set rating = '" + ((JSONObject) jsonObject.get("data")).get("valuation") + "' WHERE id = " + p.getId()).executeUpdate();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStandartWB() != null) {
                            generetedURL = URLRequestResponse.generateURL("wb", "orders", user.getTokenStandartWB(), null);
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Product> products = user.getProducts();
                                        for (Product p: products) {
                                            if (p.getNmId().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
                                                List<Item> items = p.getItems();
                                                boolean coincidence = false;
                                                for (Item it: items) {
                                                    if (it.getOdid().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString())) {
                                                        coincidence = true;
                                                        if (jsonObject.getJSONArray("price").getJSONObject(i).get("isCancel").toString().equals("true"))
                                                            session.createQuery("update Item set status = 'cancelled' WHERE id = '"
                                                                    + it.getId()
                                                                    + "'").executeUpdate();
                                                    }
                                                }
                                                if (!coincidence) {
                                                    String status = "ordered";
                                                    if (jsonObject.getJSONArray("price").getJSONObject(i).get("isCancel").toString().equals("true"))
                                                        status = "cancelled";
                                                    Item item = new Item(jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
                                                            "",
                                                            (int) ((Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString())) * (1 - (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("discountPercent").toString())) / 100)),
                                                            0,
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString(),
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString(),
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                            status,
                                                            p);
                                                    session.save(item);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            generetedURL = URLRequestResponse.generateURL("wb", "sales", user.getTokenStandartWB(), null);
                            try {
                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Product> products = user.getProducts();
                                        for (Product p: products) {
                                            if (p.getNmId().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
                                                List<Item> items = p.getItems();
                                                boolean coincidence = false;
                                                for (Item it: items) {
                                                    if (it.getOdid().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString())) {
                                                        coincidence = true;
                                                        String sdate = jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString().substring(0, 10);
                                                        String stime = jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString().substring(11, 19);
                                                        if (jsonObject.getJSONArray("price").getJSONObject(i).get("saleID").toString().substring(0, 1).equals("S"))
                                                            session.createQuery("update Item set status = 'sold', "
                                                                    + "sdate = '" + sdate
                                                                    + "', stime = '" + stime
                                                                    + "', finishedPrice = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString()))
                                                                    + ", forPay = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString()))
                                                                    + " WHERE odid = '" + it.getOdid()
                                                                    + "'").executeUpdate();
                                                        if (jsonObject.getJSONArray("price").getJSONObject(i).get("saleID").toString().substring(0, 1).equals("R"))
                                                            session.createQuery("update Item set status = 'returned', "
                                                                    + "sdate = '" + sdate
                                                                    + "', stime = '" + stime
                                                                    + "', finishedPrice = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString()))
                                                                    + ", forPay = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString()))
                                                                    + " WHERE odid = '" + it.getOdid()
                                                                    + "'").executeUpdate();
                                                    }
                                                }
                                                if (!coincidence) {
                                                    String status = "sold";
                                                    Item item = new Item("",
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
                                                            (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString())),
                                                            (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())),
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString(),
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString(),
                                                            jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                                            status,
                                                            p);

                                                    session.save(item);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

//                for (User user : users) {
//                    if (user.getNameShopOzon() != null) {
//                        if (user.getTokenClientOzon() != null) {
//                            if (user.getTokenStatisticOzon() != null) {
//                                generetedURL = URLRequestResponse.generateURL("ozon", "list", "", null);
//                                try {
//                                    response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenClientOzon(),  user.getTokenStatisticOzon(), "list", "", "");
//                                } catch (IOException | URISyntaxException e) {
//                                    e.printStackTrace();
//                                }
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject.get("result")));
//                                generetedURL = URLRequestResponse.generateURL("ozon", "info", "0", null);
//                                String answerString = "";
//                                for (int i = 0; i < jsonObject1.getJSONArray("items").length(); i++) {
//    //                                System.out.println(jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id"));
//                                    try {
//                                        response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenClientOzon(), user.getTokenStatisticOzon(), "info", jsonObject1.getJSONArray("items").getJSONObject(i).get("product_id").toString(), "0");
//                                    } catch (IOException | URISyntaxException e) {
//                                        e.printStackTrace();
//                                    }
//                                    JSONObject jsonObject2 = new JSONObject(response);
//                                    JSONObject jsonObject3 = new JSONObject(String.valueOf(jsonObject2.get("result")));
//                                    JSONObject jsonObject4 = new JSONObject(String.valueOf(jsonObject3.get("stocks")));
//
//                                    List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject3.get("id").toString()).getResultList();
//                                    if (products.isEmpty()) {
//                                        Product product = new Product(jsonObject3.get("offer_id").toString(),
//                                                jsonObject3.get("id").toString(),
//                                                jsonObject3.get("name").toString(),
//                                                (int) Float.parseFloat(jsonObject3.get("old_price").toString()),
//                                                (int) (100 * (1 - (Float.parseFloat(jsonObject3.get("price").toString()))/(Float.parseFloat(jsonObject3.get("old_price").toString())))),
//                                                "ozon", "", "");
//                                        session.save(product);
//                                    } else {
//                                        session.createQuery("update Product set price = "
//                                                + (int) Float.parseFloat(jsonObject3.get("old_price").toString())
//                                                + " WHERE nmId = '" + jsonObject3.get("id").toString() + "'").executeUpdate();
//                                        session.createQuery("update Product set discount = "
//                                                + (int) (100 * (1 - (Float.parseFloat(jsonObject3.get("price").toString()))/(Float.parseFloat(jsonObject3.get("old_price").toString()))))
//                                                + " WHERE nmId = '" + jsonObject3.get("id").toString() + "'").executeUpdate();
//                                    }
//                                      answerString = answerString
//                                            + "Наименование: "
//                                            + jsonObject3.get("name").toString()
//                                            + "\n"
//                                            + "Артикул: "
//                                            + jsonObject3.get("id").toString()
//                                            + "\n"
//                                            + "Цена: "
//                                            + jsonObject3.get("old_price").toString()
//                                            + "\n"
//                                            + "С учетом скидки: "
//                                            + jsonObject3.get("price").toString()
//                                            + "\n"
//                                            + "Остаток на складе: "
//                                            + jsonObject4.get("present").toString()
//                                            + "\n"
//                                            + "Товары в пути: "
//                                            + jsonObject4.get("reserved").toString()
//                                            + "\n"
//                                            + "Ожидаемая выручка: "
//                                            + "\n"
//                                            + "\n";
//                                }
//    //                            System.out.println(answerString);
//                            }
//                        }
//                    }
//                }
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
