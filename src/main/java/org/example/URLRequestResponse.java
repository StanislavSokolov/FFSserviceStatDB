package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class URLRequestResponse {

    public static URL generateURL(String shop, String method, String token) {
        String shopNumber = shop;
        String methodNumber = method;
        String dataAPI = null;
        String dataMethod = null;
        if (shopNumber.equals("wb")) {
            if (methodNumber.equals("stocks")){
                dataAPI = "https://statistics-api.wildberries.ru";
                dataMethod = "/api/v1/supplier/stocks?dateFrom=" + getDataCurrent() + "T00%3A00%3A00.000Z&key=" + token;
            }
            if (methodNumber.equals("sales")) {
                dataAPI = "https://statistics-api.wildberries.ru";
                dataMethod = "/api/v1/supplier/sales?dateFrom=" + getData(-7) + "T00%3A00%3A00.000Z&key=" + token;
            }
            if (methodNumber.equals("orders")) {
                dataAPI = "https://statistics-api.wildberries.ru";
                dataMethod = "/api/v1/supplier/orders?dateFrom=" + getData(-7) + "T00%3A00%3A00.000Z&key=" + token;
            }
            if (methodNumber.equals("info")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/info?quantity=0";
            }
            if (methodNumber.equals("prices")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/prices";
            }
            if (methodNumber.equals("updateDiscounts")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/updateDiscounts";
            }
            if (methodNumber.equals("updatePromocodes")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/updatePromocodes";
            }
            if (methodNumber.equals("getList")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/content/v1/cards/cursor/list";
            }
            if (methodNumber.equals("getCard")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/content/v1/cards/filter";
            }
        } else if (shopNumber.equals("ozon")) {
            dataAPI = "https://api-seller.ozon.ru";
            if (methodNumber.equals("list"))
                dataMethod = "/v2/product/list";
            if (methodNumber.equals("info"))
                dataMethod = "/v2/product/info";
            if (methodNumber.equals("fbo/list"))
                dataMethod = "/v2/posting/fbo/list";
            if (methodNumber.equals("import/prices"))
                dataMethod = "/v1/product/import/prices";
        }

        URL url = null;
        try {
            url = new URL(dataAPI + dataMethod);
            System.out.println(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getDataCurrent() {
        Date date = new Date();
        String str = date.toString();
        String[] subStr;
        String delimeter = " "; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода split()
        String day = subStr[2];
        String month = subStr[1];
        String year = subStr[5];
        String month1 = month;
        if (month.equals("Jan")) month1 = "01";
        else if (month.equals("Feb")) month1 = "02";
        else if (month.equals("Mar")) month1 = "03";
        else if (month.equals("Apr")) month1 = "04";
        else if (month.equals("May")) month1 = "05";
        else if (month.equals("Jun")) month1 = "06";
        else if (month.equals("Jul")) month1 = "07";
        else if (month.equals("Aug")) month1 = "08";
        else if (month.equals("Sep")) month1 = "09";
        else if (month.equals("Oct")) month1 = "10";
        else if (month.equals("Nov")) month1 = "11";
        else month1 = "12";
        return year + "-" + month1 + "-" + day;
    }

    public static String getData(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);
        String str = calendar.getTime().toString();
        String[] subStr;
        String delimeter = " "; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода split()
        String day = subStr[2];
        String month = subStr[1];
        String year = subStr[5];
        String month1 = month;
        if (month.equals("Jan")) month1 = "01";
        else if (month.equals("Feb")) month1 = "02";
        else if (month.equals("Mar")) month1 = "03";
        else if (month.equals("Apr")) month1 = "04";
        else if (month.equals("May")) month1 = "05";
        else if (month.equals("Jun")) month1 = "06";
        else if (month.equals("Jul")) month1 = "07";
        else if (month.equals("Aug")) month1 = "08";
        else if (month.equals("Sep")) month1 = "09";
        else if (month.equals("Oct")) month1 = "10";
        else if (month.equals("Nov")) month1 = "11";
        else month1 = "12";
        return year + "-" + month1 + "-" + day;
    }

    public static String getResponseFromURL(URL url, String token) throws IOException, URISyntaxException {

        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(url.toURI());
        final List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("accept", "application/json"));
//        params.add(new BasicNameValuePair("Authorization", token));
        httpGet.setHeader("accept", "application/json");
        httpGet.setHeader("Authorization", token);

        try (
                CloseableHttpResponse response2 = httpclient.execute(httpGet)
        ){
            final HttpEntity entity2 = response2.getEntity();
            String respond = EntityUtils.toString(entity2);
            return respond;
        }
    }

    public static String getResponseFromURL(URL url, String token, String supplierArticle) throws IOException {

        String reqBody = "";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", token);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
        reqBody = "{\"vendorCodes\":[\"" + supplierArticle + "\"],\"allowedCategoriesOnly\":true}";
        writer.write(reqBody);
        writer.close();

        System.out.println(reqBody);

        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return  null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }


    public static String getResponseFromURL(URL url, String token, String client, String method, String product_id, String price) throws IOException, URISyntaxException {

        String methodNumber = method;
        String reqBody = "";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Client-Id", client);
        httpURLConnection.setRequestProperty("Api-Key", token);
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
        if (methodNumber.equals("list")) reqBody = "{\"filter\":{\"visibility\": \"ALL\"},\"last_id\": \"\", \"limit\": 100}";
        if (methodNumber.equals("info")) reqBody = "{\"offer_id\": \"\",\"product_id\": " + product_id + ", \"sku\": 0}";
        if (methodNumber.equals("fbo/list")) reqBody = "{\"dir\": \"ASC\", \"filter\": {\"since\": \"" + getDataCurrent() + "T00:00:00.000Z\"}, \"limit\": 5, \"offset\": 0, \"translit\": true, \"with\": {\"analytics_data\": true, \"financial_data\": true}}";
        if (methodNumber.equals("import/prices")) reqBody = "{\"prices\": [{\"auto_action_enabled\": \"UNKNOWN\",\"min_price\": \"100\", \"offer_id\": \"\", \"old_price\": \"0\", \"price\": \"" + price + "\", \"product_id\": " + product_id + "}]}";
        writer.write(reqBody);
        writer.close();

        System.out.println(reqBody);

        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return  null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }
}