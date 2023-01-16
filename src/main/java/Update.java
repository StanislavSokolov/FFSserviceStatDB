import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONObject;

public class Update extends Thread {
    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                update();
                sleep(1800000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayList<ItemShop> products = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        URL generetedURL;
        String response = null;

        generetedURL = URLRequestResponse.generateURL(2, 6, "token");
        try {
            response = URLRequestResponse.getResponseFromURL(generetedURL, "token");

            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                    boolean coincidence = false;
                    String s = jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString();
                    s = s.substring(0, 10);
                    if (s.equals(URLRequestResponse.getDataCurrent())) {
                        if (products.isEmpty()) {
                            products.add(new ItemShop(jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString(),
                                    String.valueOf((int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())*0.9)),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString()));
                        } else {
                            for (ItemShop itemShopCurrent : products) {
                                if (itemShopCurrent.getSupplierArticle().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString())) {
                                    itemShopCurrent.setSale(itemShopCurrent.getSale() + 1);
                                    itemShopCurrent.setRating(itemShopCurrent.getRating() + 1);
                                    itemShopCurrent.setForPay(String.valueOf((int) (Float.parseFloat(itemShopCurrent.getForPay()) + Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())*0.9)));
                                    coincidence = true;
                                }
                            }
                            if (!coincidence) {
                                products.add(new ItemShop(jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString(),
                                        String.valueOf((int) (Float.parseFloat(jsonObject.getJSONArray("price").getJSONObject(i).get("forPay").toString())*0.9)),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("regionName").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString()));
                            }
                        }
                    }
                }
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        generetedURL = URLRequestResponse.generateURL(2, 7, "token");
        try {
            response = URLRequestResponse.getResponseFromURL(generetedURL, "token");

            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                JSONObject jsonObject = new JSONObject("{\"price\":" + response + "}");
                for (int i = 0; i < jsonObject.getJSONArray("price").length(); i++) {
                    boolean coincidence = false;
                    String s = jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString();
                    s = s.substring(0, 10);
                    if (s.equals(URLRequestResponse.getDataCurrent())) {
                        if (products.isEmpty()) {
                            products.add(new ItemShop(jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("oblast").toString(),
                                    jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString()));
                        } else {
                            for (ItemShop itemShopCurrent : products) {
                                if (itemShopCurrent.getSupplierArticle().equals(jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString())) {
                                    itemShopCurrent.setOrder(itemShopCurrent.getOrder() + 1);
                                    itemShopCurrent.setRating(itemShopCurrent.getRating() + 1);
                                    coincidence = true;
                                }
                            }
                            if (!coincidence) {
                                products.add(new ItemShop(jsonObject.getJSONArray("price").getJSONObject(i).get("subject").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("supplierArticle").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("totalPrice").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("warehouseName").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("oblast").toString(),
                                        jsonObject.getJSONArray("price").getJSONObject(i).get("date").toString()));
                            }
                        }
                    }
                }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                ItemShop productPopular = null;
                if (!products.isEmpty()) {
                    products.sort((o1, o2) -> o2.getRating() - o1.getRating());
                    productPopular = products.get(0);
                }

                int sumSale = 0;
                int sumOrder = 0;
                int sumSaleMoney = 0;

                for (ItemShop ishop : products) {
                    sumSale = sumSale + ishop.getSale();
                    sumOrder = sumOrder + ishop.getOrder();
                    String forPay = ishop.getForPay();
                    sumSaleMoney = (int) (sumSaleMoney + Float.parseFloat(forPay));
                }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                SQL.upDate(URLRequestResponse.getDataCurrent(), sumSale, sumOrder, sumSaleMoney, productPopular.getSubject() + " " + productPopular.getSupplierArticle());
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
