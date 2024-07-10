package shift.scheduler.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import shift.scheduler.backend.model.*;
import shift.scheduler.backend.model.schedule.ScheduleForDay;
import shift.scheduler.backend.model.schedule.ScheduleForWeek;
import shift.scheduler.backend.service.AuthenticationService;
import shift.scheduler.backend.service.CompanyService;
import shift.scheduler.backend.service.ScheduleService;
import shift.scheduler.backend.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class CompanyControllerTest extends ControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void getAllShouldReturnNoContentIfCompanyListIsNull() throws Exception {

        when(companyService.findAll()).thenReturn(null);

        mockMvc.perform(get("/company/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllShouldReturnNoContentIfCompanyListIsEmpty() throws Exception {

        when(companyService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/company/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllShouldReturnOkIfCompanyListIsNonempty() throws Exception {

        when(companyService.findAll()).thenReturn(List.of(Util.company));

        mockMvc.perform(get("/company/all"))
                .andExpect(status().isOk());
    }

    @Test
    void getShouldReturnBadRequestWithInvalidUser() throws Exception {

        when(authenticationService.getUserFromHeader(any())).thenReturn(null);

        mockMvc.perform(get("/company"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getShouldReturnOkWithValidUser() throws Exception {

        Manager manager = Util.createValidManager();

        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);

        mockMvc.perform(get("/company").header("Authorization", ""))
                .andExpect(status().isOk());
    }

    @Test
    void getShouldReturnNoContentIfUserHasNoCompany() throws Exception {

        Manager manager = mock(Manager.class);

        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
        when(manager.getCompany()).thenReturn(null);

        mockMvc.perform(get("/company").header("Authorization", ""))
                .andExpect(status().isNoContent());
    }

    @Test
    void setHoursShouldReturnBadRequestWithInvalidManager() throws Exception {

        Company company = mock(Company.class);
        when(authenticationService.getUserFromHeader(any())).thenReturn(null);

        mockMvc.perform(postJson("/company/hours", createHoursOfOperationRequest(company)).header("Authorization", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setHoursShouldReturnBadRequestWithInvalidHours() throws Exception {

        Manager manager = Util.createValidManager();

        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);

        Collection<HoursOfOperation> hours = new ArrayList<>();
        hours.add(new HoursOfOperation((short) 0, (short) 60, manager.getCompany(), Day.MON));

        mockMvc.perform(postJson("/company/hours", stringify(hours)).header("Authorization", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setHoursShouldReturnOkWithValidInput() throws Exception {

        Manager manager = Util.createValidManager();

        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
        when(companyService.save(any())).thenReturn(manager.getCompany());

        mockMvc.perform(
                        postJson("/company/hours", createHoursOfOperationRequest(manager.getCompany()))
                                .header("Authorization", "")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getDashboardShouldReturnBadRequestWithInvalidUser() throws Exception {

        when(authenticationService.getUserFromHeader(any())).thenReturn(null);

        mockMvc.perform(get("/company/dashboard"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDashboardShouldReturnOkWithValidUserAndExistingSchedule() throws Exception {

        Manager manager = Util.createValidManager();
        ScheduleForWeek schedule = mock(ScheduleForWeek.class);

        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
        when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(schedule);
        when(schedule.getDailySchedules()).thenReturn(
                List.of(new ScheduleForDay(Day.MON, List.of(new Shift((short) 0, (short) 8, new Employee()))))
        );

        mockMvc.perform(get("/company/dashboard").header("Authorization", ""))
                .andExpect(status().isOk());
    }

    @Test
    void getDashboardShouldReturnNoContentWithValidUserAndNonexistentSchedule() throws Exception {

        Manager manager = Util.createValidManager();

        when(authenticationService.getUserFromHeader(any())).thenReturn(manager);
        when(scheduleService.findByCompanyAndDate(any(), any())).thenReturn(null);

        mockMvc.perform(get("/company/dashboard").header("Authorization", ""))
                .andExpect(status().isNoContent());
    }

    private String createHoursOfOperationRequest(Company company) throws JsonProcessingException {

        Collection<HoursOfOperation> hours = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            hours.add(new HoursOfOperation((short) 0, (short) (16 + 4*(i%2)), company, Day.MON));
        }

        return stringify(hours);
    }
}
