package beer.unaccpetable.brewzilla.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

import beer.unaccpetable.brewzilla.Adapters.Adapter;
import beer.unaccpetable.brewzilla.Models.FermentableAddition;
import beer.unaccpetable.brewzilla.Models.HopAddition;
import beer.unaccpetable.brewzilla.Models.YeastAddition;

/**
 * Created by zak on 11/17/2016.
 */

public class Calculations {

    //TODO: Remove this whole class. All math is done on the server...

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

    public static String GetSRMColor(int iSRM) {
        String returnable = "";
        switch (iSRM)
        {
            case 0:
                returnable = "#FFE699";
                break;
            case 1:
                returnable = "#FFE699";
                break;
            case 2:
                returnable = "#FFD878";
                break;
            case 3:
                returnable = "#FFCA5A";
                break;
            case 4:
                returnable = "#FFBF42";
                break;
            case 5:
                returnable = "#FBB123";
                break;
            case 6:
                returnable = "#F8A600";
                break;
            case 7:
                returnable = "#F39C00";
                break;
            case 8:
                returnable = "#EA8F00";
                break;
            case 9:
                returnable = "#E58500";
                break;
            case 10:
                returnable = "#DE7C00";
                break;
            case 11:
                returnable = "#D77200";
                break;
            case 12:
                returnable = "#CF6900";
                break;
            case 13:
                returnable = "#CB6200";
                break;
            case 14:
                returnable = "#C35900";
                break;
            case 15:
                returnable = "#BB5100";
                break;
            case 16:
                returnable = "#B54C00";
                break;
            case 17:
                returnable = "#B04500";
                break;
            case 18:
                returnable = "#A63E00";
                break;
            case 19:
                returnable = "#A13700";
                break;
            case 20:
                returnable = "#9B3200";
                break;
            case 21:
                returnable = "#952D00";
                break;
            case 22:
                returnable = "#8E2900";
                break;
            case 23:
                returnable = "#882300";
                break;
            case 24:
                returnable = "#821E00";
                break;
            case 25:
                returnable = "#7B1A00";
                break;
            case 26:
                returnable = "#771900";
                break;
            case 27:
                returnable = "#701400";
                break;
            case 28:
                returnable = "#6A0E00";
                break;
            case 29:
                returnable = "#660D00";
                break;
            case 30:
                returnable = "#5E0B00";
                break;
            case 31:
                returnable = "#5A0A02";
                break;
            case 32:
                returnable = "#600903";
                break;
            case 33:
                returnable = "#520907";
                break;
            case 34:
                returnable = "#4C0505";
                break;
            case 35:
                returnable = "#470606";
                break;
            case 36:
                returnable = "#440607";
                break;
            case 37:
                returnable = "#3F0708";
                break;
            case 38:
                returnable = "#3B0607";
                break;
            case 39:
                returnable = "#3A070B";
                break;
            default:
                returnable = "#36080A";
                break;
        }
        return returnable;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
