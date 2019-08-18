
package com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities;

import android.content.Intent;
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


    private TextView tjitter, tmessage_phonation, tddk_reg, tddk_reg2, tddk_reg3, tmessage_articulation, tmessage_prosody, tmessage_prosody2;
    private ImageView iEmojinPhonation, iEmojinArticulation, iEmojinProsody, iEmojinProsody2;
    private Button bBack;
    private String path_pataka = null;
    private List<String> path_pataka_all = new ArrayList<>();
    private float  voiceRate, maxEnergy;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/AUDIO/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prosfeatures);
        bBack = findViewById(R.id.button_back_speech_ft);


        tddk_reg2 = findViewById(R.id.tddk_reg2);
        tddk_reg3 = findViewById(R.id.tddk_reg3);


        tmessage_phonation = findViewById(R.id.tmessage_phonation);
        tmessage_articulation = findViewById(R.id.tmessage_articulation);
        tmessage_prosody = findViewById(R.id.tmessage_prosody);
        tmessage_prosody2 = findViewById(R.id.tmessage_prosody2);




        iEmojinPhonation = findViewById(R.id.iEmojin_phonation);
        iEmojinArticulation = findViewById(R.id.iEmojin_articulation);
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


        if (path_pataka == null) {
            tddk_reg2.setText(R.string.Empty);

        } else {


            WAVfileReader wavFileReader = new WAVfileReader();

            voiceRate = VoiceRate(path_pataka);
            String voiceRateStr = String.valueOf(df.format(voiceRate)) + "%";
            tddk_reg2.setText(String.valueOf(voiceRateStr));


            // Emoji for phonation features
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


            ArrayList<Integer> x = new ArrayList<>();
            ArrayList<Float> y = new ArrayList<>();
            for (int i = 0; i < 5; i++) {

                if (i < path_pataka_all.size()) {
                    VoiceRateTemp = VoiceRate(path_pataka_all.get(i));
                    x.add(i + 1);
                    y.add(VoiceRateTemp);


                } else {
                    x.add(i + 1);
                    y.add((float) 0);
                }

            }

            String Title = getResources().getString(R.string.voiceRate);
            String Ylabel = getResources().getString(R.string.voiceRate);
            String Xlabel = getResources().getString(R.string.session);
            GraphView graphPro = findViewById(R.id.bar_perc_prosody);
            graphManager.BarGraph(graphPro, x, y, 100, 6.5, Title, Xlabel, Ylabel);


            //MAX ENERGY PART
            // get exercises from DDK: PATAKA to compute prosody features


            if (path_pataka == null) {
                tddk_reg3.setText(R.string.Empty);

            } else {


                wavFileReader = new WAVfileReader();

                maxEnergy = MaxEnergy(path_pataka);
                Log.e("FF1", "Max" + String.valueOf(maxEnergy));
                String energyMStr = String.valueOf(df.format(maxEnergy)) + "%";
                tddk_reg3.setText(String.valueOf(energyMStr));
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


                x = new ArrayList<>();
                y = new ArrayList<>();
                for (int i = 0; i < 5; i++) {

                    if (i < path_pataka_all.size()) {
                        EnergyMTemp = MaxEnergy(path_pataka_all.get(i));
                        x.add(i + 1);
                        y.add(EnergyMTemp);


                    } else {
                        x.add(i + 1);
                        y.add((float) 0);
                    }

                }

                Title = getResources().getString(R.string.MaxEnergy);
                Ylabel = getResources().getString(R.string.MaxEnergy);
                Xlabel = getResources().getString(R.string.session);
                GraphView graphPro2 = findViewById(R.id.bar_perc_prosody2);
                graphManager.BarGraph(graphPro2, x, y, 100, 6.5, Title, Xlabel, Ylabel);


            }


        }


    }


}
