package com.example.asus.quessapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

    String answer[] = new String[4];
    int locationAnswer=0;
    int selectedCelebrity=0;

    ImageView imageView;

    Button button0;
    Button button1;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
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
            selectedCelebrity = random.nextInt(listNameArtis.size());

            DownloadImage downloadImage = new DownloadImage();

            Bitmap bitmap = downloadImage.execute(listImage.get(selectedCelebrity)).get();

            imageView.setImageBitmap(bitmap);

            locationAnswer = random.nextInt(4);

            int j=0;
            for(int i=0;i<4;i++){
                if(locationAnswer==i) {
                    answer[i]=listNameArtis.get(selectedCelebrity);
                }else {
                    while(j == selectedCelebrity){
                        j=random.nextInt(listNameArtis.size());
                    }
                    answer[i]=listNameArtis.get(random.nextInt(listNameArtis.size()));
                }

            }

            for(int i=0;i<4;i++){
                if(i==0){
                    button0.setText(answer[i]);
                } else if(i==1){
                    button1.setText(answer[i]);
                } else if(i==2){
                    button2.setText(answer[i]);
                } else {
                    button3.setText(answer[i]);
                }
            }

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
