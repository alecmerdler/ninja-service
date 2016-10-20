package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import rx.Observable;
import rx.Subscriber;

import java.util.Map;
import java.util.UUID;

/**
 * Created by alec on 10/18/16.
 */
public class MessageServiceMQTT implements MessageService {

    // TODO: Move to configuration file
    private final String brokerUrl = "tcp://52.25.184.170:1884";
    private final String clientId = UUID.randomUUID().toString();
    private IMqttClient client;
    private MemoryPersistence persistence;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public MessageServiceMQTT(IMqttClient client, MemoryPersistence persistence) {
        this.client = client;
        this.persistence = persistence;
    }

    public MessageServiceMQTT() {
        this.persistence = new MemoryPersistence();
        try {
            this.client = new MqttClient(brokerUrl, clientId, persistence);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    public Observable<Map<String, Object>> getMessages() throws Exception {
        final String topic = "test";
        return Observable.create((subscriber) -> {
            try {
                client.setCallback(new SubscribeCallback(subscriber));
                client.connect();
                client.subscribe(topic);
            } catch (MqttException me) {
                me.printStackTrace();
            }
        });
    }

    public void sendMessage(String topic, Map<String, Object> message) throws Exception {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            client.publish(topic, new MqttMessage(formatMessage(message).getBytes()));
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    private String formatMessage(Map<String, Object> message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }

    private class SubscribeCallback implements MqttCallback {

        private Subscriber<? super Map<String, Object>> subscriber;
        private ObjectMapper objectMapper;

        SubscribeCallback(Subscriber<? super Map<String, Object>> subscriber) {
            this.subscriber = subscriber;
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            try {
                byte[] bytes = message.getPayload();
                Map<String, Object> messageMap = objectMapper.readValue(message.getPayload(), new TypeReference<Map<String, Object>>(){});
                subscriber.onNext(messageMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }


}
