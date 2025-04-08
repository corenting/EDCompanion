package fr.corenting.edcompanion.utils;

import fr.corenting.edcompanion.R;

public class InternalNamingUtils {
    public static String getShipName(String internalName) {
        String nameToMatch = internalName.toLowerCase();
        return switch (nameToMatch) {
            case "adder" -> "Adder",
            case "anaconda" -> "Anaconda",
            case "asp" -> "Asp Explorer",
            case "asp_scout" -> "Asp Scout",
            case "belugaliner" -> "Beluga Liner",
            case "cobramkiii" -> "Cobra Mk III",
            case "cobramkiv" -> "Cobra Mk IV",
            case "cobramkv" -> "Cobra Mk V",
            case "clipper" -> "Panther Clipper",
            case "cutter" -> "Imperial Cutter",
            case "diamondback" -> "Diamondback Scout",
            case "diamondbackxl" -> "Diamondback Explorer",
            case "dolphin" -> "Dolphin",
            case "eagle" -> "Eagle",
            case "empire_courier" -> "Imperial Courier",
            case "empire_eagle" -> "Imperial Eagle",
            case "empire_trader" -> "Imperial Clipper",
            case "federation_corvette" -> "Federal Corvette",
            case "federation_dropship" -> "Federal Dropship",
            case "federation_dropship_mkii" -> "Federal Assault Ship",
            case "federation_gunship" -> "Federal Gunship",
            case "ferdelance" -> "Fer-de-Lance",
            case "hauler" -> "Hauler",
            case "independant_trader" -> "Keelback",
            case "krait_mkii" -> "Krait Mk II",
            case "krait_light" -> "Krait Phantom",
            case "mamba" -> "Mamba",
            case "mandalay" -> "Mandalay",
            case "orca" -> "Orca",
            case "python" -> "Python",
            case "python_nx" -> "Python Mk II",
            case "sidewinder" -> "Sidewinder",
            case "type6" -> "Type-6 Transporter",
            case "type7" -> "Type-7 Transporter",
            case "type8" -> "Type-8 Transporter",
            case "type9" -> "Type-9 Heavy",
            case "type9_military" -> "Type-10 Defender",
            case "typex" -> "Alliance Chieftain",
            case "typex_2" -> "Alliance Crusader",
            case "typex_3" -> "Alliance Challenger",
            case "viper" -> "Viper Mk III",
            case "viper_mkiv" -> "Viper Mk IV",
            case "vulture" -> "Vulture",
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
