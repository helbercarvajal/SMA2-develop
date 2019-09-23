package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.R;

import java.util.ArrayList;
import java.util.List;


public class RadarChart_PosturalTremor_Activity extends AppCompatActivity implements View.OnClickListener {

    Button bBackradarchart;


    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movementRight = null, path_movementLeft = null;
    List<String> path_movement_all_right= new ArrayList<String>(), path_movement_all_left= new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_chart__postural_tremor_);



        bBackradarchart=findViewById(R.id.button_back_radarchart);
        bBackradarchart.setOnClickListener(this);


        //----------------RADARCHAT TREMOR LEFT HAND------------------
        RadarChart radarchartleft= findViewById(R.id.radar_chart1);
        radarchartleft.getDescription().setEnabled(false);
        radarchartleft.animateXY(5000, 5000, Easing.EaseInOutQuad);

        RadarData radardataleft= new RadarData();

        float posturaltremorleft = CalculatePosturalTremorLeft();
        float posturalstabilityleft = CalculatePosturalStabilityLeft();

        float movementtremorleft = CalculateMovementTremorLeft();
        float movementstabvelleft= CalculateMovementStabVelLeft();

        float[] datos1left={posturalstabilityleft,posturaltremorleft,movementstabvelleft,movementtremorleft}; // datos que se van a graficar
        float[] datos2left={80f,20f,80f,20f};

        double area1eft=0.5*(datos1left[0]+datos1left[2])*(datos1left[1]+datos1left[3]);
        double area2left=0.5*(datos2left[0]+datos2left[2])*(datos2left[1]+datos2left[3]);

        radardataleft=setdata(datos1left,datos2left);
        String[] labelsleft={"Estabilidad en rep.","Temblor en rep.", "Estabilidad de vel.", "Temblor en mov."};

        XAxis xAxisleft=radarchartleft.getXAxis();
        xAxisleft.setTextSize(12f);
        xAxisleft.setTextColor(Color.BLACK);
        xAxisleft.setValueFormatter(new IndexAxisValueFormatter(labelsleft));
        xAxisleft.setLabelRotationAngle(90f);

        YAxis yAxisleft = radarchartleft.getYAxis();
        yAxisleft.setAxisMinimum(0f);
        yAxisleft.setAxisMaximum(80f);


        Legend lleft = radarchartleft.getLegend();
        lleft.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lleft.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        lleft.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        lleft.setDrawInside(false);
        lleft.setXEntrySpace(7f);
        lleft.setYEntrySpace(5f);
        lleft.setTextColor(Color.BLACK);
        lleft.setTextSize(20f);

        radarchartleft.setExtraOffsets(0,-400,0,-400);
        //radarchart.setBackgroundColor(Color.WHITE);
        radarchartleft.setScaleY(1f);
        radarchartleft.setScaleX(1f);
        radarchartleft.setData(radardataleft);
        radarchartleft.invalidate(); // refresh
        //-----------------------------------------------------------------


        //----------------RADARCHAT TREMOR RIGHT HAND------------------
        RadarChart radarchartright= findViewById(R.id.radar_chart2);
        radarchartright.getDescription().setEnabled(false);
        radarchartright.animateXY(5000, 5000, Easing.EaseInOutQuad);

        RadarData radardataright= new RadarData();

        float posturaltremorright = CalculatePosturalTremorRight();
        float posturalstabilityright = CalculatePosturalStabilityRight();

        float movementtremorrigth = CalculateMovementTremorRigth();
        float movementstabvelrigth= CalculateMovementStabVelRigth();


        float[] datos1right={posturalstabilityright,posturaltremorright,movementstabvelrigth,movementtremorrigth}; // datos que se van a graficar
        float[] datos2right={80f,20f,80f,20f};

        double area1right=0.5*(datos1right[0]+datos1right[2])*(datos1right[1]+datos1right[3]);
        double area2right=0.5*(datos2right[0]+datos2right[2])*(datos2right[1]+datos2right[3]);

        radardataright=setdata(datos1right,datos2right);
        String[] labelsright={"Estabilidad en rep.","Temblor en rep.", "Estabilidad de vel.", "Temblor en mov."};

        XAxis xAxisright=radarchartright.getXAxis();
        xAxisright.setTextSize(12f);
        xAxisright.setTextColor(Color.BLACK);
        xAxisright.setValueFormatter(new IndexAxisValueFormatter(labelsright));
        xAxisright.setLabelRotationAngle(90f);

        YAxis yAxisright = radarchartright.getYAxis();
        yAxisright.setAxisMinimum(0f);
        yAxisright.setAxisMaximum(80f);


        Legend lright = radarchartright.getLegend();
        lright.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lright.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        lright.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        lright.setDrawInside(false);
        lright.setXEntrySpace(7f);
        lright.setYEntrySpace(5f);
        lright.setTextColor(Color.BLACK);
        lright.setTextSize(20f);

        radarchartright.setExtraOffsets(0,-400,0,-400);
        //radarchart.setBackgroundColor(Color.WHITE);
        radarchartright.setScaleY(1f);
        radarchartright.setScaleX(1f);
        radarchartright.setData(radardataright);
        radarchartright.invalidate(); // refresh
        //-----------------------------------------------------------------

    }

    //----------------Graphic RadarChart -------------------------------
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

        RadarDataSet set1 = new RadarDataSet(entries1, "Actual session");
        set1.setColor(Color.rgb(255, 185, 0));
        set1.setFillColor(Color.rgb(255, 185, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(2f);
        set1.setValueTextColor(Color.rgb(255, 185, 0));
        set1.setValueTextSize(15f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "Reference");
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



    //------------------------------------------------


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_back_radarchart:
                onButtonBack();
                break;
        }

    }

    private void onButtonBack(){
        Intent i =new Intent(RadarChart_PosturalTremor_Activity.this, PosturalTremor_feature_Activity.class);
        startActivity(i);

    }

    //--------START FUNCTIONS FOR POSTURAL TREMOR---------------------------------------------------
    // Compute Tremor for both hands
    // Left Hand
    private float CalculatePosturalTremorLeft(){

        double vtremor=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=30;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        double TremorLeft=0;

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>1){
                for (int i=SignalsLeft.size()-1;i<SignalsLeft.size();i++){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<SignalsLeft.size();i++){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if(path_movementLeft==null){
            vtremor=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
            vtremor = 100-TremorLeft;

        }

        return (float) vtremor;
    }
    // Rigth Hand
    private float CalculatePosturalTremorRight(){
        double vtremor=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=29;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);


        double TremorRight=0;
        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>1){
                for (int i=SignalsRight.size()-1;i<SignalsRight.size();i++){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<SignalsRight.size();i++){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }

        }


        if(path_movementRight==null){
            vtremor=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            vtremor = 100-TremorRight;
        }

        return (float) vtremor;
    }


    // Compute Stability for both hands
    // Left Hand
    private float CalculatePosturalStabilityLeft(){

        double vstability=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=30;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        float[] FLeft={0};
        double StabLeft;

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>1){
                for (int i=SignalsLeft.size()-1;i<SignalsLeft.size();i++){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<SignalsLeft.size();i++){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if(path_movementLeft==null){
            vstability=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");

            FLeft=MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal,100);
            StabLeft=MovementProcessor.perc_stab_freq(FLeft);
            vstability=100-StabLeft;
        }

        return (float) vstability;
    }

    // Rigth Hand
    private float CalculatePosturalStabilityRight(){
        double vstability=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=29;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);


        float[] FRight={0};
        double StabRigth;

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>1){
                for (int i=SignalsRight.size()-1;i<SignalsRight.size();i++){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<SignalsRight.size();i++){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }

        }


        if(path_movementRight==null){
            vstability=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            FRight=MovementProcessor.ComputeF0(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal,100);
            StabRigth=MovementProcessor.perc_stab_freq(FRight);
            vstability=100-StabRigth;

        }

        return (float) vstability;

    }

    //--------END FUNCTIONS FOR POSTURAL TREMOR---------------------------------------------------



    //--------START FUNCTIONS FOR MOVEMENT TREMOR---------------------------------------------------

    // Compute Tremor for both hands
    // Rigth Hand

    private float CalculateMovementTremorRigth() {
        double tremormovR = 0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 27;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);


        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsRight = signalDataService.getSignalsbyname(name);
        if (SignalsRight.size() > 0) {
            path_movementRight = PATH + SignalsRight.get(SignalsRight.size() - 1).getSignalPath();

            if (SignalsRight.size() > 1) {
                for (int i = SignalsRight.size() - 1; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            }

        }


        if (path_movementRight == null) {
            tremormovR = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            tremormovR = MovementProcessor.TremorMov(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tremormovR = 100 - tremormovR;

        }

        return (float) tremormovR;

    }

    // Left Hand

    private float CalculateMovementTremorLeft() {
        double tremormovL = 0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 28;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);

        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsLeft = signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size() > 0) {
            path_movementLeft = PATH + SignalsLeft.get(SignalsLeft.size() - 1).getSignalPath();

            if (SignalsLeft.size() > 1) {
                for (int i = SignalsLeft.size() - 1; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if (path_movementLeft == null) {
            tremormovL = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");

            tremormovL = MovementProcessor.TremorMov(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tremormovL = 100 - tremormovL;
        }

        return (float) tremormovL;
    }
    // Compute Stability for both hands
    // Rigth Hand

    private float CalculateMovementStabVelRigth() {
        double stabVelR=0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 27;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);


        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsRight = signalDataService.getSignalsbyname(name);
        if (SignalsRight.size() > 0) {
            path_movementRight = PATH + SignalsRight.get(SignalsRight.size() - 1).getSignalPath();

            if (SignalsRight.size() > 1) {
                for (int i = SignalsRight.size() - 1; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            }

        }


        if (path_movementRight == null) {
            stabVelR = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            stabVelR = MovementProcessor.stabVelocity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            stabVelR = 100 - stabVelR;

        }

        return (float) stabVelR;

    }
    // Left Hand
    private float CalculateMovementStabVelLeft() {
        double stabVelL=0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 28;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);

        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsLeft = signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size() > 0) {
            path_movementLeft = PATH + SignalsLeft.get(SignalsLeft.size() - 1).getSignalPath();

            if (SignalsLeft.size() > 1) {
                for (int i = SignalsLeft.size() - 1; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if (path_movementLeft == null) {
            stabVelL = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            stabVelL = MovementProcessor.stabVelocity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            stabVelL = 100 - stabVelL;

        }

        return (float) stabVelL;

    }

    //--------END FUNCTIONS FOR MOVEMENT TREMOR---------------------------------------------------
}



