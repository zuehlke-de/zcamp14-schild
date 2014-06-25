//package com.zuehlke.camp2014.iot.brokers.schild.queue;
//
//import java.io.IOException;
//
//import org.apache.log4j.Logger;
//
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//
//public class MqQueue {
//    
//    private final ConnectionFactory factory;
//    private Connection connection = null;
//    private Channel channel = null;
//    
//    private final Logger log;
//    private final String queueName;
//    
//    public MqQueue(final String host, final String queue) {
//        log = Logger.getLogger(getClass());
//        queueName = queue;
//        factory = new ConnectionFactory();
//        factory.setHost(host);
//        connect();
//    }
//    
//    protected void connect() {
//        try {
//            connection = factory.newConnection();
//            channel = connection.createChannel();
//            channel.queueDeclare(queueName, true, false, false, null);
//        } catch (IOException e) {
//            log.error("Error connecting to queue " + queueName, e);
//        }
//    }
//    
//    public void close() {
//        if (channel != null) {
//            try {
//                channel.close();
//            } catch (IOException e) {
//                log.error("Error closing channel", e);
//            }
//        }
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (IOException e) {
//                log.error("Error closing connection", e);
//            }
//        }
//    }
//
//    /**
//     * @return the channel
//     */
//    protected final Channel getChannel() {
//        return channel;
//    }
//
//    /**
//     * @return the queueName
//     */
//    protected final String getQueueName() {
//        return queueName;
//    }
//
//    /**
//     * @return the log
//     */
//    protected final Logger getLog() {
//        return log;
//    }
//
//}
