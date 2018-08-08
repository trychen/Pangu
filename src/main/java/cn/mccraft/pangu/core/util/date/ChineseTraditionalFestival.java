package cn.mccraft.pangu.core.util.date;

import javax.annotation.Nullable;
import java.util.Calendar;

/**
 * 中国传统节日
 *
 * @author trychen
 * @since 1.0.0.5
 */
public enum ChineseTraditionalFestival {
    /**
     * 春节
     * 农历一月一日
     */
    SPRING(1, 1),

    /**
     * 元宵节
     * 农历一月十五日
     */
    LANTERN(1, 15),

    /**
     * 清明节
     * 公历4月4或5或6日
     */
    TOMB_SWEEPING(-1, -1) {
        final double[] COEFFICIENT = { 5.15, 5.37, 5.59, 4.82, 5.02, 5.26, 5.48, 4.70, 4.92, 5.135, 5.36, 4.60, 4.81, 5.04, 5.26 };

        @Override
        public boolean isToday(LunarCalendar calendar) {
            if (Calendar.getInstance().get(Calendar.MONTH) != Calendar.WEDNESDAY) return false;
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int date = Calendar.getInstance().get(Calendar.DATE);
            if (year == 2232) {
                return date == 4;
            }
            int mod = year % 100;
            return date == (int) (mod * 0.2422 + COEFFICIENT[year / 100 - 17] - mod / 4);
        }
    },

    /**
     * 端午节
     * 农历五月初五
     */
    DRAGON_BOAT(5, 5),

    /**
     * 中秋节
     * 农历八月十五
     */
    MID_AUTUMN(8, 15),

    /**
     * 重阳节
     * 农历九月九日
     */
    DOUBLE_NINTH(9, 9),

    /**
     * 除夕
     * 农历十二月三十日
     */
    NEW_YEARS_EVE(12, 30);

    public final int month, day;

    /**
     *
     * @param month lunar month
     * @param day lunar day
     */
    ChineseTraditionalFestival(int month, int day) {
        this.month = month;
        this.day = day;
    }

    public boolean isToday() {
        return isToday(LunarCalendar.getInstance());
    }

    public boolean isToday(LunarCalendar calendar) {
        return calendar.getMonth() == this.month && calendar.getDay() == this.day;
    }

    /**
     * find today's festive
     * @return null if isn't festive
     */
    @Nullable
    public static ChineseTraditionalFestival today() {
        for (ChineseTraditionalFestival festival : values()) {
            if (festival.isToday()) return festival;
        }
        return null;
    }
    /**
     * find festive from lunar calendar
     * @return null if isn't festive
     */
    @Nullable
    public static ChineseTraditionalFestival valueOf(LunarCalendar calendar) {
        for (ChineseTraditionalFestival festival : values()) {
            if (festival.isToday(calendar)) return festival;
        }
        return null;
    }
}
