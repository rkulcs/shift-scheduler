package shift.scheduler.backend.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import shift.scheduler.backend.model.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTest {

    protected static final Company sampleCompany = new Company("Sample Company", "Sample City", new ArrayList<>());

    protected static final Employee sampleEmployee = new Employee(
            new Account("sampleEmployee", "Sample Employee", "password123"), sampleCompany , (short) 12, (short) 16, (short) 12, (short) 16
    );

    protected static final Manager sampleManager = new Manager(
            new Account("sampleManager", "Sample Manager", "password123")
    );

    protected static final List<User> sampleUsers = List.of(sampleManager, sampleEmployee);

    protected ModelMapper modelMapper = new ModelMapper().registerModule(new RecordModule());

    static List<User> getSampleUsers() {
        return sampleUsers;
    }
}
