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
            jsonWriter.value(duration.getSeconds());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        long seconds = jsonReader.nextLong();
        return Duration.ofMinutes(seconds);
    }

    public class MyDuration {
        private long seconds;

        public MyDuration(long seconds) {
            this.seconds = seconds;
        }

        public Duration toDuration() {
            return Duration.ofSeconds(seconds);
        }

        public MyDuration fromDuration(Duration duration) {
            return new MyDuration(duration.getSeconds());
        }
    }
}