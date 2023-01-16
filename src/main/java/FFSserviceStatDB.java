import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class FFSserviceStatDB {
    public static void main(String[] args) {
        SQL.createBD();
        Update update = new Update();
        update.start();
    }
}


