package beer.unaccpetable.brewzilla.Tools;

import beer.unaccpetable.brewzilla.Adapters.Adapter;
import beer.unaccpetable.brewzilla.Ingredients.FermentableAddition;
import beer.unaccpetable.brewzilla.Ingredients.HopAddition;
import beer.unaccpetable.brewzilla.Ingredients.YeastAddition;

/**
 * Created by zak on 11/17/2016.
 */

public class Calculations {

    static double dIBUBoilTimeCurveFit = -0.04;
    static double dIntoFermenterVolume = 5;
    static double KitEfficiency = 0.7;

    public static int CalculateIBU(Adapter hops, Adapter malts) {
        double dIBU = 0;
        double dOG = CalculateOG(malts);
        double dFG = 0;
        double dfT = 0;
        double dUtil = 0;

        if (hops.size() == 0) {
            return (int)dIBU;
        }

        //TODO: I removed Time from Hop because it belongs in HopAddition and this broke these calculations
        for (int i = 0; i < hops.Dataset().size(); i++) {
            HopAddition h = (HopAddition) hops.get(i);
            dFG = 1.65 * (Math.pow(0.000125,(dOG - 1)));
            dfT = (1 - Math.pow(Math.E, dIBUBoilTimeCurveFit * h.time)) / 4.15;
            dUtil = dFG * dfT;
            dIBU += ((h.amount * h.hop.aau) * dUtil * 74.89) / dIntoFermenterVolume;
        }

        return (int)dIBU;
    }

    public static double CalculateOG(Adapter malts) {
        if (malts.size() == 0) return 1;

        double dPPGCalc = 0;

        for ( int i = 0; i < malts.size(); i++) {
            FermentableAddition m = (FermentableAddition)malts.get(i);
            dPPGCalc += m.fermentable.ppg * m.weight;
        }

        dPPGCalc *= KitEfficiency;

        return (1 + (dPPGCalc / dIntoFermenterVolume) / 1000);
    }

    public static double CalculateFG(Adapter malts, Adapter yeasts) {
        int yeastCount = 0;
        double attenuationTotal = 0;

        if (yeasts.size() == 0) return 0;

        for (int i = 0; i < yeasts.size(); i++) {
            YeastAddition y = (YeastAddition) yeasts.get(i);

            yeastCount++;
            attenuationTotal += y.yeast.attenuation;
        }

        double finalAttenuation = attenuationTotal / yeastCount;
        double currentFG = 1 + (((CalculateOG(malts) - 1) * 1000) *((100 - finalAttenuation)/ 100)) / 1000;

        return currentFG;
    }

    public static double CalculateABV(Adapter malts, Adapter yeasts) {
        double dOG = CalculateOG(malts);
        double dFG = CalculateFG(malts, yeasts);

        double dCurrentABV = (dOG - dFG) * 131.25;
        if ( dCurrentABV > 10 ) {
            dCurrentABV = (76.08 * (dOG - dFG) / (1.775 - dOG)) * (dFG / 0.794);
        }

        return (double)Math.round(dCurrentABV * 1000) / 1000;
    }

    public static int CalculateSRM(Adapter malts) {
        double dSRM = 0;
        for (int i = 0; i < malts.size(); i++ ) {
            FermentableAddition m = (FermentableAddition)malts.Dataset().get(i);
            dSRM += m.fermentable.color * m.weight;
        }

        dSRM = 1.4922 * (Math.pow(dSRM/dIntoFermenterVolume, 0.69));

        return (int)dSRM;
    }
}