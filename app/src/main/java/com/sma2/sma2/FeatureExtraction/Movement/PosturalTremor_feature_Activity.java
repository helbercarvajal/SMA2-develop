package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PosturalTremor_feature_Activity extends AppCompatActivity implements View.OnClickListener {


    Button bBack,bRadarchart;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movementRight = null, path_movementLeft = null;
    List<String> path_movement_all_right= new ArrayList<String>(), path_movement_all_left= new ArrayList<String>();
    TextView tTremor_left, tTremor_right;
    TextView tF0_left, tF0_rigth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postural_tremor_feature_);
        SignalDataService signalDataService =new SignalDataService(this);
        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");

        bBack=findViewById(R.id.button_back5);
        bBack.setOnClickListener(this);

        bRadarchart=findViewById(R.id.button_radarchart);
        bRadarchart.setOnClickListener(this);

        tTremor_left=findViewById(R.id.tTremor_left);
        tTremor_right=findViewById(R.id.tTremor_right);
        tF0_left=findViewById(R.id.tF0_left);
        tF0_rigth=findViewById(R.id.tF0_Right);



        int IDEx=29;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        float[] FRight={0};
        double StabRigth;
        double TremorRight=0;

        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>3){
                for (int i=SignalsRight.size()-3;i<SignalsRight.size();i++){
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
            tTremor_right.setText(R.string.Empty);
            tF0_rigth.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            TremorRight = 100-TremorRight;
            tTremor_right.setText(String.valueOf(df.format(TremorRight))+"%");

            FRight=MovementProcessor.ComputeF0(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal,100);
            StabRigth=100-MovementProcessor.perc_stab_freq(FRight);

            tF0_rigth.setText(String.valueOf(df.format(StabRigth))+"%");

        }


        IDEx=30;
        GetEx=new GetExercises(this);
        name=GetEx.getNameExercise(IDEx);

        double TremorLeft=0;
        float[] FLeft={0};
        double StabLeft;

        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);
        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>3){
                for (int i=SignalsLeft.size()-3;i<SignalsLeft.size();i++){
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
            tTremor_left.setText(R.string.Empty);
            tF0_left.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
            TremorLeft = 100-TremorLeft;

            tTremor_left.setText(String.valueOf(df.format(TremorLeft))+"%");

            FLeft=MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal,100);
            StabLeft=100-MovementProcessor.perc_stab_freq(FLeft);

            tF0_left.setText(String.valueOf(df.format(StabLeft))+"%");

        }


        // Graphic for LeftHand

        GraphManager graphManager=new GraphManager(this);
        ArrayList<Integer> xl=new ArrayList<>();
        ArrayList<Float> yl=new ArrayList<>();
        ArrayList<Float> yl2=new ArrayList<>();


        for (int i=0;i<3;i++){

            if (i<path_movement_all_left.size()){
                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aZ [m/s^2]");
                TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
                TremorLeft = 100-TremorLeft;


                FLeft=MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal,100);
                StabLeft=100-MovementProcessor.perc_stab_freq(FLeft);

                xl.add(i+1);
                yl.add((float)TremorLeft);
                yl2.add((float)StabLeft);
            }
            else{
                xl.add(i+1);
                yl.add((float) 0);
                yl2.add((float) 0);
            }

        }
        //-----------------------------------------------------------------

        String YlabelL=getResources().getString(R.string.TremorLeft);
        String XlabelL=getResources().getString(R.string.session);

        GraphView graphL =findViewById(R.id.bar_percTremorLeft);

        DataPoint[] dpL = new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(1,yl.get(0)),
                new DataPoint(2,yl2.get(0)),
                new DataPoint(3,0),
                new DataPoint(4,yl.get(1)),
                new DataPoint(5,yl2.get(1)),
                new DataPoint(6,0),
                new DataPoint(7,yl.get(2)),
                new DataPoint(8,yl2.get(2)),
                new DataPoint(9,0),
        };

        float max_ylL = Math.max(yl.get(0), Math.max(yl.get(1), yl.get(2)));
        float max_yl2L = Math.max(yl2.get(0), Math.max(yl2.get(1), yl2.get(2)));


        float max_allL = Math.max(max_ylL,max_yl2L);

        int max_graphL = (Math.round(max_allL + (float) 0.5))+10;
        if ((max_graphL)>100)
            max_graphL=100;

        BarGraphSeries<DataPoint> seriesL = new BarGraphSeries<>(dpL);
        graphL.addSeries(seriesL);

        GridLabelRenderer gridLabelL = graphL.getGridLabelRenderer();
        gridLabelL.setHorizontalAxisTitle(XlabelL);
        gridLabelL.setVerticalAxisTitle(YlabelL);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        StaticLabelsFormatter staticLabelsFormatterL = new StaticLabelsFormatter(graphL);
        staticLabelsFormatterL.setHorizontalLabels(new String[] {" ","1", " ", "2", " ", "3", " "});
        graphL.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterL);


        graphL.getViewport().setMinX(0);
        graphL.getViewport().setMaxX(9);
        graphL.getViewport().setMinY(0);
        graphL.getViewport().setMaxY(max_graphL);

        graphL.getViewport().setXAxisBoundsManual(true);
        graphL.getViewport().setYAxisBoundsManual(true);

        seriesL.setSpacing(5);

        seriesL.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if((data.getX()==1) || (data.getX()==4) || (data.getX()==7)|| (data.getX()==10))
                    return (Color.rgb(255,190,0));
                else
                    return (Color.rgb(255,140,0));
            }
        });



        // Graphic for RightHand
        //-----------------------------------------------------------------


        ArrayList<Integer> xr=new ArrayList<>();
        ArrayList<Float> yr=new ArrayList<>();
        ArrayList<Float> yr2=new ArrayList<>();
        for (int i=0;i<3;i++){

            if (i<path_movement_all_right.size()){
                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aZ [m/s^2]");
                TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
                TremorRight = 100-TremorRight;


                FRight=MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal,100);
                StabRigth=100-MovementProcessor.perc_stab_freq(FRight);

                xr.add(i+1);
                yr.add((float)TremorRight);
                yr2.add((float)StabRigth);
            }
            else{
                xr.add(i+1);
                yr.add((float) 0);
                yr2.add((float) 0);
            }

        }

        String YlabelR=getResources().getString(R.string.TremorRight);
        String XlabelR=getResources().getString(R.string.session);
        GraphView graphR =findViewById(R.id.bar_percTremorRight);

        DataPoint[] dpR = new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(1,yr.get(0)),
                new DataPoint(2,yr2.get(0)),
                new DataPoint(3,0),
                new DataPoint(4,yr.get(1)),
                new DataPoint(5,yr2.get(1)),
                new DataPoint(6,0),
                new DataPoint(7,yr.get(2)),
                new DataPoint(8,yr2.get(2)),
                new DataPoint(9,0),
        };

        float max_ylR = Math.max(yr.get(0), Math.max(yr.get(1), yr.get(2)));
        float max_yl2R = Math.max(yr2.get(0), Math.max(yr2.get(1), yr2.get(2)));


        float max_allR = Math.max(max_ylR,max_yl2R);

        int max_graphR = (Math.round(max_allR + (float) 0.5))+10;
        if ((max_graphR)>100)
            max_graphR=100;

        BarGraphSeries<DataPoint> seriesR = new BarGraphSeries<>(dpR);
        graphR.addSeries(seriesR);

        GridLabelRenderer gridLabelR = graphR.getGridLabelRenderer();
        gridLabelR.setHorizontalAxisTitle(XlabelR);
        gridLabelR.setVerticalAxisTitle(YlabelR);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        StaticLabelsFormatter staticLabelsFormatterR = new StaticLabelsFormatter(graphR);
        staticLabelsFormatterR.setHorizontalLabels(new String[] {" ","1", " ", "2", " ", "3", " "});
        graphR.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterR);


        graphR.getViewport().setMinX(0);
        graphR.getViewport().setMaxX(9);
        graphR.getViewport().setMinY(0);
        graphR.getViewport().setMaxY(max_graphR);

        graphR.getViewport().setXAxisBoundsManual(true);
        graphR.getViewport().setYAxisBoundsManual(true);

        seriesR.setSpacing(5);

        seriesR.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if((data.getX()==1) || (data.getX()==4) || (data.getX()==7)|| (data.getX()==10))
                    return (Color.rgb(255,190,0));
                else
                    return (Color.rgb(255,140,0));
            }
        });


        //-----------------------------------------------------------------

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back5:
                onButtonBack();
                break;
            case R.id.button_radarchart:
                onButtonRadarChar();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(PosturalTremor_feature_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }

    private void onButtonRadarChar(){
        Intent i =new Intent(PosturalTremor_feature_Activity.this, RadarChart_PosturalTremor_Activity.class);
        startActivity(i);

    }
}