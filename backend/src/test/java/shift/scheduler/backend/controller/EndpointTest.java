package shift.scheduler.backend.controller;

/**
 * Should be implemented by all nested test classes in a controller test class.
 */
public interface EndpointTest {
    // Should return the API endpoint that is tested by the implementer
    String endpoint();
}
