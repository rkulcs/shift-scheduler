package shift.scheduler.backend.model;

import org.junit.jupiter.api.Test;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.period.HoursOfOperation;
import shift.scheduler.backend.model.period.TimeInterval;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HoursOfOperationTest {

    @Test
    void generatesAllBlocksInPeriod() {

        var period = new HoursOfOperation((short) 4, (short) 20, null, Day.MON);
        var blocks = (List<TimeInterval>) period.getTimeBlocks();
        assertThat(blocks).hasSize(4);
        assertThat((int) blocks.getFirst().getStart()).isEqualTo(4);
        assertThat((int) blocks.getFirst().getEnd()).isEqualTo(8);
        assertThat((int) blocks.get(1).getStart()).isEqualTo(8);
        assertThat((int) blocks.get(1).getEnd()).isEqualTo(12);
        assertThat((int) blocks.get(2).getStart()).isEqualTo(12);
        assertThat((int) blocks.get(2).getEnd()).isEqualTo(16);
        assertThat((int) blocks.get(3).getStart()).isEqualTo(16);
        assertThat((int) blocks.get(3).getEnd()).isEqualTo(20);
    }
}
