package shift.scheduler.backend.model;

import org.junit.jupiter.api.Test;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.HoursOfOperation;
import shift.scheduler.backend.model.period.TimePeriod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HoursOfOperationTest {

    @Test
    void generatesAllBlocksInPeriod() {

        var period = new HoursOfOperation((short) 4, (short) 20, null, Day.MON);
        var blocks = (List<TimePeriod>) period.getTimeBlocks();
        assertThat(blocks).hasSize(4);
        assertThat((int) blocks.getFirst().getStartHour()).isEqualTo(4);
        assertThat((int) blocks.getFirst().getEndHour()).isEqualTo(8);
        assertThat((int) blocks.get(1).getStartHour()).isEqualTo(8);
        assertThat((int) blocks.get(1).getEndHour()).isEqualTo(12);
        assertThat((int) blocks.get(2).getStartHour()).isEqualTo(12);
        assertThat((int) blocks.get(2).getEndHour()).isEqualTo(16);
        assertThat((int) blocks.get(3).getStartHour()).isEqualTo(16);
        assertThat((int) blocks.get(3).getEndHour()).isEqualTo(20);
    }
}
