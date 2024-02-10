package fr.corenting.edcompanion.utils;

import fr.corenting.edcompanion.R;

public class InternalNamingUtils {
    public static String getShipName(String internalName) {
        return switch (internalName) {
            case "SideWinder" -> "Sidewinder Mk I";
            case "Eagle" -> "Eagle Mk II";
            case "Viper" -> "Viper Mk III";
            case "CobraMkIII" -> "Cobra Mk III";
            case "Type6" -> "Type-6 Transporter";
            case "Type7" -> "Type-7 Transporter";
            case "Asp" -> "Asp Explorer";
            case "Empire_Trader" -> "Imperial Clipper";
            case "Federation_Dropship" -> "Federal Dropship";
            case "Type9" -> "Type-9 Heavy";
            case "BelugaLiner" -> "Beluga Liner";
            case "FerDeLance" -> "Fer-de-Lance";
            case "Federation_Corvette" -> "Federal Corvette";
            case "Cutter" -> "Imperial Cutter";
            case "DiamondBack" -> "Diamondback Scout";
            case "Empire_Courier" -> "Imperial Courier";
            case "DiamondBackXL" -> "Diamondback Explorer";
            case "Empire_Eagle" -> "Imperial Eagle";
            case "Federation_Dropship_MkII" -> "Federal Assault Ship";
            case "Federation_Gunship" -> "Federal Gunship";
            case "Viper_MkIV" -> "Viper Mk IV";
            case "CobraMkIV" -> "Cobra Mk IV";
            case "Independant_Trader" -> "Keelback";
            case "Asp_Scout" -> "Asp Scout";
            case "Type9_Military" -> "Type-10 Defender";
            case "Krait_MkII" -> "Krait Mk II";
            case "TypeX" -> "Alliance Chieftain";
            case "TypeX_2" -> "Alliance Crusader";
            case "TypeX_3" -> "Alliance Challenger";
            case "Krait_Light" -> "Krait Phantom";
            default -> internalName;
        };
    }

    public static int getCombatLogoId(int rankValue) {
        return switch (rankValue + 1) {
            default -> R.drawable.rank_1_combat;
            case 2 -> R.drawable.rank_2_combat;
            case 3 -> R.drawable.rank_3_combat;
            case 4 -> R.drawable.rank_4_combat;
            case 5 -> R.drawable.rank_5_combat;
            case 6 -> R.drawable.rank_6_combat;
            case 7 -> R.drawable.rank_7_combat;
            case 8 -> R.drawable.rank_8_combat;
            case 9, 10, 11, 12, 13, 14 ->
                // there are no public icons for Elite I-V ranks
                    R.drawable.rank_9_combat;
        };
    }

    public static int getTradeLogoId(int rankValue) {
        return switch (rankValue + 1) {
            default -> R.drawable.rank_1_trading;
            case 2 -> R.drawable.rank_2_trading;
            case 3 -> R.drawable.rank_3_trading;
            case 4 -> R.drawable.rank_4_trading;
            case 5 -> R.drawable.rank_5_trading;
            case 6 -> R.drawable.rank_6_trading;
            case 7 -> R.drawable.rank_7_trading;
            case 8 -> R.drawable.rank_8_trading;
            case 9, 10, 11, 12, 13, 14 ->
                // there are no public icons for Elite I-V ranks
                    R.drawable.rank_9_trading;
        };
    }

    public static int getExplorationLogoId(int rankValue) {
        return switch (rankValue + 1) {
            default -> R.drawable.rank_1_exploration;
            case 2 -> R.drawable.rank_2_exploration;
            case 3 -> R.drawable.rank_3_exploration;
            case 4 -> R.drawable.rank_4_exploration;
            case 5 -> R.drawable.rank_5_exploration;
            case 6 -> R.drawable.rank_6_exploration;
            case 7 -> R.drawable.rank_7_exploration;
            case 8 -> R.drawable.rank_8_exploration;
            case 9, 10, 11, 12, 13, 14 ->
                // there are no public icons for Elite I-V ranks
                    R.drawable.rank_9_exploration;
        };
    }

    public static int getCqcLogoId(int rankValue) {
        return switch (rankValue + 1) {
            default -> R.drawable.rank_1_cqc;
            case 2 -> R.drawable.rank_2_cqc;
            case 3 -> R.drawable.rank_3_cqc;
            case 4 -> R.drawable.rank_4_cqc;
            case 5 -> R.drawable.rank_5_cqc;
            case 6 -> R.drawable.rank_6_cqc;
            case 7 -> R.drawable.rank_7_cqc;
            case 8 -> R.drawable.rank_8_cqc;
            case 9, 10, 11, 12, 13, 14 ->
                // there are no public icons for Elite I-V ranks
                    R.drawable.rank_9_cqc;
        };
    }
}
