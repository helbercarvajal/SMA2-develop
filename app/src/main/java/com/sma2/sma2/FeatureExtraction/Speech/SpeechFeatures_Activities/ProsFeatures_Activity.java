
package com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.Speech_features_Activity;
import com.sma2.sma2.FeatureExtraction.Speech.features.ProsFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.array_manipulation;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProsFeatures_Activity extends AppCompatActivity implements View.OnClickListener {


    private TextView  tVrateDDK1,tVrateDDK2,tVrateDDK3,tMEnergyDDK1,tMEnergyDDK2,tMEnergyDDK3,  tmessage_prosody, tmessage_prosody2;
    private ImageView  iEmojinProsody, iEmojinProsody2;
    private Button bBack;
    private String path_pataka = null, path_pakata = null, path_petaka = null;
    private List<String> path_pataka_all = new ArrayList<>(), path_pakata_all = new ArrayList<>(), path_petaka_all = new ArrayList<>();
    private float  voiceRate, maxEnergy;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/AUDIO/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prosfeatures);
        bBack = findViewById(R.id.button_back_speech_ft);


        tVrateDDK1 = findViewById(R.id.tVrateDDK1);
        tMEnergyDDK1 = findViewById(R.id.tMEnergyDDK1);

        tVrateDDK2 = findViewById(R.id.tVrateDDK2);
        tMEnergyDDK2 = findViewById(R.id.tMEnergyDDK2);


        tVrateDDK3 = findViewById(R.id.tVrateDDK3);
        tMEnergyDDK3 = findViewById(R.id.tMEnergyDDK3);


        tmessage_prosody = findViewById(R.id.tmessage_prosody);
        tmessage_prosody2 = findViewById(R.id.tmessage_prosody2);



        iEmojinProsody = findViewById(R.id.iEmojin_prosody);
        iEmojinProsody2 = findViewById(R.id.iEmojin_prosody2);

        SetListeners();
        ProsodyFeatures();
    }




    private void SetListeners() {
        bBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_speech_ft:
                onButtonBack();
                break;

        }
    }

    private void onButtonBack() {
        Intent i = new Intent(ProsFeatures_Activity.this, Speech_features_Activity.class);
        startActivity(i);

    }



    private float VoiceRate(String AudioFile) {
        ProsFeatures ProsFeatures = new ProsFeatures();

        float vRate = ProsFeatures.voiceRate(AudioFile);

        // Voice rate healthy control ranges: 1.1 to 2
        //0 to 1.0 Regular
        //1.1 to 1.55 Good
        // 1.56 to 2 Excellent


        if (vRate >= 2) {
            // 2 or More than 2 percentage (%) given by the rule of three
            vRate = 100;
        } else {
            //Less than 2 gives 100%
            vRate = (100 * vRate) / 2;
        }


        return vRate;
    }


    private float MaxEnergy(String AudioFile) {

        ProsFeatures ProsFeatures = new ProsFeatures();

        //

        float energyM = ProsFeatures.maxEnergy(AudioFile);
        energyM=Math.abs(energyM);



        // Max Energy healthy control ranges: 14.3 to 17.9
        //0 to 14.2 Regular
        //14.3 to 16.1 Good
        // 16.2 to 17.9 Excellent


        if (energyM >= 17.9) {
            // 17.9 or More than 17.9 percentage (%) given by the rule of three
            energyM = 100;
        } else {
            //Less than 17.9 gives 100%
            energyM = (100 * energyM) / (float) 17.9;
        }

        return energyM;
    }




    private void ProsodyFeatures() {


        //VOICE RATE PART
        // get exercises from DDK: PATAKA to compute prosody features


        int IDEx = 11;
        String voiceRateStr="";
        WAVfileReader wavFileReader = new WAVfileReader();
        array_manipulation ArrM = new array_manipulation();

        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        long N = signalDataService.countSignalsbyname(name);

        if (N > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(name);
            if (signals.size() > 0) {
                path_pataka = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size() > 4) {
                    for (int i = signals.size() - 4; i < signals.size(); i++) {
                        path_pataka_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i = 0; i < signals.size(); i++) {
                        path_pataka_all.add(signals.get(i).getSignalPath());
                    }
                }
            }
        }

        // DDK2 PAKATA


        IDEx = 13;
        ArrM = new array_manipulation();

        GetEx = new GetExercises(this);
        name = GetEx.getNameExercise(IDEx);
        signalDataService = new SignalDataService(this);
        df = new DecimalFormat("#.00");
        N = signalDataService.countSignalsbyname(name);

        if (N > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(name);
            if (signals.size() > 0) {
                path_pakata = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size() > 4) {
                    for (int i = signals.size() - 4; i < signals.size(); i++) {
                        path_pakata_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i = 0; i < signals.size(); i++) {
                        path_pakata_all.add(signals.get(i).getSignalPath());
                    }
                }
            }
        }




        // DDK2 PETAKA


        IDEx = 12;
        ArrM = new array_manipulation();

        GetEx = new GetExercises(this);
        name = GetEx.getNameExercise(IDEx);
        signalDataService = new SignalDataService(this);
        df = new DecimalFormat("#.00");
        N = signalDataService.countSignalsbyname(name);

        if (N > 0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(name);
            if (signals.size() > 0) {
                path_petaka = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size() > 4) {
                    for (int i = signals.size() - 4; i < signals.size(); i++) {
                        path_petaka_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i = 0; i < signals.size(); i++) {
                        path_petaka_all.add(signals.get(i).getSignalPath());
                    }
                }
            }
        }








        //DDK2: PAKATA

            if (path_pakata == null) {
                tVrateDDK2.setText(R.string.Empty);

            } else {




                voiceRate = VoiceRate(path_pakata);
                voiceRateStr = String.valueOf(df.format(voiceRate)) + "%";
                tVrateDDK2.setText(String.valueOf(voiceRateStr));}


        //DDK3: PETAKA

            if (path_petaka == null) {
                tVrateDDK3.setText(R.string.Empty);

            } else {


                wavFileReader = new WAVfileReader();

                voiceRate = VoiceRate(path_petaka);
                voiceRateStr = String.valueOf(df.format(voiceRate)) + "%";
                tVrateDDK3.setText(String.valueOf(voiceRateStr));}
        //DDK1: PATAKA

        if (path_pataka == null) {
            tVrateDDK1.setText(R.string.Empty);

        } else {



            voiceRate = VoiceRate(path_pataka);
            voiceRateStr = String.valueOf(df.format(voiceRate)) + "%";
            tVrateDDK1.setText(String.valueOf(voiceRateStr));

            // Emoji for prosody features just for DDK1: PATAKA
//78
            if (voiceRate >= 70) {

                iEmojinProsody.setImageResource(R.drawable.happy_emojin);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                iEmojinProsody.startAnimation(animation);
                tmessage_prosody.setText(R.string.Positive_message);
                Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                tmessage_prosody.startAnimation(animation2);
                //55
            } else if (voiceRate >= 50) {

                iEmojinProsody.setImageResource(R.drawable.medium_emojin);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                iEmojinProsody.startAnimation(animation);
                tmessage_prosody.setText(R.string.Medium_message);
                Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                tmessage_prosody.startAnimation(animation2);
            } else {

                iEmojinProsody.setImageResource(R.drawable.sad_emoji);

                Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                iEmojinProsody.startAnimation(animation);
                tmessage_prosody.setText(R.string.Negative_message);
                Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                tmessage_prosody.startAnimation(animation2);
            }

            float VoiceRateTemp;




            GraphManager graphManager = new GraphManager(this);

            ArrayList<Float> vRate_listDDK1=new ArrayList<>();


            ArrayList<Integer> x = new ArrayList<>();
            ArrayList<Float> y = new ArrayList<>();
            for (int i = 0; i < 5; i++) {

                if (i < path_pataka_all.size()) {
                    VoiceRateTemp = VoiceRate(path_pataka_all.get(i));
                    vRate_listDDK1.add(VoiceRateTemp);


                } else {
                    vRate_listDDK1.add((float) 0);
                }

            }







            ArrayList<Float> vRate_listDDK2=new ArrayList<>();


            for (int i = 0; i < 5; i++) {

                if (i < path_pakata_all.size()) {
                    VoiceRateTemp = VoiceRate(path_pakata_all.get(i));
                    vRate_listDDK2.add(VoiceRateTemp);


                } else {
                    vRate_listDDK2.add((float) 0);
                }

            }

            ArrayList<Float> vRate_listDDK3=new ArrayList<>();


            for (int i = 0; i < 5; i++) {

                if (i < path_petaka_all.size()) {
                    VoiceRateTemp = VoiceRate(path_petaka_all.get(i));
                    vRate_listDDK3.add(VoiceRateTemp);



                } else {
                    vRate_listDDK3.add((float) 0);

                }

            }





            String Title = getResources().getString(R.string.voiceRate);
            String Ylabel = getResources().getString(R.string.voiceRate);
            String Xlabel = getResources().getString(R.string.session);
            GraphView graph = findViewById(R.id.bar_perc_prosody);
            //graphManager.BarGraph(graphPro, x, y, 100, 6.5, Title, Xlabel, Ylabel);



                DataPoint[] dp = new DataPoint[]{
                        new DataPoint(0,0),
                        new DataPoint(1,vRate_listDDK1.get(0)),
                        new DataPoint(2,vRate_listDDK2.get(0)),
                        new DataPoint(3,vRate_listDDK3.get(0)),
                        new DataPoint(4,0),
                        new DataPoint(5,vRate_listDDK1.get(1)),
                        new DataPoint(6,vRate_listDDK2.get(1)),
                        new DataPoint(7,vRate_listDDK3.get(1)),
                        new DataPoint(8,0),
                        new DataPoint(9,vRate_listDDK1.get(2)),
                        new DataPoint(10,vRate_listDDK2.get(2)),
                        new DataPoint(11,vRate_listDDK3.get(2)),
                        new DataPoint(12, 0),
                };

                float max_DDK1 = Math.max(vRate_listDDK1.get(0), Math.max(vRate_listDDK1.get(1), vRate_listDDK1.get(2)));
                float max_DDK2 = Math.max(vRate_listDDK2.get(0), Math.max(vRate_listDDK2.get(1), vRate_listDDK2.get(2)));
                float max_DDK3 = Math.max(vRate_listDDK3.get(0), Math.max(vRate_listDDK3.get(1), vRate_listDDK3.get(2)));

                float max_all = Math.max(max_DDK1, Math.max(max_DDK2, max_DDK3));

                int max_graph = Math.round(max_all + (float) 0.5);

                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dp);
                graph.addSeries(series);

                GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle(Xlabel);
                gridLabel.setVerticalAxisTitle(Ylabel);
                //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                staticLabelsFormatter.setHorizontalLabels(new String[] {" ","1", " ", "2", " ", "3", " "});
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(12);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(max_graph+1);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);

                series.setSpacing(5);

                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        if((data.getX()==1) || (data.getX()==5) || (data.getX()==9))
                            return (Color.rgb(153,99,0));
                        else if((data.getX()==2) || (data.getX()==6) || (data.getX()==10))
                            return (Color.rgb(255,190,0));
                        else
                            return (Color.rgb(255,140,0));
                    }

                });























            //MAX ENERGY PART

            //DDK2: PAKATA

            if (path_pakata == null) {
                tMEnergyDDK2.setText(R.string.Empty);

            } else {


                maxEnergy = MaxEnergy(path_pakata);
                Log.e("FF1", "Max" + String.valueOf(maxEnergy));
                String energyMStr = String.valueOf(df.format(maxEnergy)) + "%";
                tMEnergyDDK2.setText(String.valueOf(energyMStr));}


            //DDK3: PETAKA

            if (path_petaka == null) {
                tMEnergyDDK3.setText(R.string.Empty);

            } else {


                maxEnergy = MaxEnergy(path_petaka);
                Log.e("FF1", "Max" + String.valueOf(maxEnergy));
                String energyMStr = String.valueOf(df.format(maxEnergy)) + "%";
                tMEnergyDDK3.setText(String.valueOf(energyMStr));}


            // get exercises from DDK: PATAKA to compute prosody features


            if (path_pataka == null) {
                tMEnergyDDK1.setText(R.string.Empty);

            } else {


                wavFileReader = new WAVfileReader();

                maxEnergy = MaxEnergy(path_pataka);
                Log.e("FF1", "Max" + String.valueOf(maxEnergy));
                String energyMStr = String.valueOf(df.format(maxEnergy)) + "%";
                tMEnergyDDK1.setText(String.valueOf(energyMStr));
                //Log.e("F0", String.valueOf(energyPerturbation));

                // Emoji for prosody features

                if (maxEnergy >= 60) {

                    iEmojinProsody2.setImageResource(R.drawable.happy_emojin);

                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                    iEmojinProsody2.startAnimation(animation);
                    tmessage_prosody2.setText(R.string.Positive_message);
                    Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                    tmessage_prosody2.startAnimation(animation2);
                } else if (maxEnergy >= 40) {

                    iEmojinProsody2.setImageResource(R.drawable.medium_emojin);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                    iEmojinProsody2.startAnimation(animation);
                    tmessage_prosody2.setText(R.string.Medium_message);
                    Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                    tmessage_prosody2.startAnimation(animation2);
                } else {

                    iEmojinProsody2.setImageResource(R.drawable.sad_emoji);

                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
                    iEmojinProsody2.startAnimation(animation);
                    tmessage_prosody2.setText(R.string.Negative_message);
                    Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
                    tmessage_prosody2.startAnimation(animation2);
                }




                float EnergyMTemp;


                graphManager = new GraphManager(this);



                Title = getResources().getString(R.string.MaxEnergy);
                Ylabel = getResources().getString(R.string.MaxEnergy);
                Xlabel = getResources().getString(R.string.session);
                graph = findViewById(R.id.bar_perc_prosody2);





                ArrayList<Float> mEnergy_listDDK1=new ArrayList<>();


                for (int i = 0; i < 5; i++) {

                    if (i < path_pataka_all.size()) {
                        EnergyMTemp = MaxEnergy(path_pataka_all.get(i));
                        mEnergy_listDDK1.add(EnergyMTemp);


                    } else {
                        mEnergy_listDDK1.add((float) 0);
                    }

                }







                ArrayList<Float> mEnergy_listDDK2=new ArrayList<>();


                for (int i = 0; i < 5; i++) {

                    if (i < path_pakata_all.size()) {
                        EnergyMTemp = MaxEnergy(path_pakata_all.get(i));
                        mEnergy_listDDK2.add(EnergyMTemp);


                    } else {
                        mEnergy_listDDK2.add((float) 0);
                    }

                }

                ArrayList<Float> mEnergy_listDDK3=new ArrayList<>();


                for (int i = 0; i < 5; i++) {

                    if (i < path_petaka_all.size()) {
                        EnergyMTemp = MaxEnergy(path_petaka_all.get(i));
                        mEnergy_listDDK3.add(EnergyMTemp);



                    } else {
                        mEnergy_listDDK3.add((float) 0);

                    }

                }








                dp = new DataPoint[]{
                        new DataPoint(0,0),
                        new DataPoint(1,mEnergy_listDDK1.get(0)),
                        new DataPoint(2,mEnergy_listDDK2.get(0)),
                        new DataPoint(3,mEnergy_listDDK3.get(0)),
                        new DataPoint(4,0),
                        new DataPoint(5,mEnergy_listDDK1.get(1)),
                        new DataPoint(6,mEnergy_listDDK2.get(1)),
                        new DataPoint(7,mEnergy_listDDK3.get(1)),
                        new DataPoint(8,0),
                        new DataPoint(9,mEnergy_listDDK1.get(2)),
                        new DataPoint(10,mEnergy_listDDK2.get(2)),
                        new DataPoint(11,mEnergy_listDDK3.get(2)),
                        new DataPoint(12, 0),
                };

                max_DDK1 = Math.max(mEnergy_listDDK1.get(0), Math.max(mEnergy_listDDK1.get(1), mEnergy_listDDK1.get(2)));
                max_DDK2 = Math.max(mEnergy_listDDK2.get(0), Math.max(mEnergy_listDDK2.get(1), mEnergy_listDDK2.get(2)));
                max_DDK3 = Math.max(mEnergy_listDDK3.get(0), Math.max(mEnergy_listDDK3.get(1), mEnergy_listDDK3.get(2)));

                max_all = Math.max(max_DDK1, Math.max(max_DDK2, max_DDK3));

                max_graph = Math.round(max_all + (float) 0.5);

                series = new BarGraphSeries<>(dp);
                graph.addSeries(series);

                gridLabel = graph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle(Xlabel);
                gridLabel.setVerticalAxisTitle(Ylabel);
                //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

                staticLabelsFormatter = new StaticLabelsFormatter(graph);
                staticLabelsFormatter.setHorizontalLabels(new String[] {" ","1", " ", "2", " ", "3", " "});
                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(12);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(max_graph+1);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);

                series.setSpacing(5);

                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        if((data.getX()==1) || (data.getX()==5) || (data.getX()==9))
                            return (Color.rgb(153,99,0));
                        else if((data.getX()==2) || (data.getX()==6) || (data.getX()==10))
                            return (Color.rgb(255,190,0));
                        else
                            return (Color.rgb(255,140,0));
                    }

                });













            }


        }


    }


}
