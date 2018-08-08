package cn.mccraft.pangu.core.test.utils;

import cn.mccraft.pangu.core.util.date.ChineseTraditionalFestival;
import cn.mccraft.pangu.core.util.date.LunarCalendar;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class FestivalTest {
    @Test
    public void lunarCalendar() {
        assertNotNull(LunarCalendar.getInstance());

        LunarCalendar lunarCalendar = LunarCalendar.getInstance(2018, Calendar.AUGUST, 4);

        assertEquals("狗", lunarCalendar.animalsYear());
        assertEquals(2018, lunarCalendar.getYear());
        assertEquals(6, lunarCalendar.getMonth());
        assertEquals(23, lunarCalendar.getDay());
        assertEquals("戊戌", lunarCalendar.cyclical());
    }
    @Test
    public void chineseTraditionalFestival() {
        ChineseTraditionalFestival.today();

        assertSame(ChineseTraditionalFestival.valueOf(LunarCalendar.getInstance(2024, Calendar.FEBRUARY, 10)), ChineseTraditionalFestival.SPRING);
    }
}
