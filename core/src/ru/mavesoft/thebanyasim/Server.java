package ru.mavesoft.thebanyasim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server extends Thread {
    final static int TYPE_LOGIN = 0;
    final static int TYPE_REGISTRATION = 1;

    Preferences gamePreferences;

    String URL = "http://f99640g0.beget.tech/index.php/";

    int id;
    String login;
    String password;
    int requestType;

    public Server(Preferences preferences) {
        gamePreferences = Gdx.app.getPreferences("GamePreferences");
    }

    public Server(int requestType, String login, String password) {
        this.login = login;
        this.password = password;
        this.requestType = requestType;
        gamePreferences = Gdx.app.getPreferences("GamePreferences");
    }

    public String[] login() {
        String[] data = new String[4];

        if (requestType == Server.TYPE_LOGIN) {
            run();
        }

        return data;
    }

    @Override
    public void run() {
        try {
            if (requestType == Server.TYPE_LOGIN) {

                URL = "http://f99640g0.beget.tech/login.php?login=" + login + "&password=" + password;
                URL currURL = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) currURL.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                String res = "";
                int c = inputStreamReader.read();
                while (c != -1) {
                    res += (char) c;
                    c = inputStreamReader.read();
                }

                Gdx.app.log("Response", res);

            } else if (requestType == Server.TYPE_REGISTRATION) {
                URL = "http://f99640g0.beget.tech/index.php/";
                URL currURL = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) currURL.openConnection();
                httpURLConnection.setRequestMethod("POST");
                // httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
                httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                String urlParameters = "login=" + login + "&password=" + password + "&woodAmount="
                        + gamePreferences.getInteger("banyaWoodAmount")
                        + "&waterAmount=" + gamePreferences.getInteger("banyaWaterAmount")
                        + "&besomsAmount=" + gamePreferences.getInteger("banyaBesomsAmount");

                // Send post request
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = httpURLConnection.getResponseCode();

                String res = "";
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int c = reader.read();
                while (c != -1) {
                    res += (char) c;
                    c = reader.read();
                }
                Gdx.app.log("Response", res);
                res = res.substring(1, res.length() - 1);
                id = Integer.parseInt(res);
                gamePreferences.putInteger("userId", id);
                gamePreferences.flush();
            }
        } catch (MalformedURLException ex) {
            Gdx.app.log("Exception!", "MalfURL");
        } catch (IOException ex) {
            Gdx.app.log("Exception!", "IO");
        }
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
