package sal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link DateTimeParser} parsing of supported date/time formats and storage/display formatting.
 */
public class DateTimeParserTest {
    @Test
    public void parse_isoDate_returnsDateOnly() {
        TaskDateTime dateTime = DateTimeParser.parse("2019-10-15");
        assertFalse(dateTime.hasTime());
        assertEquals(LocalDate.of(2019, 10, 15), dateTime.getValue().toLocalDate());
        assertEquals(0, dateTime.getValue().getHour());
        assertEquals(0, dateTime.getValue().getMinute());
    }

    @Test
    public void parse_slashDate_returnsDateOnly() {
        TaskDateTime dateTime = DateTimeParser.parse("2/12/2019");
        assertFalse(dateTime.hasTime());
        assertEquals(LocalDate.of(2019, 12, 2), dateTime.getValue().toLocalDate());
    }

    @Test
    public void parse_paddedSlashDate_returnsDateOnly() {
        TaskDateTime dateTime = DateTimeParser.parse("02/12/2019");
        assertFalse(dateTime.hasTime());
        assertEquals(LocalDate.of(2019, 12, 2), dateTime.getValue().toLocalDate());
    }

    @Test
    public void parse_isoDateTime_returnsDateTime() {
        TaskDateTime dateTime = DateTimeParser.parse("2019-10-15 1800");
        assertTrue(dateTime.hasTime());
        assertEquals(LocalDateTime.of(2019, 10, 15, 18, 0), dateTime.getValue());
    }

    @Test
    public void parse_slashDateTime_returnsDateTime() {
        TaskDateTime dateTime = DateTimeParser.parse("2/12/2019 1800");
        assertTrue(dateTime.hasTime());
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), dateTime.getValue());
    }

    @Test
    public void parse_midnightDateTime_keepsExplicitTime() {
        TaskDateTime dateTime = DateTimeParser.parse("2019-10-15 0000");
        assertTrue(dateTime.hasTime());
        assertEquals(LocalDateTime.of(2019, 10, 15, 0, 0), dateTime.getValue());
    }

    @Test
    public void parse_surroundingWhitespace_trimsInput() {
        TaskDateTime dateTime = DateTimeParser.parse("  2019-10-15  ");
        assertFalse(dateTime.hasTime());
        assertEquals(LocalDate.of(2019, 10, 15), dateTime.getValue().toLocalDate());
    }

    @Test
    public void parse_unsupportedFormat_exceptionThrown() {
        assertThrows(DateTimeParseException.class, () -> DateTimeParser.parse("15-10-2019"));
        assertThrows(DateTimeParseException.class, () -> DateTimeParser.parse("2019/10/15"));
        assertThrows(DateTimeParseException.class, () -> DateTimeParser.parse("not a date"));
        assertThrows(DateTimeParseException.class, () -> DateTimeParser.parse("2/12/2019 6pm"));
    }

    @Test
    public void formatForStorage_dateOnly_usesIsoDate() {
        TaskDateTime dateTime = DateTimeParser.parse("2/12/2019");
        assertEquals("2019-12-02", DateTimeParser.formatForStorage(dateTime));
    }

    @Test
    public void formatForStorage_dateTime_usesIsoDateAndTime() {
        TaskDateTime dateTime = DateTimeParser.parse("2/12/2019 1800");
        assertEquals("2019-12-02 1800", DateTimeParser.formatForStorage(dateTime));
    }

    @Test
    public void formatForStorage_midnightDateTime_keepsTimeComponent() {
        TaskDateTime dateTime = DateTimeParser.parse("2019-10-15 0000");
        assertEquals("2019-10-15 0000", DateTimeParser.formatForStorage(dateTime));
    }

    @Test
    public void formatForDisplay_dateOnly_doesNotIncludeTime() {
        TaskDateTime dateTime = DateTimeParser.parse("2019-10-15");
        String display = DateTimeParser.formatForDisplay(dateTime);
        assertFalse(display.contains(":"));
        assertTrue(display.contains("2019"));
    }

    @Test
    public void formatForDisplay_dateTime_includesTime() {
        TaskDateTime dateTime = DateTimeParser.parse("2/12/2019 1800");
        String display = DateTimeParser.formatForDisplay(dateTime);
        assertTrue(display.contains(":"));
        assertTrue(display.contains("2019"));
    }
}
