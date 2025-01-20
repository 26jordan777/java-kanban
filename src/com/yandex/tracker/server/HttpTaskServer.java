package com.yandex.tracker.server;

import com.sun.net.httpserver.HttpServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

public class HttpTaskServer {
    private static final int PORT = 8080;
    public static TaskManager taskManager;
    public static Gson gson;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) {
        taskManager = manager;
        gson = new GsonBuilder().registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            System.out.println("Сервер запущен на порту " + PORT);

            server.createContext("/tasks", new TaskHandler());
            server.createContext("/subtasks", new SubtaskHandler());
            server.createContext("/epics", new EpicHandler());
            server.createContext("/history", new HistoryHandler());
            server.createContext("/prioritized", new PrioritizedHandler());

            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Сервер остановлен.");
        }
    }
}