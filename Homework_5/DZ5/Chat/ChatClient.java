package org.example.DZ5.Chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DZ5.Json.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Object monitor = new Object();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Подключились к серверу...");

            System.out.println("Введите ваш логин:");
            String sender = console.readLine();
            out.println(sender);

            Thread listenerThread = new Thread(new ServerListener(in));
            listenerThread.start();

            while (true) {
                    System.out.println("Что хотите сделать?");
                    System.out.println("1. Послать сообщение другу");
                    System.out.println("2. Послать сообщение всем");
                    System.out.println("3. Получить список клиентов");
                    System.out.println("4. Выйти из чата");
                    String s = console.readLine();
                    int choice;
                    try {
                        choice = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный выбор, попробуйте снова.");
                        continue;
                    }
                    catch (Exception e) {
                        System.out.println("Неверный выбор, попробуйте снова.");
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            sendMessageToFriend(out, console, sender);
                            break;
                        case 2:
                            sendMessageToAll(out, console, sender);
                            break;
                        case 3:
                            getClientsList(out, console);
                            break;
                        case 4:
                            exitFromChat(out, sender);
                            return;
                        default:
                            System.out.println("Неверный выбор, попробуйте снова.");
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToFriend(PrintWriter out, BufferedReader console, String sender) throws JsonProcessingException {
        try {
            System.out.println("Введите логин получателя:");
            String recipient = console.readLine();

            System.out.println("Введите сообщение:");
            String message = console.readLine();

            Request request = new Request("send_to_friend", sender, recipient, message);
            out.println(objectMapper.writeValueAsString(request));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageToAll(PrintWriter out, BufferedReader console, String sender) throws JsonProcessingException {
        try {
            System.out.println("Введите сообщение для всех:");
            String message = console.readLine();

            Request request = new Request("send_to_all", sender, null, message);
            out.println(objectMapper.writeValueAsString(request));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getClientsList(PrintWriter out, BufferedReader in) throws JsonProcessingException {
        try {
            Request request = new Request("get_clients", null, null, null);
            out.println(objectMapper.writeValueAsString(request));

            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exitFromChat(PrintWriter out, String sender) throws JsonProcessingException {
        try {
            Request request = new Request("exit_chat", sender, null, "Пользователь:" + sender + ", отключился от чата.");
            out.println(objectMapper.writeValueAsString(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerListener implements Runnable {
        private BufferedReader in;

        public ServerListener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String response = in.readLine();
                    if (response != null) {
                        System.out.println("Ответ от сервера: " + response);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
