package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

import android.graphics.Color;

public class WalkingFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movement = null;
    List<String> path_movement_all = new ArrayList<>();
    TextView tWalking;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_feature_);
        bBack = findViewById(R.id.button_back6);
        bBack.setOnClickListener(this);
        tWalking = findViewById(R.id.tTremorWalking);
        SignalDataService signalDataService = new SignalDataService(this);
        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");

        int IDEx = 31;   // Gait 4x10
        GetExercises GetEx = new GetExercises(this);

        String name = GetEx.getNameExercise(IDEx);
        double Tremor = 0;
        List<SignalDA> Signals = signalDataService.getSignalsbyname(name);
        if (Signals.size()>0){
            path_movement = PATH + Signals.get(Signals.size()-1).getSignalPath();

            if (Signals.size() > 4){
                for (int i=Signals.size()-4;i<Signals.size();i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }
            else{
                for (int i = 0; i < Signals.size(); i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }

        }

        if(path_movement == null){
            tWalking.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");
            Tremor = MovementProcessor.ComputeTremor(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tWalking.setText(String.valueOf(df.format(Tremor)));
            System.out.println(TremorSignalaX.Signal.size());
        }

        GraphManager graphManager = new GraphManager(this);
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();

        for (int i = 0; i < 5; i++){

            if (i < path_movement_all.size()){
                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all.get(i), "aZ [m/s^2]");
                Tremor = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
                x.add(i+1);
                y.add((float)Tremor);
            }
            else{
                x.add(i+1);
                y.add((float) 0);
            }
        }

        String Title = getResources().getString(R.string.TremorAmplitude);
        String Ylabel = getResources().getString(R.string.TremorAmplitude);
        String Xlabel = getResources().getString(R.string.session);
        GraphView graph = findViewById(R.id.bar_TremorGait);
        graphManager.BarGraph(graph, x, y, 0, 5, Title, Xlabel, Ylabel);

        // Plot accelerometer gait signal
        GraphView graph2 = findViewById(R.id.plotWalkingAccelerometer);
        Title = getResources().getString(R.string.F0);
        Xlabel = getResources().getString(R.string.time);
        //Ylabel = getResources().getString(R.string.frequency);
        Ylabel = "acceleration [g]";
        graphManager.LineGraph(graph2, x, y, 0, 10, Title, Xlabel, Ylabel);


        // Plot of gyroscope gait signal
        GraphView graph3 = findViewById(R.id.plotWalkingGyroscope);
        Title = getResources().getString(R.string.F0);
        Xlabel = getResources().getString(R.string.time);
        //Ylabel = getResources().getString(R.string.frequency);
        Ylabel = "degrees";
        graphManager.LineGraph(graph3, x, y, 0, 10, Title, Xlabel, Ylabel);

        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);
        radarchart.getDescription().setEnabled(false);
        radarchart.animateXY(5000, 5000, Easing.EaseInOutQuad);

        RadarData radardata= new RadarData();
        float[] datos1={100f,20f,90f,80f}; // datos que se van a graficar
        float[] datos2={70f,70f,30f,90f};
        double area1=0.5*(datos1[0]+datos1[2])*(datos1[1]+datos1[3]);
        double area2=0.5*(datos2[0]+datos2[2])*(datos2[1]+datos2[3]);
        radardata=setdata(datos1,datos2);
        String[] labels={"Tremor","Max. Aceleration", "Max. Velocity", "Energy"};

        XAxis xAxis=radarchart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(90f);

        YAxis yAxis = radarchart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);

        Legend l = radarchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);
        l.setTextSize(20f);

        radarchart.setExtraOffsets(0,-400,0,-400);
    //radarchart.setBackgroundColor(Color.WHITE);
        radarchart.setScaleY(1f);
        radarchart.setScaleX(1f);
        radarchart.setData(radardata);
        radarchart.invalidate(); // refresh

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back6:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(WalkingFeatureActivity.this, MainActivityMenu.class);
        startActivity(i);

    }

    private RadarData setdata(float[] datos1, float[] datos2) {
        int cnt = datos1.length;
        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            //float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(datos1[i]));

            //float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(datos2[i]));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Below Session");
        set1.setColor(Color.rgb(255, 185, 0));
        set1.setFillColor(Color.rgb(255, 185, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(2f);
        set1.setValueTextColor(Color.rgb(255, 185, 0));
        set1.setValueTextSize(15f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "Current Session");
        set2.setColor(Color.rgb(0, 200, 200));
        set2.setFillColor(Color.rgb(0, 200, 200));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        set2.setValueTextColor(Color.rgb(0, 200, 200));
        set2.setValueTextSize(15f);

        RadarData dataradar= new RadarData();

        dataradar.addDataSet(set1);
        dataradar.addDataSet(set2);
        return dataradar;
    }

}
