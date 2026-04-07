package me.av306.chathook.webhook;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import me.av306.chathook.ChatHook;
import net.minecraft.server.level.ServerPlayer;

public enum WebhookSystem
{
    INSTANCE;
    private final ChatHook chatHook;
    private final HttpClient client;

    WebhookSystem()
    {
        this.chatHook = ChatHook.getInstance();
        this.client = HttpClient.newBuilder()
                .version( Version.HTTP_2 )
                .followRedirects( Redirect.NORMAL )
                .build();
    }

    public void sendMessage(ServerPlayer player, String message, boolean async ) {
        String username = "";
        String userIcon = "";
        if (player != null) {
            username = String.format("\"username\": \"%s\", ", player.getName().getString());
            userIcon = String.format("\"avatar_url\": \"https://visage.surgeplay.com/bust/%s\", ", player.getUUID());
        }

        URI uri;
        HttpRequest.Builder builder;
        try {
            uri = URI.create(chatHook.cm.getConfig("webhook_url"));
            builder = HttpRequest.newBuilder( uri );
        } catch (IllegalArgumentException | NullPointerException e) {
            chatHook.LOGGER.info("Invalid webhook url.");
            return;
        }

        // POST message to webhook
        HttpRequest request = builder.POST(
                    BodyPublishers.ofString( String.format(
                            "{" + username + userIcon + "\"content\": \"%s\"}",
                            message
                    ) )
                )
                .header( "Content-Type", "application/json" )
                .build();
        if (async) {
            this.client.sendAsync(request, BodyHandlers.ofString() ).thenAccept(this::response);
        } else {
            try {
                HttpResponse<String> response = this.client.send(request, responseInfo -> null);
                response(response);
            } catch (IOException | InterruptedException ignored) {}
        }
    }

    private void response(HttpResponse<String> response) {
        if ( response.statusCode() != 204 )
            chatHook.LOGGER.info(
                    "Received unexpected status code: {}; response body: {}",
                    response.statusCode(),
                    response.body()
            );
    }

    public void sendMessage(ServerPlayer player, String message ) {
        sendMessage(player, message, true);
    }
}
