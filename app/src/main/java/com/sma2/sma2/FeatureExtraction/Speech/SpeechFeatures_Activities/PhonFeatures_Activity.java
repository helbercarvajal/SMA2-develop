package com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities;

import android.content.Intent;
import android.graphics.Color;
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
import com.sma2.sma2.FeatureExtraction.Speech.features.PhonFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PhonFeatures_Activity extends AppCompatActivity  implements View.OnClickListener {

    private TextView tjitterA,tjitterI,tjitterU,tshimmerA,tshimmerI,tshimmerU,tmessage_phonation, tmessage_shimmer;
    private ImageView iEmojinPhonation, iEmojinShimmer;
    private Button bBack;
    private String path_ah = null;
    private List<String> path_ah_all= new ArrayList<>();
    private String path_i = null;
    private List<String> path_i_all= new ArrayList<>();
    private String path_u = null;
    private List<String> path_u_all= new ArrayList<>();

    private float jitterA, jitterI, jitterU, shimmerA, shimmerI, shimmerU;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/AUDIO/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonfeatures);

        bBack = findViewById(R.id.button_back_speech_Phon);

        tjitterA=findViewById(R.id.tjitterA);
        tjitterI=findViewById(R.id.tjitterI);
        tjitterU=findViewById(R.id.tjitterU);
        tshimmerA=findViewById(R.id.tshimmerA);
        tshimmerI=findViewById(R.id.tshimmerI);
        tshimmerU=findViewById(R.id.tshimmerU);

        tmessage_phonation=findViewById(R.id.tmessage_phonation);
        tmessage_shimmer=findViewById(R.id.tmessage_shimmer);


        iEmojinPhonation=findViewById(R.id.iEmojin_phonation);
        iEmojinShimmer=findViewById(R.id.iEmojin_shimmer);

        SetListeners();
        PhonationFeatures();
    }


    private void SetListeners() {
        bBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_speech_Phon:
                onButtonBack();
                break;

        }
    }

    private void onButtonBack() {
        Intent i = new Intent(PhonFeatures_Activity.this, Speech_features_Activity.class);
        startActivity(i);

    }

    private float Jitter(String AudioFile){
        WAVfileReader wavFileReader=new WAVfileReader();
        f0detector F0Detector =new f0detector();
        PhonFeatures PhonFeatures=new PhonFeatures();

        int[]  InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] SignalAh=wavFileReader.readWAV(InfoSig[0]);
        //Normalize signal
        sigproc SigProc = new sigproc();
        SignalAh = SigProc.normsig(SignalAh);
        float[] F0= F0Detector.sig_f0(SignalAh, InfoSig[1]);
        return PhonFeatures.jitter(F0);
    }

    private float Shimmer(String AudioFile){
        WAVfileReader wavFileReader=new WAVfileReader();
        f0detector F0Detector =new f0detector();
        PhonFeatures PhonFeatures=new PhonFeatures();

        int[]  InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] SignalAh=wavFileReader.readWAV(InfoSig[0]);
        //Normalize signal
        sigproc SigProc = new sigproc();
        SignalAh = SigProc.normsig(SignalAh);
        float[] F0= F0Detector.sig_f0(SignalAh, InfoSig[1]);

        List<float[]> Amp = SigProc.sigframe(SignalAh, InfoSig[1],(float) 0.04,(float) 0.03);

        return PhonFeatures.shimmer(F0, Amp);
    }


    private void PhonationFeatures() {

        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        GetExercises GetEx=new GetExercises(this);

        //Vowel A
        int IDEx=18;
        String name_A=GetEx.getNameExercise(IDEx);
        long N_A = signalDataService.countSignalsbyname(name_A);

        if (N_A>0) {

            List<SignalDA> signals = signalDataService.getSignalsbyname(name_A);
            if (signals.size() > 0) {
                path_ah = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size()>3){
                    for (int i=signals.size()-3;i<signals.size();i++){
                        path_ah_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i=0;i<signals.size();i++){
                        path_ah_all.add(signals.get(i).getSignalPath());
                    }
                }
            }
        }

        float JitterTemp, ShimmerTemp;
        ArrayList<Float> jitter_listA=new ArrayList<>();
        ArrayList<Float> shimmer_listA=new ArrayList<>();
        for (int i=0;i<3;i++){
            if (i<path_ah_all.size()){

                // Jitter sustained vowel ah
                JitterTemp = Jitter(path_ah_all.get(i));
                jitter_listA.add(JitterTemp);

                // Shimmer sustained vowel ah
                ShimmerTemp = Shimmer(path_ah_all.get(i));
                shimmer_listA.add(ShimmerTemp);

                // Show phonation features for the last speech task from sustained vowel ah
                if (path_ah.equals(path_ah_all.get(i))){
                    jitterA = JitterTemp;
                    String JitterStr = String.valueOf(df.format(jitterA)) + "%";
                    tjitterA.setText(JitterStr);

                    shimmerA = ShimmerTemp;
                    String ShimmerStr = String.valueOf(df.format(shimmerA)) + "%";
                    tshimmerA.setText(ShimmerStr);
                }
            }
            else{
                jitter_listA.add((float) 0);
                shimmer_listA.add((float) 0);
            }
        }

        if (path_ah == null) {
            tjitterA.setText(R.string.Empty);
            tshimmerA.setText(R.string.Empty);
        }


        //Vowel I

        // get exercises from sustained vowel ah to compute phonation features
        int IDEx_I=19;
        String name_I=GetEx.getNameExercise(IDEx_I);
        long N_I = signalDataService.countSignalsbyname(name_I);

        if (N_I>0) {

            List<SignalDA> signals_I = signalDataService.getSignalsbyname(name_I);
            if (signals_I.size() > 0) {
                path_i = signals_I.get(signals_I.size() - 1).getSignalPath();
                if (signals_I.size()>3){
                    for (int i=signals_I.size()-3;i<signals_I.size();i++){
                        path_i_all.add(signals_I.get(i).getSignalPath());
                    }
                } else {
                    for (int i=0;i<signals_I.size();i++){
                        path_i_all.add(signals_I.get(i).getSignalPath());
                    }
                }
            }
        }

        ArrayList<Float> jitter_listI=new ArrayList<>();
        ArrayList<Float> shimmer_listI=new ArrayList<>();
        for (int i=0;i<3;i++){

            if (i<path_i_all.size()){

                // Jitter sustained vowel i
                JitterTemp = Jitter(path_i_all.get(i));
                jitter_listI.add(JitterTemp);

                // Shimmer sustained vowel ah
                ShimmerTemp = Shimmer(path_i_all.get(i));
                shimmer_listI.add(ShimmerTemp);

                // Show phonation features for the last speech task from sustained vowel i
                if (path_i.equals(path_i_all.get(i))){

                    jitterI = JitterTemp;
                    String JitterStr = String.valueOf(df.format(jitterI)) + "%";
                    tjitterI.setText(JitterStr);

                    shimmerI = ShimmerTemp;
                    String ShimmerStr = String.valueOf(df.format(shimmerI)) + "%";
                    tshimmerI.setText(ShimmerStr);
                }
            }
            else{
                jitter_listI.add((float) 0);
                shimmer_listI.add((float) 0);
            }
        }

        if (path_i == null) {
            tjitterI.setText(R.string.Empty);
            tshimmerI.setText(R.string.Empty);
        }

        //Vowel U

        // get exercises from sustained vowel ah to compute phonation features
        int IDEx_U=20;
        String name_U=GetEx.getNameExercise(IDEx_U);

        long N_U = signalDataService.countSignalsbyname(name_U);

        if (N_U>0) {

            List<SignalDA> signals_U = signalDataService.getSignalsbyname(name_U);
            if (signals_U.size() > 0) {
                path_u = signals_U.get(signals_U.size() - 1).getSignalPath();
                if (signals_U.size()>3){
                    for (int i=signals_U.size()-3;i<signals_U.size();i++){
                        path_u_all.add(signals_U.get(i).getSignalPath());
                    }
                } else {
                    for (int i=0;i<signals_U.size();i++){
                        path_u_all.add(signals_U.get(i).getSignalPath());
                    }
                }
            }
        }

        ArrayList<Float> jitter_listU=new ArrayList<>();
        ArrayList<Float> shimmer_listU=new ArrayList<>();
        for (int i=0;i<3;i++){

            if (i<path_u_all.size()){

                // Jitter sustained vowel u
                JitterTemp = Jitter(path_u_all.get(i));
                jitter_listU.add(JitterTemp);

                // Shimmer sustained vowel u
                ShimmerTemp = Shimmer(path_u_all.get(i));
                shimmer_listU.add(ShimmerTemp);

                // Show phonation features for the last speech task from sustained vowel i
                if (path_u.equals(path_u_all.get(i))){

                    jitterU = JitterTemp;
                    String JitterStr = String.valueOf(df.format(jitterU)) + "%";
                    tjitterU.setText(JitterStr);

                    shimmerU = ShimmerTemp;
                    String ShimmerStr = String.valueOf(df.format(shimmerU)) + "%";
                    tshimmerU.setText(ShimmerStr);

                }
            }
            else{
                jitter_listU.add((float) 0);
                shimmer_listU.add((float) 0);
            }
        }
        if (path_u == null) {
            tjitterU.setText(R.string.Empty);
            tshimmerU.setText(R.string.Empty);
        }


        // Emoji for Jitter A

        if (jitterA <= 3) {
            iEmojinPhonation.setImageResource(R.drawable.happy_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinPhonation.startAnimation(animation);
            tmessage_phonation.setText(R.string.Positive_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_phonation.startAnimation(animation2);
        } else if (jitterA <= 10) {
            iEmojinPhonation.setImageResource(R.drawable.medium_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinPhonation.startAnimation(animation);
            tmessage_phonation.setText(R.string.Medium_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_phonation.startAnimation(animation2);
        } else {
            iEmojinPhonation.setImageResource(R.drawable.sad_emoji);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinPhonation.startAnimation(animation);
            tmessage_phonation.setText(R.string.Negative_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_phonation.startAnimation(animation2);
        }

        // Graphic for Jitter
        String Ylabel=getResources().getString(R.string.jitter);
        String Xlabel=getResources().getString(R.string.session);

        GraphView graph =findViewById(R.id.bar_perc_phonation);

        DataPoint[] dp = new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(1,jitter_listA.get(0)),
                new DataPoint(2,jitter_listI.get(0)),
                new DataPoint(3,jitter_listU.get(0)),
                new DataPoint(4,0),
                new DataPoint(5,jitter_listA.get(1)),
                new DataPoint(6,jitter_listI.get(1)),
                new DataPoint(7,jitter_listU.get(1)),
                new DataPoint(8,0),
                new DataPoint(9,jitter_listA.get(2)),
                new DataPoint(10,jitter_listI.get(2)),
                new DataPoint(11,jitter_listU.get(2)),
                new DataPoint(12, 0),
        };

        float max_A = Math.max(jitter_listA.get(0), Math.max(jitter_listA.get(1), jitter_listA.get(2)));
        float max_I = Math.max(jitter_listI.get(0), Math.max(jitter_listI.get(1), jitter_listI.get(2)));
        float max_U = Math.max(jitter_listU.get(0), Math.max(jitter_listU.get(1), jitter_listU.get(2)));

        float max_all = Math.max(max_A, Math.max(max_I, max_U));

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

        // Emoji for Shimmer A
        if (shimmerA <= 3) {
            iEmojinShimmer.setImageResource(R.drawable.happy_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinShimmer.startAnimation(animation);
            tmessage_shimmer.setText(R.string.Positive_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_shimmer.startAnimation(animation2);
        } else if (shimmerA <= 10) {
            iEmojinShimmer.setImageResource(R.drawable.medium_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinShimmer.startAnimation(animation);
            tmessage_shimmer.setText(R.string.Medium_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_shimmer.startAnimation(animation2);
        } else {
            iEmojinShimmer.setImageResource(R.drawable.sad_emoji);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinShimmer.startAnimation(animation);
            tmessage_shimmer.setText(R.string.Negative_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_shimmer.startAnimation(animation2);
        }


        // Graphic for Shimmer
        String Ylabel_shimmer=getResources().getString(R.string.shimmer);
        GraphView graph_shimmer =findViewById(R.id.bar_perc_shimmer);

        DataPoint[] dp_shimmer = new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(1,shimmer_listA.get(0)),
                new DataPoint(2,shimmer_listI.get(0)),
                new DataPoint(3,shimmer_listU.get(0)),
                new DataPoint(4,0),
                new DataPoint(5,shimmer_listA.get(1)),
                new DataPoint(6,shimmer_listI.get(1)),
                new DataPoint(7,shimmer_listU.get(1)),
                new DataPoint(8,0),
                new DataPoint(9,shimmer_listA.get(2)),
                new DataPoint(10,shimmer_listI.get(2)),
                new DataPoint(11,shimmer_listU.get(2)),
                new DataPoint(12, 0),
        };

        max_A = Math.max(shimmer_listA.get(0), Math.max(shimmer_listA.get(1), shimmer_listA.get(2)));
        max_I = Math.max(shimmer_listI.get(0), Math.max(shimmer_listI.get(1), shimmer_listI.get(2)));
        max_U = Math.max(shimmer_listU.get(0), Math.max(shimmer_listU.get(1), shimmer_listU.get(2)));

        max_all = Math.max(max_A, Math.max(max_I, max_U));

        max_graph = Math.round(max_all + (float) 0.5);

        BarGraphSeries<DataPoint> series_shimmer = new BarGraphSeries<>(dp_shimmer);
        graph_shimmer.addSeries(series_shimmer);

        GridLabelRenderer gridLabel2 = graph_shimmer.getGridLabelRenderer();
        gridLabel2.setHorizontalAxisTitle(Xlabel);
        gridLabel2.setVerticalAxisTitle(Ylabel_shimmer);
        //graph_shimmer.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        StaticLabelsFormatter staticLabelsFormatter2 = new StaticLabelsFormatter(graph_shimmer);
        staticLabelsFormatter2.setHorizontalLabels(new String[] {" ","1", " ", "2", " ", "3", " "});
        graph_shimmer.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter2);

        graph_shimmer.getViewport().setMinX(0);
        graph_shimmer.getViewport().setMaxX(12);
        graph_shimmer.getViewport().setMinY(0);
        graph_shimmer.getViewport().setMaxY(max_graph+1);

        graph_shimmer.getViewport().setXAxisBoundsManual(true);
        graph_shimmer.getViewport().setYAxisBoundsManual(true);

        //series.setDrawValuesOnTop(true);
        //series.setValuesOnTopColor(Color.RED);
        series_shimmer.setSpacing(5);

        series_shimmer.setValueDependentColor(new ValueDependentColor<DataPoint>() {
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
