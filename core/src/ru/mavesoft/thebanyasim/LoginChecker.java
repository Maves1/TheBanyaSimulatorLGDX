package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginChecker extends Thread {
    String login;
    boolean result;

    public LoginChecker(String login) {
        this.login = login;
    }

    public boolean loginExists() {
        run();
        return result;
    }

    @Override
    public void run() {
        String checkUrl = "http://f99640g0.beget.tech/logincheck.php?login=" + login;
        String res = "";
        try {
            URL currURL = new URL(checkUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) currURL.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int c = inputStreamReader.read();
            while (c != -1) {
                res += (char) c;
                c = inputStreamReader.read();
            }
            Gdx.app.log("LoginChecker", res);

            // res = res.substring(1, 2);

            if (res.equals("1")) {
                result = true;
            } else {
                result = false;
            }
        } catch (MalformedURLException ex) {
            Gdx.app.log("Exception!", "MalfURL");
        } catch (IOException ex) {
            Gdx.app.log("Exception!", "IO");
        }

    }
}
