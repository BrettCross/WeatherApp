package com.brettcross.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    Button btnForecast;
    TextView tvForecastList;
    RequestQueue requestQueue;

    String apiKey = "e74617f54e010f26e0c7305dddf44426";
    String baseUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
    String unit = "&units=metric";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.etCity = (EditText) findViewById(R.id.et_city);
        this.btnForecast = (Button) findViewById(R.id.btn_get_forecast);
        this.tvForecastList = (TextView) findViewById(R.id.tv_forecast);
        this.tvForecastList.setMovementMethod(new ScrollingMovementMethod());

        requestQueue = Volley.newRequestQueue(this);
    }

    private void clearForecastList() {
        this.tvForecastList.setText("");
    }

    private void setForecastListText(String str) {
        this.tvForecastList.setText(str);

    }

    private void getForecastList(String city) {
        this.url = this.baseUrl + city + this.unit + "&APPID=" + apiKey;

        JsonObjectRequest objectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() > 0) {
                            try {
                                String cityName = response.getString("name");
                                String weather = response.getJSONArray("weather").getJSONObject(0).getString("main");
                                int temp = response.getJSONObject("main").getInt("temp");
                                int loTemp = response.getJSONObject("main").getInt("temp_min");
                                int hiTemp = response.getJSONObject("main").getInt("temp_max");
                                setForecastListText(cityName+"\n"+weather+"\n"+temp);
                            } catch (JSONException e) {
                                Log.e("Volley", "Invalid JSON object");
                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setForecastListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                });

        requestQueue.add(objectRequest);
    }

    public void getForecastClicked(View v) {
        clearForecastList();
        getForecastList(etCity.getText().toString());
    }
}
