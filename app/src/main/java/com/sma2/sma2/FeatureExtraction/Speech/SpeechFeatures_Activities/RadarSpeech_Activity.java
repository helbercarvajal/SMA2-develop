package com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.Speech.Speech_features_Activity;
import com.sma2.sma2.FeatureExtraction.Speech.features.PhonFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.features.ProsFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.R;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RadarSpeech_Activity extends AppCompatActivity implements View.OnClickListener {

    private Button bBack;
    private String path_ah = null, path_pataka = null;
    private List<String> path_pataka_all = new ArrayList<>(), path_pakata_all = new ArrayList<>(), path_petaka_all = new ArrayList<>();
    private List<Float> vRatePer = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_speech);
        bBack = findViewById(R.id.button_back_feat);

        SetListeners();

        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);
        radarchart.getDescription().setEnabled(false);
        radarchart.animateXY(5000, 5000, Easing.EaseInOutQuad);


        float shimmer = Shimmer();
        float jitter = Jitter();
        float ddkRegularity=DDKRegularity();
        float voicerate = VoiceRate();




        float[] datos1={(100f-shimmer),(100f-jitter),ddkRegularity,voicerate}; // data to graph
        float[] datos2={(float) 96.405,(float) 84.695,(float) 94.28,(float) 51.04};

        RadarData radardata=setdata(datos1,datos2);
        String[] labels={"Volumen","Monotonicidad", "Reg. Articulacion", "Vel. Articulatoria"};

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

    private void SetListeners() {
        bBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_feat:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack() {
        Intent i = new Intent(RadarSpeech_Activity.this, Speech_features_Activity.class);
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

        RadarDataSet set1 = new RadarDataSet(entries1, "Patients");
        set1.setColor(Color.rgb(255, 185, 0));
        set1.setFillColor(Color.rgb(255, 185, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(2f);
        set1.setValueTextColor(Color.rgb(255, 185, 0));
        set1.setValueTextSize(15f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "Controls");
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

    private float Shimmer(){
        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        GetExercises GetEx = new GetExercises(this);

        //Vowel A
        int IDEx = 18;
        String name_A = GetEx.getNameExercise(IDEx);
        long N_A = signalDataService.countSignalsbyname(name_A);

        if (N_A > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(name_A);
            if (signals.size() > 0) {
                path_ah = signals.get(signals.size() - 1).getSignalPath();
            }
        }

        if (path_ah == null)
            return 0;
        else {
            WAVfileReader wavFileReader=new WAVfileReader();
            f0detector F0Detector =new f0detector();
            PhonFeatures PhonFeatures=new PhonFeatures();

            int[]  InfoSig= wavFileReader.getdatainfo(path_ah);
            float[] SignalAh=wavFileReader.readWAV(InfoSig[0]);
            //Normalize signal
            sigproc SigProc = new sigproc();
            SignalAh = SigProc.normsig(SignalAh);
            float[] F0= F0Detector.sig_f0_autocorrelate(SignalAh, InfoSig[1]);

            List<float[]> Amp = SigProc.sigframe(SignalAh, InfoSig[1],(float) 0.04,(float) 0.03);

            return PhonFeatures.shimmer(F0, Amp);
        }
    }

    private float Jitter() {
        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        GetExercises GetEx = new GetExercises(this);

        //Vowel A
        int IDEx = 18;
        String name_A = GetEx.getNameExercise(IDEx);
        long N_A = signalDataService.countSignalsbyname(name_A);

        if (N_A > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(name_A);
            if (signals.size() > 0) {
                path_ah = signals.get(signals.size() - 1).getSignalPath();
            }
        }

        if (path_ah == null)
            return 0;
        else {
            WAVfileReader wavFileReader=new WAVfileReader();
            f0detector F0Detector =new f0detector();
            PhonFeatures PhonFeatures=new PhonFeatures();

            int[]  InfoSig= wavFileReader.getdatainfo(path_ah);
            float[] SignalAh=wavFileReader.readWAV(InfoSig[0]);
            //Normalize signal
            sigproc SigProc = new sigproc();
            SignalAh = SigProc.normsig(SignalAh);
            float[] F0= F0Detector.sig_f0_autocorrelate(SignalAh, InfoSig[1]);
            return PhonFeatures.jitter(F0);
        }
    }

    private float DDKRegularity() {
        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        GetExercises GetEx = new GetExercises(this);

        //VOICE RATE PART
        // get exercises from DDK: PATAKA to compute prosody features
        int IDEx = 11;
        String pataka = GetEx.getNameExercise(IDEx);
        long N_pataka = signalDataService.countSignalsbyname(pataka);

        if (N_pataka > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(pataka);
            if (signals.size() > 0) {
                path_pataka = signals.get(signals.size() - 1).getSignalPath();
            }
        }

        if (path_pataka == null)
            return 0;
        else {

            WAVfileReader wavFileReader = new WAVfileReader();
            f0detector F0Detector = new f0detector();
            PhonFeatures PhonFeatures = new PhonFeatures();

            int[] InfoSig = wavFileReader.getdatainfo(path_pataka);
            float[] Signal = wavFileReader.readWAV(InfoSig[0]);
            sigproc SigProc = new sigproc();
            Signal = SigProc.normsig(Signal);
            float[] F0 = F0Detector.sig_f0(Signal, InfoSig[1]);
            float sumF0 = 0;
            for (int i = 0; i < F0.length; i++) {
                sumF0 += F0[i];
            }
            if (sumF0 == 0) {
                return 0;
            } else {
                List<float[]> Voiced = F0Detector.voiced(F0, Signal);

                List<Float> Duration = new ArrayList<>();
                float[] segment;
                for (int i = 0; i < Voiced.size(); i++) {
                    segment = Voiced.get(i);
                    Duration.add(((float) segment.length * 1000) / InfoSig[0]);
                }


                float DKR = PhonFeatures.calculateSD(Duration);

                // DDKRegularity healthy control ranges: 0 to 313 ms (SD)
                // A good answer from the task is when is less than 313


                if (DKR <= 200) {

                    DKR = 0;
                } else {
                    DKR = (100 * (DKR - 200)) / 200;
                }


                return 100 - DKR;
            }
        }
    }

    private float VoiceRate() {
        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        GetExercises GetEx = new GetExercises(this);

        //VOICE RATE PART
        // get exercises from DDK: PATAKA to compute prosody features
        int IDEx = 11;
        String pataka = GetEx.getNameExercise(IDEx);
        long N_pataka = signalDataService.countSignalsbyname(pataka);

        if (N_pataka > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(pataka);
            if (signals.size() > 0) {
                path_pataka = signals.get(signals.size() - 1).getSignalPath();
            }
        }

        if (path_pataka == null)
            return 0;
        else {
            ProsFeatures ProsFeatures = new ProsFeatures();
            float vRate = ProsFeatures.voiceRate(path_pataka);
            if (vRate >= 2) {
                // 2 or More than 2 percentage (%) given by the rule of three
                vRate = 100;
            } else {
                //Less than 2 gives 100%
                vRate = (100 * vRate) / 2;
            }
            return vRate;
        }
    }




}
