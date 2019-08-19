package com.sma2.sma2.FeatureExtraction.Speech.features;

import android.util.Log;

import com.sma2.sma2.FeatureExtraction.Speech.tools.Matrix;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.array_manipulation;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;

import static java.util.Arrays.copyOfRange;

public class ProsFeatures {
    private Matrix featmat = null;//Feature matrix
    double[] meanBarkBands;


    public double getSD_F0(String AudioFile){
        WAVfileReader wavFileReader=new WAVfileReader();
        f0detector F0Detector =new f0detector();


        int[] InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] Signal=wavFileReader.readWAV(InfoSig[0]);
        sigproc SigProc = new sigproc();
        Signal = SigProc.normsig(Signal);
        float[] F0= F0Detector.sig_f0(Signal, InfoSig[1]);
        double mean= calculateMean(F0);


            return calculateSD(F0,mean);
        }


    public float voiceRate(String AudioFile) {
        WAVfileReader wavFileReader=new WAVfileReader();
        f0detector F0Detector =new f0detector();


        int[] InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] Signal=wavFileReader.readWAV(InfoSig[0]);
        float datalen= InfoSig[0];
        sigproc SigProc = new sigproc();
        Signal = SigProc.normsig(Signal);
        float[] F0= F0Detector.sig_f0(Signal, InfoSig[1]);
        List VoicedSeg = F0Detector.voiced(F0, Signal);

        float vRate=InfoSig[1]*VoicedSeg.size()/datalen;

        return vRate;
    }

    public float maxEnergy(String AudioFile) {
        WAVfileReader wavFileReader=new WAVfileReader();
        Energy energy =new Energy();

        int[] InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] Signal=wavFileReader.readWAV(InfoSig[0]);
        sigproc SigProc = new sigproc();
        Signal = SigProc.normsig(Signal);

        float[] energyCo= energy.energyContour(Signal, InfoSig[1]);
        double max;
        if(Double.isInfinite((double)energyCo[0])){max=-10E-100;}
        else if (Double.isNaN((double)energyCo[0])){max=-10E-100;}
        else{max=energyCo[0];}
        for(int i=0;i<energyCo.length;i++) { max= Math.max(max, energyCo[i]);}

        return (float) max ;
    }





    private double calculateMean(float[] vector){
        double mean=0.0, std=0.0;
        int cont=0;
        for(int i=0; i<vector.length; i++){
            if(vector[i]>50) {
                mean = mean + vector[i];
                cont=cont+1;
            }
        }
        mean=mean/cont;

        return mean;
    }

    private double calculateSD(float[] vector, double mean){

        double  std=0.0;
        int cont=0;
        for(int i=0; i<vector.length; i++){
            if(vector[i]>50) {
                std = std + ((vector[i] - mean) * (vector[i] - mean));
                cont=cont+1;
            }

        }
        std=Math.sqrt(std/cont);

        return std;
    }



}
