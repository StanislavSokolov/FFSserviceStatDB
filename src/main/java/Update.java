import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import jdk.nashorn.internal.parser.Token;
import org.json.JSONArray;
import org.json.JSONObject;

public class Update extends Thread {

    private final String TOKENOZON1 = "token";
    private final String TOKENOZON2 = "token";

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

        products = new ArrayList<>();

        generetedURL = URLRequestResponse.generateURL(3, 3, "0");
        try {
            response = URLRequestResponse.getResponseFromURL(generetedURL, TOKENOZON1, TOKENOZON2, 3, "0", "0");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println(response);

        String answerString = "";

        JSONObject jsonObject5 = new JSONObject(response);
        for (int i = 0; i < jsonObject5.getJSONArray("result").length(); i++) {
            JSONArray jsonArray = (JSONArray) jsonObject5.getJSONArray("result").getJSONObject(i).get("products");
            JSONObject jsonObject6 = new JSONObject(String.valueOf(jsonObject5.getJSONArray("result").getJSONObject(i).get("analytics_data")));
            ItemShop itemShop = new ItemShop(jsonArray.getJSONObject(0).get("name").toString(),
                    jsonArray.getJSONObject(0).get("offer_id").toString(),
                    jsonArray.getJSONObject(0).get("price").toString(),
                    String.valueOf((int) (Float.parseFloat(jsonArray.getJSONObject(0).get("price").toString()) * 0.88)),
                    jsonObject6.getString("warehouse_name"),
                    jsonObject6.getString("region") + " (" + jsonObject6.getString("city") + ")",
                    jsonObject5.getJSONArray("result").getJSONObject(i).get("created_at").toString());
            answerString = answerString
                    + "\n"
                    + "Наименование: "
                    + jsonArray.getJSONObject(0).get("name").toString()
                    + "\n"
                    + "Артикул: "
                    + jsonArray.getJSONObject(0).get("offer_id").toString()
                    + "\n"
                    + "Количество: "
                    + jsonArray.getJSONObject(0).get("quantity").toString()
                    + "\n"
                    + "Цена: "
                    + jsonArray.getJSONObject(0).get("price").toString()
                    + "\n"
                    + "Склад отгрузки: "
                    + jsonObject6.getString("warehouse_name")
                    + "\n"
                    + "Регион доставки: "
                    + jsonObject6.getString("region") + " (" + jsonObject6.getString("city") + ")"
                    + "\n"
                    + "Дата: "
                    + jsonObject5.getJSONArray("result").getJSONObject(i).get("created_at").toString()
                    + "\n";

            products.add(itemShop);
        }

        System.out.println(answerString);

        if (!answerString.equals("")) {
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
            SQL.upDate(URLRequestResponse.getDataCurrent(), sumSale, sumOrder, sumSaleMoney, productPopular.getSubject() + " " + productPopular.getSupplierArticle(), "ozon");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } else {
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
            SQL.upDate(URLRequestResponse.getDataCurrent(), 0, 0, 0, "", "ozon");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
    }
}
