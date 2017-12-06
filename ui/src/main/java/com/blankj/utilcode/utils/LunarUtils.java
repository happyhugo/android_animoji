//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

public class LunarUtils {
    private static int[] lunar_month_days = new int[]{1887, 5780, 5802, 19157, 2742, '쒷', 1198, 2646, '딪', 7466, 3412, 30122, 5482, 67949, 2396, 5294, 'ꩍ', 6732, 6954, '赕', 2772, 4954, 18781, 2396, '풛', 5274, 6730, '몥', 5800, 6868, 21210, 4790, '\ue937', 2350, 5270, '뙋', 3402, 3496, '閵', 1388, 4782, 18735, 2350, '첖', 6804, 7498, '궩', 2906, 1388, 29294, 4700, '來', 6442, 6804, '\udb4a', 5802, 2772, '镛', 1210, 4698, 22827, 5418, '\uf695', 3476, 5802, 'ꪵ', 2484, 5302, 27223, 2646, 70954, 7466, 3412, '햪', 5482, 2412, '钮', 5294, 2636, 32038, 6954, '\ueb55', 2772, 4826, '\ua95d', 2394, 5274, '驍', 6730, 72357, 5800, 5844, '틚', 4790, 2358, '钗', 5270, 87627, 3402, 3496, '햴', 5484, 4782, '꤯', 2350, 3222, 27978, 7498, 68965, 2904, 5484, '뉭', 4700, 6444, '骕', 6804, 6986, 19285, 2772, '\uf55b', 1210, 4698, '뤫', 5418, 5780, '險', 5546, 76469, 2420, 5302, '쩗', 2646, 5414, '躕', 3412, 5546, 18869, 2412, '풮', 5276, 6732, '봦', 6822, 2900, 28010, 4826, 92509, 2394, 5274, '\uda4b', 6730, 6820, '뭔', 5812, 2778, 18779, 2358, '\uf497', 5270, 5450, '뚥', 3492, 5556, 27318, 4718, 67887, 2350, 3222, '쵊', 7498, 3428, '镬', 5468, 4700, 31022, 6444, '歹', 6804, 6986, '\uab55', 2772, 5338, '詝', 2650, 70955, 5418, 5780, '횪', 5546, 2740, '钺', 5302, 2646, 29991, 3366, '\uee53', 3412, 5546, 'ꦵ', 2412, 5294, '詎', 6732, 72998, 6820, 6996, '쵪', 2778, 2396, '钝', 5274, 6698, 23333, 6820, 'ﭒ', 5812, 2746, '\ua95b', 2358, 5270, '驋', 5450, 79525, 3492, 5548};
    private static int[] solar_1_1 = new int[]{1887, 966732, 967231, 967733, 968265, 968766, 969297, 969798, 970298, 970829, 971330, 971830, 972362, 972863, 973395, 973896, 974397, 974928, 975428, 975929, 976461, 976962, 977462, 977994, 978494, 979026, 979526, 980026, 980558, 981059, 981559, 982091, 982593, 983124, 983624, 984124, 984656, 985157, 985656, 986189, 986690, 987191, 987722, 988222, 988753, 989254, 989754, 990286, 990788, 991288, 991819, 992319, 992851, 993352, 993851, 994383, 994885, 995385, 995917, 996418, 996918, 997450, 997949, 998481, 998982, 999483, 1000014, 1000515, 1001016, 1001548, 1002047, 1002578, 1003080, 1003580, 1004111, 1004613, 1005113, 1005645, 1006146, 1006645, 1007177, 1007678, 1008209, 1008710, 1009211, 1009743, 1010243, 1010743, 1011275, 1011775, 1012306, 1012807, 1013308, 1013840, 1014341, 1014841, 1015373, 1015874, 1016404, 1016905, 1017405, 1017937, 1018438, 1018939, 1019471, 1019972, 1020471, 1021002, 1021503, 1022035, 1022535, 1023036, 1023568, 1024069, 1024568, 1025100, 1025601, 1026102, 1026633, 1027133, 1027666, 1028167, 1028666, 1029198, 1029699, 1030199, 1030730, 1031231, 1031763, 1032264, 1032764, 1033296, 1033797, 1034297, 1034828, 1035329, 1035830, 1036362, 1036861, 1037393, 1037894, 1038394, 1038925, 1039427, 1039927, 1040459, 1040959, 1041491, 1041992, 1042492, 1043023, 1043524, 1044024, 1044556, 1045057, 1045558, 1046090, 1046590, 1047121, 1047622, 1048122, 1048654, 1049154, 1049655, 1050187, 1050689, 1051219, 1051720, 1052220, 1052751, 1053252, 1053752, 1054284, 1054786, 1055285, 1055817, 1056317, 1056849, 1057349, 1057850, 1058382, 1058883, 1059383, 1059915, 1060415, 1060947, 1061447, 1061947, 1062479, 1062981, 1063480, 1064012, 1064514, 1065014, 1065545, 1066045, 1066577, 1067078, 1067578, 1068110, 1068611, 1069112, 1069642, 1070142, 1070674, 1071175, 1071675, 1072207, 1072709, 1073209, 1073740, 1074241, 1074741, 1075273, 1075773, 1076305, 1076807, 1077308, 1077839, 1078340, 1078840, 1079372, 1079871, 1080403, 1080904};

    private LunarUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    private static int GetBitInt(int data, int length, int shift) {
        return (data & (1 << length) - 1 << shift) >> shift;
    }

    private static long SolarToInt(int y, int m, int d) {
        m = (m + 9) % 12;
        y -= m / 10;
        return (long)(365 * y + y / 4 - y / 100 + y / 400 + (m * 306 + 5) / 10 + (d - 1));
    }

    public static String lunarYearToGanZhi(int lunarYear) {
        String[] tianGan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
        String[] diZhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        return tianGan[(lunarYear - 4) % 10] + diZhi[(lunarYear - 4) % 12] + "年";
    }

    private static LunarUtils.Solar SolarFromInt(long g) {
        long y = (10000L * g + 14780L) / 3652425L;
        long ddd = g - (365L * y + y / 4L - y / 100L + y / 400L);
        if(ddd < 0L) {
            --y;
            ddd = g - (365L * y + y / 4L - y / 100L + y / 400L);
        }

        long mi = (100L * ddd + 52L) / 3060L;
        long mm = (mi + 2L) % 12L + 1L;
        y += (mi + 2L) / 12L;
        long dd = ddd - (mi * 306L + 5L) / 10L + 1L;
        LunarUtils.Solar solar = new LunarUtils.Solar();
        solar.solarYear = (int)y;
        solar.solarMonth = (int)mm;
        solar.solarDay = (int)dd;
        return solar;
    }

    public static LunarUtils.Solar LunarToSolar(LunarUtils.Lunar lunar) {
        int days = lunar_month_days[lunar.lunarYear - lunar_month_days[0]];
        int leap = GetBitInt(days, 4, 13);
        int offset = 0;
        int loopend = leap;
        if(!lunar.isLeap) {
            if(lunar.lunarMonth > leap && leap != 0) {
                loopend = lunar.lunarMonth;
            } else {
                loopend = lunar.lunarMonth - 1;
            }
        }

        int solar11;
        for(solar11 = 0; solar11 < loopend; ++solar11) {
            offset += GetBitInt(days, 1, 12 - solar11) == 1?30:29;
        }

        offset += lunar.lunarDay;
        solar11 = solar_1_1[lunar.lunarYear - solar_1_1[0]];
        int y = GetBitInt(solar11, 12, 9);
        int m = GetBitInt(solar11, 4, 5);
        int d = GetBitInt(solar11, 5, 0);
        return SolarFromInt(SolarToInt(y, m, d) + (long)offset - 1L);
    }

    public static LunarUtils.Lunar SolarToLunar(LunarUtils.Solar solar) {
        LunarUtils.Lunar lunar = new LunarUtils.Lunar();
        int index = solar.solarYear - solar_1_1[0];
        int data = solar.solarYear << 9 | solar.solarMonth << 5 | solar.solarDay;
        boolean solar11 = false;
        if(solar_1_1[index] > data) {
            --index;
        }

        int var17 = solar_1_1[index];
        int y = GetBitInt(var17, 12, 9);
        int m = GetBitInt(var17, 4, 5);
        int d = GetBitInt(var17, 5, 0);
        long offset = SolarToInt(solar.solarYear, solar.solarMonth, solar.solarDay) - SolarToInt(y, m, d);
        int days = lunar_month_days[index];
        int leap = GetBitInt(days, 4, 13);
        int lunarY = index + solar_1_1[0];
        int lunarM = 1;
        boolean lunarD = true;
        ++offset;

        for(int i = 0; i < 13; ++i) {
            int dm = GetBitInt(days, 1, 12 - i) == 1?30:29;
            if(offset <= (long)dm) {
                break;
            }

            ++lunarM;
            offset -= (long)dm;
        }

        int var18 = (int)offset;
        lunar.lunarYear = lunarY;
        lunar.lunarMonth = lunarM;
        lunar.isLeap = false;
        if(leap != 0 && lunarM > leap) {
            lunar.lunarMonth = lunarM - 1;
            if(lunarM == leap + 1) {
                lunar.isLeap = true;
            }
        }

        lunar.lunarDay = var18;
        return lunar;
    }

    public static class Solar {
        public int solarDay;
        public int solarMonth;
        public int solarYear;

        public Solar() {
        }
    }

    public static class Lunar {
        public boolean isLeap;
        public int lunarDay;
        public int lunarMonth;
        public int lunarYear;

        public Lunar() {
        }
    }
}
