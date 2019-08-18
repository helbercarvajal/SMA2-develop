package com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sma2.sma2.FeatureExtraction.Speech.features.PhonFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ArtFeatures_Activity extends AppCompatActivity implements View.OnClickListener{

    private TextView  tddk_reg,  tmessage_articulation;
    private ImageView  iEmojinArticulation;
    private Button bBack;
    private String path_pataka = null;
    private List<String> path_pataka_all = new ArrayList<>();
    private float  DDK_reg;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/AUDIO/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artfeatures);

        bBack = findViewById(R.id.button_back_speech_Art);

        tddk_reg = findViewById(R.id.tddk_reg);

        tmessage_articulation = findViewById(R.id.tmessage_articulation);

        iEmojinArticulation = findViewById(R.id.iEmojin_articulation);

        SetListeners();
        ArticulationFeatures();
    }

    private void SetListeners() {
        bBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_speech_Art:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack() {
        Intent i = new Intent(ArtFeatures_Activity.this, Speech_features_Activity.class);
        startActivity(i);

    }

    private float DDKRegularity(String AudioFile) {
        WAVfileReader wavFileReader = new WAVfileReader();
        f0detector F0Detector = new f0detector();
        PhonFeatures PhonFeatures = new PhonFeatures();

        int[] InfoSig = wavFileReader.getdatainfo(AudioFile);
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

            return PhonFeatures.calculateSD(Duration);
        }

    }



    private void ArticulationFeatures() {
        int IDEx = 11;
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
            tddk_reg.setText(R.string.Empty);
        } else {
            DDK_reg = DDKRegularity(path_pataka);
            String DDK_regStr = String.valueOf(df.format(DDK_reg)) + "ms";
            tddk_reg.setText(DDK_regStr);
        }

        // Emoji for articulation features

        if (DDK_reg <= 200) {
            iEmojinArticulation.setImageResource(R.drawable.happy_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinArticulation.startAnimation(animation);
            tmessage_articulation.setText(R.string.Positive_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_articulation.startAnimation(animation2);
        } else if (DDK_reg <= 400) {
            iEmojinArticulation.setImageResource(R.drawable.medium_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinArticulation.startAnimation(animation);
            tmessage_articulation.setText(R.string.Medium_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_articulation.startAnimation(animation2);
        } else {
            iEmojinArticulation.setImageResource(R.drawable.sad_emoji);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinArticulation.startAnimation(animation);
            tmessage_articulation.setText(R.string.Negative_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_articulation.startAnimation(animation2);
        }


        float DDK_regTemp;


        GraphManager graphManager = new GraphManager(this);


        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            if (i < path_pataka_all.size()) {
                DDK_regTemp = DDKRegularity(path_pataka_all.get(i));
                x.add(i + 1);
                y.add(DDK_regTemp);
            } else {
                x.add(i + 1);
                y.add((float) 0);
            }

        }

        String Title = getResources().getString(R.string.ddk_reg);
        String Ylabel = getResources().getString(R.string.ddk_reg);
        String Xlabel = getResources().getString(R.string.session);
        GraphView graph = findViewById(R.id.bar_perc_articulation);
        graphManager.BarGraph(graph, x, y, 0, 5, Title, Xlabel, Ylabel);

    }

}
