package com.yandex.tracker.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.tracker.model.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                handleGetTasks(exchange);
                break;
            case "POST":
                handleCreateTask(exchange);
                break;
            case "DELETE":
                handleDeleteTask(exchange);
                break;
            default:
                sendText(exchange, "Method Not Allowed");
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = HttpTaskServer.taskManager.getAllTasks();
        String jsonResponse = HttpTaskServer.gson.toJson(tasks);
        sendText(exchange, jsonResponse);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        Task task = HttpTaskServer.gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Task.class);
        HttpTaskServer.taskManager.createTask(task);
        sendText(exchange, "{\"message\":\"Task created successfully\"}");
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.split("=")[1]);
            HttpTaskServer.taskManager.deletedTask(id);
            sendText(exchange, "{\"message\":\"Task deleted successfully\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}