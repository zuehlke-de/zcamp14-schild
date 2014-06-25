//package com.zuehlke.camp2014.iot.brokers.schild.queue;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//
//import com.zuehlke.camp2014.iot.model.runtime.Message;
//
//public class MqSender extends MqQueue {
//
//    public MqSender(final String hostName, final String queue) {
//        super(hostName, queue);
//    }
//
//    public void sendMessage(Message message) throws IOException {
//    	byte[] bytes; 
//    	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//    	ObjectOutputStream oos = new ObjectOutputStream(baos); 
//    	oos.writeObject(message); 
//    	oos.flush();
//    	oos.reset();
//    	bytes = baos.toByteArray();
//    	oos.close();
//    	baos.close();
//    	
//    	getChannel().basicPublish("", getQueueName(), null, bytes);
//    	
//    	getLog().info(" [x] Sent '" + message + "'");
//    }
//}
