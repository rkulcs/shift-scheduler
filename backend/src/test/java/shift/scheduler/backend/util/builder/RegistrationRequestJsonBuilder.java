package shift.scheduler.backend.util.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shift.scheduler.backend.model.Role;

import java.util.HashMap;
import java.util.Map;

public class RegistrationRequestJsonBuilder implements Builder<String> {

    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, Object> requestBody = new HashMap<>();

    private Map<String, String> accountDetails = null;
    private Map<String, String> companyDetails = null;

    private Map<String, String> getAccountDetails() {

        if (accountDetails == null)
            accountDetails = new HashMap<>();

        return accountDetails;
    }

    private Map<String, String> getCompanyDetails() {

        if (companyDetails == null)
            companyDetails = new HashMap<>();

        return companyDetails;
    }

    public RegistrationRequestJsonBuilder setUsername(String username) {
        getAccountDetails().put("username", username);
        return this;
    }

    public RegistrationRequestJsonBuilder setName(String name) {
        getAccountDetails().put("name", name);
        return this;
    }

    public RegistrationRequestJsonBuilder setPassword(String password) {
        getAccountDetails().put("password", password);
        return this;
    }

    public RegistrationRequestJsonBuilder setRole(Role role) {
        getAccountDetails().put("role", role.name());
        return this;
    }

    public RegistrationRequestJsonBuilder setCompanyName(String name) {
        getCompanyDetails().put("name", name);
        return this;
    }

    public RegistrationRequestJsonBuilder setCompanyLocation(String location) {
        getCompanyDetails().put("location", location);
        return this;
    }

    @Override
    public String build() {

        if (accountDetails != null)
            requestBody.put("account", accountDetails);

        if (companyDetails != null)
            requestBody.put("company", companyDetails);

        try {
            return mapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
