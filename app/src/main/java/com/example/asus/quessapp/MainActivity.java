package com.example.asus.quessapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    List<String> listImage = new ArrayList<String>();
    List<String> listNameArtis = new ArrayList<String>();
    int selectedCelebrity=0;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        DownloadTask downloadTask = new DownloadTask();
        String result ;

        try {
            result = downloadTask.execute("http://www.posh24.se/kandisar").get();

            String split[] = result.split("<div class=\"listedArticles\">");

            Pattern pattern = Pattern.compile("img src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(split[0]);

            while (matcher.find()) {
                listImage.add(matcher.group(1));
            }

            pattern = Pattern.compile("alt=\"(.*?)\"");
            matcher = pattern.matcher(split[0]);

            while (matcher.find()) {
                listNameArtis.add(matcher.group(1));
            }

            Random random = new Random();
            selectedCelebrity = random.nextInt(listNameArtis.size()+1);

            DownloadImage downloadImage = new DownloadImage();

            Bitmap bitmap = downloadImage.execute(listImage.get(selectedCelebrity)).get();

            imageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return  myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStream.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
