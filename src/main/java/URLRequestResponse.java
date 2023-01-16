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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class URLRequestResponse {

    public static URL generateURL(int shop, int method, String token) {
        int shopNumber = shop;
        int methodNumber = method;
        String dataAPI = null;
        String dataMethod = null;
        switch (shopNumber){
            case (2):
                dataAPI = "https://suppliers-api.wildberries.ru";
                switch (methodNumber){
                    case (1):
                        dataMethod = "/public/api/v1/info?quantity=0";
                        break;
                    case (2):
                        dataMethod = "/public/api/v1/prices";
                        break;
                    case (3):
                        dataMethod = "/public/api/v1/updateDiscounts";
                        break;
                    case (4):
                        dataMethod = "/public/api/v1/updatePromocodes";
                        break;
                    case (5):
                        dataAPI = "https://suppliers-stats.wildberries.ru";
                        dataMethod = "/api/v1/supplier/stocks?dateFrom=" + getDataCurrent() + "T00%3A00%3A00.000Z&key=" + token;
                        break;
                    case (6):
                        dataAPI = "https://suppliers-stats.wildberries.ru";
                        dataMethod = "/api/v1/supplier/sales?dateFrom=" + getDataCurrent() + "T00%3A00%3A00.000Z&key=" + token;
                        break;
                    case (7):
                        dataAPI = "https://suppliers-stats.wildberries.ru";
                        dataMethod = "/api/v1/supplier/orders?dateFrom=" + getDataCurrent() + "T00%3A00%3A00.000Z&key=" + token;
                        break;
                    default:
                        break;
                }
                break;
            case (1):
                dataAPI = "https://suppliers-api.wildberries.ru";
                switch (methodNumber){
                    case (1):
                        dataMethod = "/public/api/v1/info?quantity=0";
                        break;
                    case (2):
                        dataMethod = "/public/api/v1/prices";
                        break;
                    case (3):
                        dataMethod = "/public/api/v1/updateDiscounts";
                        break;
                    case (4):
                        dataMethod = "/public/api/v1/updatePromocodes";
                        break;
                    case (5):

                        break;
                    default:
                        break;
                }
                break;
            case (3):
                dataAPI = "https://api-seller.ozon.ru";
                switch (methodNumber){
                    case (1):
                        dataMethod = "/v2/product/list";
                        break;
                    case (2):
                        dataMethod = "/v2/product/info";
                        break;
                    case (3):
                        dataMethod = "/v2/posting/fbo/list";
                        break;
                    case (4):
                        dataMethod = "/v1/product/import/prices";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
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

    public static String getResponseFromURL(URL url, String token) throws IOException, URISyntaxException {

        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(url.toURI());
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accept", "application/json"));
        params.add(new BasicNameValuePair("Authorization", token));
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

    public static String getResponseFromURL(URL url) throws IOException, URISyntaxException {
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpUriRequest httpGet = new HttpGet(url.toURI());
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet))

        {
            final HttpEntity entity1 = response1.getEntity();
            String respond = EntityUtils.toString(entity1);
            System.out.println(respond);
            return respond;
        }
    }
}