package shift.scheduler.framework;

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
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.ArrayList;

import static shift.scheduler.util.Constants.*;

public class ApiClient {

    private static final CloseableHttpClient client;

    static {
        try {
            client = initClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean registerManager(String username, String name,
                                       String password, String companyName, String companyLocation) {

        String body = new JsonStringBuilder()
                .with("role", "MANAGER")
                .with("manager", username)
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

        return executeHttpRequest(request, HttpStatus.SC_OK);
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

        return executeHttpRequest(request, HttpStatus.SC_OK);
    }

    public static boolean deleteUser(String username) {

        Request request = Request.delete(String.format("%s/%s", USER_DELETION_ENDPOINT, username));

        return executeHttpRequest(request, HttpStatus.SC_OK);
    }

    private static boolean executeHttpRequest(Request request, int expectedStatusCode) {

        try {
            HttpResponse response = request.execute(client).returnResponse();

            return (response.getCode() == expectedStatusCode);
        } catch (IOException e) {
            return false;
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
}
