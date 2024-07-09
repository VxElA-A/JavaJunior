package org.example.DZ5.Chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DZ5.Json.Request;
import org.example.DZ5.Json.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String login;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {

            String initialLogin = in.readLine();
            if (isLoginTaken(initialLogin)) {
                out.println("Ошибка: Логин '" + initialLogin + "' уже используется.");
                return;
            } else {
                this.login = initialLogin;
                System.out.println("Пользователь подключился: " + login);
                clients.put(login, this);
            }

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено сообщение: " + inputLine);
                Request request = objectMapper.readValue(inputLine, Request.class);
                handleRequest(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(login);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isLoginTaken(String loginToCheck) {
        return clients.containsKey(loginToCheck);
    }

    private void handleRequest(Request request) throws IOException {
        switch (request.getType()) {
            case "send_to_friend":
                sendMessageToFriend(request.getRecipient(), request.getMessage());
                break;
            case "send_to_all":
                sendToAll(request.getSender(), request.getMessage());
                break;
            case "get_clients":
                sendClientsList();
                break;
            case "exit_chat":
                exitChat(request.getSender());
                break;
            default:
                System.out.println("Неизвестный тип запроса: " + request.getType());
        }
    }

    private void sendMessageToFriend(String recipient, String message) {
        ClientHandler recipientClient = clients.get(recipient);
        if (recipientClient != null) {
            recipientClient.out.println("Сообщение от " + login + ": " + message);
        } else {
            out.println("Ошибка: Пользователь с логином '" + recipient + "' не найден.");
        }
    }

    private void sendToAll(String sender, String message) {
        for (ClientHandler client : clients.values()) {
            if (!client.login.equals(sender)) {
                client.out.println("Сообщение всем от " + login + ": " + message);
            }
        }
    }

    private void sendClientsList() throws IOException {
        Set<String> clientLogins = clients.keySet();
        Response response = new Response(clientLogins);
        out.println(objectMapper.writeValueAsString(response));
    }

    private void exitChat(String sender) {
        ClientHandler senderClient = clients.get(sender);
        if (senderClient != null) {
            clients.remove(sender);
            for (ClientHandler client : clients.values()) {
                client.out.println("Пользователь " + sender + " отключился от чата.");
            }
        }
    }
}
