package org.example;

import org.example.com.Key;
import org.example.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
                sleep(10000);
                if (count > 3) count = 0;
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
                addAnnotatedClass(Item.class).
                addAnnotatedClass(Media.class).buildSessionFactory();
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
                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                    System.out.println(response);
                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                                        List<Product> products = user.getProducts();
                                        if (products.isEmpty()) {
                                            Product product = new Product("",
                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString(),
                                                    "",
                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("price").toString()),
                                                    parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("discount").toString()),
                                                    "wb", "", "", user);
                                            session.save(product);
                                        } else {
                                            boolean coincidence = false;
                                            for (Product p: products) {
                                                if (p.getNmId().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString())) {
                                                    session.createQuery("update Product set price = "
                                                            + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("price").toString())
                                                            + " WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();
                                                    session.createQuery("update Product set discount = "
                                                            + parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("discount").toString())
                                                            + " WHERE nmId = '" + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString() + "'").executeUpdate();

                                                    coincidence = true;
                                                }
                                            }
                                            if (!coincidence) {
                                                Product product = new Product("",
                                                        jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString(),
                                                        "",
                                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("price").toString()),
                                                        parseInt(jsonObject.getJSONArray("price").getJSONObject(i).get("discount").toString()),
                                                        "wb", "", "", user);
                                                session.save(product);
                                            }
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


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
            } else if (count == 1) {
                for (User user : users) {
                    if (user.getNameShopWB() != null) {
                        if (user.getTokenStandartWB() != null) {
                            generetedURL = URLRequestResponse.generateURL("wb", "getCard", user.getTokenStandartWB(), null);
                            List<Product> products = user.getProducts();
                            if (!products.isEmpty()) {
                                for (Product p : products) {
                                    if (!p.getSupplierArticle().equals("")) {
                                        try {
                                            response = URLRequestResponse.getResponseFromURLandBodyRequest(generetedURL, user.getTokenStandartWB(), p.getSupplierArticle());
                                            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getJSONArray("data").length() != 0) {
                                                    System.out.println(response);
                                                    List<Media> medias = p.getMedias();
                                                    if (medias.isEmpty()) {
                                                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                                                            if (jsonObject.getJSONArray("data").getJSONObject(i).get("vendorCode").equals(p.getSupplierArticle())) {
                                                                for (int j = 0; j < jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("mediaFiles").length(); j++) {
                                                                    System.out.println(jsonObject.getJSONArray("data").getJSONObject(i).get("vendorCode"));
                                                                    Media media = new Media(jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("mediaFiles").get(j).toString(), j, p);
                                                                    session.save(media);
                                                                }
                                                            }
                                                        }


//                                                    } else {
//                                                        if (medias.size() != jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("mediaFiles").length()) {
//                                                            session.createQuery("DELETE Media WHERE product_id = " + product.getId()).executeUpdate();
//                                                            for (int i = 0; i < jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("mediaFiles").length(); i++) {
//                                                                Media media = new Media(jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("mediaFiles").get(i).toString(), i, product);
//                                                                session.save(media);
//                                                            }
//                                                        }
                                                    }
                                                    session.createQuery("UPDATE Product set description = '" + jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("characteristics").getJSONObject(jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("characteristics").length() - 1).get("Описание") + "' WHERE id = " + p.getId()).executeUpdate();

                                                }
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
            } else if (count == 2) {
//                for (User user : users) {
//                    if (user.getNameShopWB() != null) {
//                        if (user.getTokenStandartWB() != null) {
//                            List<Product> products = session.createQuery("FROM Product").getResultList();
//                            if (!products.isEmpty()) {
//                                for (Product product : products) {
//                                    if (!product.getSupplierArticle().equals("")) {
//                                        ArrayList<Key> keys = new ArrayList<>();
//                                        keys.add(new Key("nmId", product.getNmId()));
//                                        generetedURL = URLRequestResponse.generateURL("wb", "getRating", user.getTokenStandartWB(), keys);
//                                        try {
//                                            response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStandartWB());
//                                            System.out.println(response);
//                                            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                                JSONObject jsonObject = new JSONObject(response);
//                                                session.createQuery("UPDATE Product set rating = '" + ((JSONObject) jsonObject.get("data")).get("valuation") + "' WHERE id = " + product.getId()).executeUpdate();
//                                            }
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            } else {
//                for (User user : users) {
//                    if (user.getNameShopWB() != null) {
//                        if (user.getTokenStatisticWB() != null) {
//                            generetedURL = URLRequestResponse.generateURL("wb", "orders", user.getTokenStatisticWB(), null);
//                            try {
//                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
//                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                    System.out.println(response);
//                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
//                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
//                                        List<Item> items = session.createQuery("FROM Item WHERE odid LIKE '" + jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString() + "'").getResultList();
//                                        if (items.isEmpty()) {
//                                            List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
//                                            String status = "ordered";
//                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("isCancel").toString().equals("true"))
//                                                status = "cancelled";
//                                            Item item = new Item(jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
//                                                    "",
//                                                    (int) ((Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString())) * (1 - (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("discountPercent").toString())) / 100)),
//                                                    0,
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString(),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString(),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
//                                                    status,
//                                                    products.get(0));
//                                            session.save(item);
//                                        } else {
//                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("isCancel").toString().equals("true"))
//                                                session.createQuery("update Item set status = 'cancelled' WHERE id = '"
//                                                        + items.get(0).getId()
//                                                        + "'").executeUpdate();
//                                        }
//                                    }
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            generetedURL = URLRequestResponse.generateURL("wb", "sales", user.getTokenStatisticWB(), null);
//                            try {
//                                response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticWB());
//                                if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
//                                    System.out.println(response);
//                                    JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
//                                    for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
//                                        List<Item> items = session.createQuery("FROM Item WHERE odid LIKE '" + jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString() + "'").getResultList();
//                                        if (items.isEmpty()) {
//                                            List<Product> products = session.createQuery("FROM Product WHERE nmId LIKE " + jsonObject.getJSONArray("price").getJSONObject(i).get("nmId").toString()).getResultList();
//                                            Item item = new Item("",
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString(),
//                                                    (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString())),
//                                                    (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("gNumber").toString(),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString(),
//                                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
//                                                    "sold",
//                                                    products.get(0));
//
//                                            session.save(item);
//                                        } else {
//                                            String sdate = jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString().substring(0, 10);
//                                            String stime = jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString().substring(11, 19);
//                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("saleID").toString().substring(0, 1).equals("S"))
//                                                session.createQuery("update Item set status = 'sold', "
//                                                        + "sdate = '" + sdate
//                                                        + "', stime = '" + stime
//                                                        + "', finishedPrice = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString()))
//                                                        + ", forPay = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString()))
//                                                        + " WHERE odid = '" + items.get(0).getOdid()
//                                                        + "'").executeUpdate();
//                                            if (jsonObject.getJSONArray("price").getJSONObject(i).get("saleID").toString().substring(0, 1).equals("R"))
//                                                session.createQuery("update Item set status = 'returned', "
//                                                        + "sdate = '" + sdate
//                                                        + "', stime = '" + stime
//                                                        + "', finishedPrice = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("finishedPrice").toString()))
//                                                        + ", forPay = " + (int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString()))
//                                                        + " WHERE odid = '" + items.get(0).getOdid()
//                                                        + "'").executeUpdate();
//
//                                        }
//                                    }
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
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
