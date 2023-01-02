package fr.corenting.edcompanion.utils;

import fr.corenting.edcompanion.R;

public class InternalNamingUtils {
    public static String getShipName(String internalName) {
        switch (internalName) {
            case "SideWinder":
                return "Sidewinder Mk I";
            case "Eagle":
                return "Eagle Mk II";
            case "Viper":
                return "Viper Mk III";
            case "CobraMkIII":
                return "Cobra Mk III";
            case "Type6":
                return "Type-6 Transporter";
            case "Type7":
                return "Type-7 Transporter";
            case "Asp":
                return "Asp Explorer";
            case "Empire_Trader":
                return "Imperial Clipper";
            case "Federation_Dropship":
                return "Federal Dropship";
            case "Type9":
                return "Type-9 Heavy";
            case "BelugaLiner":
                return "Beluga Liner";
            case "FerDeLance":
                return "Fer-de-Lance";
            case "Federation_Corvette":
                return "Federal Corvette";
            case "Cutter":
                return "Imperial Cutter";
            case "DiamondBack":
                return "Diamondback Scout";
            case "Empire_Courier":
                return "Imperial Courier";
            case "DiamondBackXL":
                return "Diamondback Explorer";
            case "Empire_Eagle":
                return "Imperial Eagle";
            case "Federation_Dropship_MkII":
                return "Federal Assault Ship";
            case "Federation_Gunship":
                return "Federal Gunship";
            case "Viper_MkIV":
                return "Viper Mk IV";
            case "CobraMkIV":
                return "Cobra Mk IV";
            case "Independant_Trader":
                return "Keelback";
            case "Asp_Scout":
                return "Asp Scout";
            case "Type9_Military":
                return "Type-10 Defender";
            case "Krait_MkII":
                return "Krait Mk II";
            case "TypeX":
                return "Alliance Chieftain";
            case "TypeX_2":
                return "Alliance Crusader";
            case "TypeX_3":
                return "Alliance Challenger";
            case "Krait_Light":
                return "Krait Phantom";
            default:
                return internalName;
        }
    }

    public static int getCombatLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_combat;
            case 2:
                return R.drawable.rank_2_combat;
            case 3:
                return R.drawable.rank_3_combat;
            case 4:
                return R.drawable.rank_4_combat;
            case 5:
                return R.drawable.rank_5_combat;
            case 6:
                return R.drawable.rank_6_combat;
            case 7:
                return R.drawable.rank_7_combat;
            case 8:
                return R.drawable.rank_8_combat;
            case 9:
                return R.drawable.rank_9_combat;
        }
    }

    public static int getTradeLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_trading;
            case 2:
                return R.drawable.rank_2_trading;
            case 3:
                return R.drawable.rank_3_trading;
            case 4:
                return R.drawable.rank_4_trading;
            case 5:
                return R.drawable.rank_5_trading;
            case 6:
                return R.drawable.rank_6_trading;
            case 7:
                return R.drawable.rank_7_trading;
            case 8:
                return R.drawable.rank_8_trading;
            case 9:
                return R.drawable.rank_9_trading;
        }
    }

    public static int getExplorationLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_exploration;
            case 2:
                return R.drawable.rank_2_exploration;
            case 3:
                return R.drawable.rank_3_exploration;
            case 4:
                return R.drawable.rank_4_exploration;
            case 5:
                return R.drawable.rank_5_exploration;
            case 6:
                return R.drawable.rank_6_exploration;
            case 7:
                return R.drawable.rank_7_exploration;
            case 8:
                return R.drawable.rank_8_exploration;
            case 9:
                return R.drawable.rank_9_exploration;
        }
    }

    public static int getCqcLogoId(int rankValue) {
        switch (rankValue + 1) {
            default:
                return R.drawable.rank_1_cqc;
            case 2:
                return R.drawable.rank_2_cqc;
            case 3:
                return R.drawable.rank_3_cqc;
            case 4:
                return R.drawable.rank_4_cqc;
            case 5:
                return R.drawable.rank_5_cqc;
            case 6:
                return R.drawable.rank_6_cqc;
            case 7:
                return R.drawable.rank_7_cqc;
            case 8:
                return R.drawable.rank_8_cqc;
            case 9:
                return R.drawable.rank_9_cqc;
        }
    }
}
