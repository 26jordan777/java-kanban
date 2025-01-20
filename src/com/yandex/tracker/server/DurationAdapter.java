package com.yandex.tracker.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null || duration.isZero()) {
            jsonWriter.nullValue();
        } else {
            long durationInMinutes = duration.toMinutes();
            jsonWriter.value(durationInMinutes);
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        long minutes = jsonReader.nextLong();
        return Duration.ofMinutes(minutes);
    }
}