package shift.scheduler.framework;

import java.util.List;

public class JsonStringBuilder {

    private final StringBuilder builder = new StringBuilder();
    private boolean hasFields = false;

    public JsonStringBuilder() {
        builder.append("{");
    }

    private void addCommaIfNeeded() {

        if (hasFields)
            builder.append(",");
        else
            hasFields = true;
    }

    public JsonStringBuilder with(String key, String value) {

        addCommaIfNeeded();

        builder.append(String.format("\"%s\": \"%s\"", key, value));
        return this;
    }

    public JsonStringBuilder with(String key, int value) {

        addCommaIfNeeded();

        builder.append(String.format("\"%s\": \"%d\"", key, value));
        return this;
    }

    public JsonStringBuilder with(String key, List<String> value) {

        addCommaIfNeeded();

        builder.append(String.format("\"%s\": %s", key, value.toString()));
        return this;
    }

    public JsonStringBuilder with(String key, JsonStringBuilder value) {

        addCommaIfNeeded();

        builder.append(String.format("\"%s\": %s", key, value.build()));
        return this;
    }

    public String build() {

        builder.append("}");
        return builder.toString();
    }
}
