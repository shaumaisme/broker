package com.sacty.broker;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathConfig;
import io.moquette.server.config.IConfig;

public class Main {
    static class PublisherListener extends AbstractInterceptHandler {
        @Override
        public void onPublish(InterceptPublishMessage message) {
            System.out.println("From:"+message.getClientID()+" To: " + message.getTopicName()+ ", Content: " + new String(message.getPayload().array()));
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        final IConfig classPathConfig = new ClasspathConfig();
        final Server mqttBroker = new Server();
        final List<? extends InterceptHandler> userHandlers = Arrays.asList(new PublisherListener());
        mqttBroker.startServer(classPathConfig, userHandlers);

        System.out.println("Broker Started");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Stopping Broker");
                mqttBroker.stopServer();
                System.out.println("Broker Stopped");
            }
        });

        Thread.sleep(4000);
    }
}