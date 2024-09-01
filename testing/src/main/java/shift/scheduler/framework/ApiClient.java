package shift.scheduler.framework;

import com.beust.jcommander.Strings;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static shift.scheduler.util.Constants.*;

public class ApiClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<HashMap<String, Object>> mapperTypeRef = new TypeReference<>() {};

    private static final Map<Integer, String> DAYS = new HashMap<>()
    {{
        put(0, "MON");
        put(1, "TUE");
        put(2, "WED");
        put(3, "THU");
        put(4, "FRI");
        put(5, "SAT");
        put(6, "SUN");
    }};

    private static final CloseableHttpClient client;

    static {
        try {
            client = initClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String logIn(String username, String password) {

        String body = new JsonStringBuilder()
                .with("username", username)
                .with("password", password)
                .build();

        Request request = Request.post(USER_LOGIN_ENDPOINT)
                .addHeader("Content-Type", "application/json")
                .bodyString(body, ContentType.APPLICATION_JSON);

        var response = executeHttpRequest(request);

        if (assertResponseIsOk(response)) {
            var responseBody = extractResponseBody(response);

            if (responseBody == null)
                return null;

            return responseBody.split("\"")[3];
        } else {
            return null;
        }
    }

    public static boolean registerManager(String username, String name,
                                       String password, String companyName, String companyLocation) {

        String body = new JsonStringBuilder()
                .with("role", "MANAGER")
                .with("username", username)
                .with("name", name)
                .with("password", password)
                .with("company", new JsonStringBuilder()
                        .with("name", companyName)
                        .with("location", companyLocation)
                        .with("hoursOfOperation", new ArrayList<>())
                )
                .build();

        Request request = Request.post(USER_REGISTRATION_ENDPOINT)
                .addHeader("Content-Type", "application/json")
                .bodyString(body, ContentType.APPLICATION_JSON);

        return assertResponseIsOk(executeHttpRequest(request));
    }

    public static boolean registerEmployee(String username, String name, String password,
                                           String companyName, String companyLocation) {

        String body = new JsonStringBuilder()
                .with("role", "EMPLOYEE")
                .with("username", username)
                .with("name", name)
                .with("password", password)
                .with("company", new JsonStringBuilder()
                        .with("name", companyName)
                        .with("location", companyLocation)
                        .with("hoursOfOperation", new ArrayList<>())
                )
                .build();

        Request request = Request.post(USER_REGISTRATION_ENDPOINT)
                .addHeader("Content-Type", "application/json")
                .bodyString(body, ContentType.APPLICATION_JSON);

        return assertResponseIsOk(executeHttpRequest(request));
    }

    public static boolean deleteUser(String username) {

        Request request = Request.delete(String.format("%s/%s", USER_DELETION_ENDPOINT, username));

        return assertResponseIsOk(executeHttpRequest(request));
    }

    public static Map<String, Object> getCompany(String userToken) {

        Request request = Request.get(COMPANY_GET_ENDPOINT)
                .addHeader("Authorization", String.format("Bearer %s", userToken));

        var response = executeHttpRequest(request);
        var responseBody = extractResponseBody(response);

        try {
            return (responseBody == null) ? null : objectMapper.readValue(responseBody, mapperTypeRef);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean setHoursOfOperation(String managerToken, int companyId, List<List<Integer>> hoursOfOperation) {

        List<String> days = new ArrayList<>();

        for (int day = 0; day < hoursOfOperation.size(); day++) {
            List<Integer> hours = hoursOfOperation.get(day);

            if (hours == null)
                continue;

            days.add(new JsonStringBuilder()
                    .with("company", companyId)
                    .with("day", DAYS.get(day))
                    .with("startHour", hours.getFirst())
                    .with("endHour", hours.get(1))
                    .build());
        }

        String body = String.format("[%s]", Strings.join(",", days));

        Request request = Request.post(HOURS_OF_OPERATION_ENDPOINT)
                .addHeader("Authorization", String.format("Bearer %s", managerToken))
                .addHeader("Content-Type", "application/json")
                .bodyString(body, ContentType.APPLICATION_JSON);

        return assertResponseIsOk(executeHttpRequest(request));
    }

    private static HttpResponse executeHttpRequest(Request request) {

        try {
            return request.execute(client).returnResponse();
        } catch (IOException e) {
            return null;
        }
    }

    private static CloseableHttpClient initClient() throws Exception {

        TrustStrategy trustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();

        var connectionManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                        .build()
        );

        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    private static boolean assertResponseIsOk(HttpResponse response) {

        if (response == null)
            return false;

        return (response.getCode() == HttpStatus.SC_OK);
    }

    private static String extractResponseBody(HttpResponse response) {

        try {
            return EntityUtils.toString(((BasicClassicHttpResponse) response).getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
}
