package shift.scheduler.backend.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Miscellaneous beans.
 */
@Configuration
public class BeanConfig {

    /**
     * Mapper for mapping between DTOs and entities. Includes the record module
     * to provide support for records.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper().registerModule(new RecordModule());
    }
}
