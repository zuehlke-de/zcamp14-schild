//package com.zuehlke.camp2014.iot.brokers.schild.queue;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.rabbitmq.client.ConsumerCancelledException;
//import com.rabbitmq.client.QueueingConsumer;
//import com.rabbitmq.client.QueueingConsumer.Delivery;
//import com.rabbitmq.client.ShutdownSignalException;
//import com.zuehlke.camp2014.iot.model.runtime.Message;
//
//public class MqListener extends MqQueue  {
//    
//    private final List<MessageHandler> messageHandlers;
//
//    public MqListener(final String host, final String queueName) {
//        super(host, queueName);
//        messageHandlers = new ArrayList<MessageHandler>();
//        
//        try {
//            getLog().info("Binding exchange to queue " + getQueueName());
//            getChannel().queueBind(getQueueName(), "amq.topic", "iot");
//        } catch (IOException e) {
//            getLog().error("Error setting up exchange", e);
//        }
//    }
//
//    public void receive() throws IOException {
//        QueueingConsumer consumer = new QueueingConsumer(getChannel());
//        String consumerName = getChannel().basicConsume(getQueueName(), true, consumer);
//        
//        getLog().info("Consumer " + consumerName + " is listening...");
//
//        Delivery delivery;
//        while (true) {
//            try {
//                delivery = consumer.nextDelivery();
//                Message message = MqttMessageFormat.formatDelivery(delivery);
//                deliver(message);
//            } catch (ShutdownSignalException | ConsumerCancelledException | InterruptedException e) {
//                throw new IOException("Error reading from queue " + getQueueName(), e);
//            }
//        }
//    }
//    
//    public String receiveSingleMessage() throws IOException {
//        String message = new String(getChannel().basicGet(getQueueName(), true).getBody());
//        getLog().info(" [x] Received '" + message + "'");
//        return message;
//    }
//    
//    private void deliver(final Message message) {
//        for (MessageHandler handler : messageHandlers) {
//            handler.handleMessage(message);
//        }
//    }
//
//    public void registerHandler(final MessageHandler handler) {
//        messageHandlers.add(handler);
//    }
//    
//    public void removeHandler(final MessageHandler handler) {
//        messageHandlers.remove(handler);
//    }
//
//    /* MqttListener 
//    @Override
//    public void connectionLost(Throwable cause) {
//        getLog().error("Connection lost", cause);
//    }
//
//    @Override
//    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        getLog().info("Message arrived [" + topic + "]");
//        deliver(MqttMessageFormat.formatMqttMessage(message));
//    }
//
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//        getLog().info("Delivery Complete [" + token.getMessageId() + "]");
//    }
//    */
//
//}
