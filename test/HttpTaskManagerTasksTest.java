package com.yandex.tracker;

import com.yandex.tracker.model.Status;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskType;
import com.yandex.tracker.server.HttpTaskServer;
import com.yandex.tracker.service.InMemoryTaskManager;
import com.yandex.tracker.service.TaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

   private TaskManager manager;
   private  HttpTaskServer taskServer;
   private Gson gson;

    public HttpTaskManagerTasksTest() {

    }


    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = new GsonBuilder().registerTypeAdapter(Duration.class, new DurationAdapter()).create();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(1, TaskType.TASK, "Тестовая задача", Status.NEW, "Описание теста", Duration.ofMinutes(5), LocalDateTime.now());

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).header("Content-Type", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Тестовая задача", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }
}