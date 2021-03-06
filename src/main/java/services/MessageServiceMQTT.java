package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.Message;
import org.eclipse.paho.client.mqttv3.*;
import org.hibernate.service.spi.ServiceException;
import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alec on 10/18/16.
 */
public class MessageServiceMQTT implements MessageService {

    private IMqttClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Message> messages = new ArrayList<>();
    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    @Inject
    public MessageServiceMQTT(IMqttClient client) {
        try {
            this.client = client;
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
                wildcard = "/+";
            }
            try {
                client.subscribe(topic.concat(wildcard));
                addSubscriberToTopic(topic + wildcard, subscriber);
            } catch (MqttException me) {
                me.printStackTrace();
            }
        });
    }

    @Override
    public void publish(Message message) throws Exception {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            MqttMessage mqttMessage = new MqttMessage(objectMapper.writeValueAsString(message).getBytes());
            mqttMessage.setQos(2);
            client.publish(message.getTopic(), mqttMessage);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public Observable<Message> request(String responseTopic, Message message) throws Exception {
        return Observable.create((subscriber) -> {
            try {
                client.subscribe(responseTopic + "/+");
                addSubscriberToTopic(responseTopic + "/+", subscriber);
                publish(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        public void messageArrived(String mqttTopic, MqttMessage mqttMessage) {
            try {
                Message newMessage = objectMapper.readValue(mqttMessage.getPayload(), Message.class);
                messages.add(newMessage);
                String[] incomingTopic = mqttTopic.split("/");
                Observable.from(subscribers.entrySet())
                        .filter((entry) -> {
                            String[] topic = entry.getKey().split("/");
                            boolean include = true;
                            if (incomingTopic.length > topic.length) {
                                include = false;
                            }
                            else {
                                for (int i = 0; i < incomingTopic.length; i++) {
                                    if (!topic[i].equals("+") &&
                                        !topic[i].equals(incomingTopic[i])) {
                                        include = false;
                                    }
                                }
                            }

                            return include;
                        })
                        .subscribe((entry) -> {
                            for (Subscriber subscriber : entry.getValue()) {
                                subscriber.onNext(newMessage);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }
}
