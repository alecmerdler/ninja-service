package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Message;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.hibernate.service.spi.ServiceException;
import rx.Observable;
import rx.Subscriber;

import java.util.*;

/**
 * Created by alec on 10/18/16.
 */
public class MessageServiceMQTT implements MessageService {

    // TODO: Move to configuration file
    private final String brokerUrl = "tcp://52.25.184.170:1884";
    private final String clientId = UUID.randomUUID().toString();
    private IMqttClient client;
    private final MemoryPersistence persistence;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Message> messages = new ArrayList<>();
    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    public MessageServiceMQTT() {
        this.persistence = new MemoryPersistence();
        try {
            this.client = new MqttClient(brokerUrl, clientId, persistence);
            this.client.setCallback(new SubscribeCallback());
            this.client.connect();
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public Observable<List<Message>> getMessages() throws Exception {
        return Observable.just(messages);
    }

    @Override
    public Observable<Message> subscribe(String topic, boolean subscribeToAll) {
        return Observable.create((subscriber) -> {
            String wildcard = "";
            if (subscribeToAll) {
                wildcard = "+";
            }
            try {
                client.subscribe(topic + "/" + wildcard);
                addSubscriberToTopic(topic, subscriber);
            } catch (MqttException me) {
                me.printStackTrace();
            }
        });
    }

    @Override
    public void sendMessage(Message message) throws Exception {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            System.out.println(message.getTopic());
            client.publish(message.getTopic(), new MqttMessage(objectMapper.writeValueAsString(message).getBytes()));
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public void stop() throws ServiceException {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException me) {
            throw new ServiceException(me.getMessage());
        }
    }

    private void addSubscriberToTopic(String topic, Subscriber subscriber) {
        if (subscribers.containsKey(topic)) {
            subscribers.get(topic).add(subscriber);
        }
        else {
            List<Subscriber> newSubscriberList = new ArrayList<>();
            newSubscriberList.add(subscriber);
            subscribers.put(topic, newSubscriberList);
        }
    }

    private class SubscribeCallback implements MqttCallback {

        private final ObjectMapper objectMapper;

        SubscribeCallback() {
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("Connection to MQTT broker lost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) {
            try {
                Message message = objectMapper.readValue(mqttMessage.getPayload(), Message.class);
                messages.add(message);
                for (Subscriber subscriber : subscribers.get(message.getResourceName())) {
                    subscriber.onNext(message);
                }
                System.out.println(messages);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }
}
