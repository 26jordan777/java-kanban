package com.yandex.tracker.server;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
        return context.serialize(duration.getSeconds());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) {
        long seconds = json.getAsLong();
        return Duration.ofSeconds(seconds);
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