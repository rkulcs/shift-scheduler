package shift.scheduler.backend.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import shift.scheduler.backend.dto.CompanyDashboardDataDTO;
import shift.scheduler.backend.dto.EmployeeDashboardDataDTO;
import shift.scheduler.backend.model.Employee;
import shift.scheduler.backend.model.Shift;
import shift.scheduler.backend.model.period.Day;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.util.builder.ScheduleForWeekBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DashboardServiceTest extends ServiceTest {

    @Mock
    ScheduleService scheduleService;

    @InjectMocks
    DashboardService dashboardService;

    @Nested
    class GetEmployeeDashboardData {

        static class TestCase {
            ScheduleForWeek schedule;
            LocalDate date;
            Shift expectedNextShift;
            int expectedNumShifts;
            int expectedNumHours;

            public TestCase(ScheduleForWeek schedule, LocalDate date, Shift expectedNextShift,
                            int expectedNumShifts, int expectedNumHours) {
                this.schedule = schedule;
                this.date = date;
                this.expectedNextShift = expectedNextShift;
                this.expectedNumShifts = expectedNumShifts;
                this.expectedNumHours = expectedNumHours;
            }
        }

        @Test
        void shouldReturnNullIfNoScheduleWasFound() {
            when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Optional.empty());
            assertNull(dashboardService.getEmployeeDashboardData(sampleEmployee));
        }

        @ParameterizedTest
        @MethodSource("createTestCases")
        void shouldReturnCorrectDataWithValidSchedule(TestCase testCase) {

            EmployeeDashboardDataDTO data;

            try (var localDateMock = mockStatic(LocalDate.class)) {
                localDateMock.when(LocalDate::now).thenReturn(testCase.date);
                when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Optional.of(testCase.schedule));

                data = dashboardService.getEmployeeDashboardData(sampleEmployee);
            }

            assertEquals(testCase.expectedNextShift, data.nextShift().shift());
            assertEquals(testCase.expectedNumShifts, data.numShifts());
            assertEquals(testCase.expectedNumHours, data.numHours());
        }

        static List<TestCase> createTestCases() {

            List<TestCase> cases = new ArrayList<>();

            var firstDay = LocalDate.of(2024, 10, 7);
            var shift1 = new Shift((short) 4, (short) 16, sampleEmployee);

            var schedule1 = new ScheduleForWeekBuilder()
                    .setCompany(sampleCompany)
                    .setFirstDay(firstDay)
                    .addShift(Day.MON, shift1)
                    .build();

            cases.add(new TestCase(schedule1, firstDay, shift1, 1, 12));

            // Set current date to be a Thursday
            var date2 = LocalDate.of(2024, 10, 10);

            // Next shift to take place on a Friday
            var shift2 = new Shift((short) 12, (short) 20, sampleEmployee);

            var schedule2 = new ScheduleForWeekBuilder()
                    .setCompany(sampleCompany)
                    .setFirstDay(firstDay)
                    .addShift(sampleEmployee, Day.MON, 4, 12)
                    .addShift(new Employee(), Day.MON, 4, 12)
                    .addShift(sampleEmployee, Day.TUE, 4, 16)
                    .addShift(Day.FRI, shift2)
                    .build();

            cases.add(new TestCase(schedule2, date2, shift2, 3, 28));

            return cases;
        }
    }

    @Nested
    class GetCompanyDashboardData {

        static class TestCase {
            ScheduleForWeek schedule;
            LocalDate queryDate;
            LocalDate nextDate;
            int startHour;
            int endHour;
            int numEmployees;
            int numHours;

            public TestCase(ScheduleForWeek schedule, LocalDate queryDate, LocalDate nextDate, int startHour, int endHour, int numEmployees, int numHours) {
                this.schedule = schedule;
                this.queryDate = queryDate;
                this.nextDate = nextDate;
                this.startHour = startHour;
                this.endHour = endHour;
                this.numEmployees = numEmployees;
                this.numHours = numHours;
            }
        }

        @Test
        void shouldReturnNullIfNoScheduleWasFound() {
            when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Optional.empty());
            assertNull(dashboardService.getCompanyDashboardData(sampleManager));
        }

        @ParameterizedTest
        @MethodSource("createTestCases")
        void shouldReturnCorrectDataWithValidSchedule(TestCase testCase) {
            CompanyDashboardDataDTO data;

            try (var localDateMock = mockStatic(LocalDate.class)) {
                localDateMock.when(LocalDate::now).thenReturn(testCase.queryDate);
                when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(Optional.of(testCase.schedule));

                data = dashboardService.getCompanyDashboardData(sampleManager);
            }

            assertEquals(testCase.nextDate, data.nextDay().date());
            assertEquals(testCase.startHour, data.nextDay().startHour());
            assertEquals(testCase.endHour, data.nextDay().endHour());
            assertEquals(testCase.numEmployees, data.numEmployees());
            assertEquals(testCase.numHours, data.totalHours());
        }

        static List<TestCase> createTestCases() {

            List<TestCase> cases = new ArrayList<>();

            LocalDate firstDay = LocalDate.of(2024, 10, 7);

            var schedule1 = new ScheduleForWeekBuilder()
                    .setFirstDay(firstDay)
                    .setCompany(sampleCompany)
                    .addShift(sampleEmployee, Day.MON, 4, 12)
                    .addShift(sampleEmployee, Day.TUE, 4, 12)
                    .addShift(sampleEmployee, Day.SUN, 4, 16)
                    .build();

            cases.add(new TestCase(schedule1, firstDay, firstDay, 4, 12, 1, 28));
            cases.add(new TestCase(schedule1, LocalDate.of(2024, 10, 12), LocalDate.of(2024, 10, 13), 4, 16, 1, 28));

            return cases;
        }
    }
}
