package com.student;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
            new InetSocketAddress(8081), 0
        );

        // Display Registration Form
        server.createContext("/", exchange -> {

            byte[] response;

            try (var input = App.class.getResourceAsStream("/index.html")) {

                if (input == null) {
                    throw new IOException("index.html not found");
                }

                response = input.readAllBytes();
            }

            exchange.getResponseHeaders().set(
                "Content-Type", "text/html; charset=UTF-8"
            );

            exchange.sendResponseHeaders(200, response.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        });

        // Handle Form Submission
        server.createContext("/register", exchange -> {

            String message;

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                String formData = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
                );

                System.out.println("Registration Data: " + formData);

                message = "Student Registration Submitted Successfully!";

            } else {
                message = "Please submit the registration form.";
            }

            byte[] response = message.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                "Content-Type", "text/plain; charset=UTF-8"
            );

            exchange.sendResponseHeaders(200, response.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }

            exchange.close();
        });

        server.start();

        System.out.println("Server started successfully!");
        System.out.println("Open: http://localhost:8081");
    }
}