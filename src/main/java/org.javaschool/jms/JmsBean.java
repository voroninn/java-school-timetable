package org.javaschool.jms;

import lombok.extern.log4j.Log4j2;
import org.javaschool.service.TimetableService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;


@Singleton
@Startup
@Log4j2
public class JmsBean {

    @EJB
    private TimetableService timetableService;

    private QueueConnection connection;
    private QueueSession session;
    private QueueReceiver receiver;

    @PostConstruct
    private void startConnection() throws NamingException, JMSException {
        log.info("Initiating JMS connection");
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put("java.naming.provider.url", "tcp://localhost:61616");
        properties.put("queue.js-queue", "TimetableQueue");
        properties.put("connectionFactoryNames", "queueCF");

        Context context = new InitialContext(properties);
        QueueConnectionFactory connectionFactory = (QueueConnectionFactory) context.lookup("queueCF");
        Queue queue = (Queue) context.lookup("js-queue");
        connection = connectionFactory.createQueueConnection();
        connection.start();
        session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        receiver = session.createReceiver(queue);
        MessageDrivenBean listener = new MessageDrivenBean();
        listener.setTimetableService(timetableService);
        receiver.setMessageListener(listener);
        log.info("Connected to JMS server");
    }

    @PreDestroy
    private void closeConnection(){
        try{
            receiver.close();
            session.close();
            connection.close();
            log.info("JMS connection closed");
        }
        catch(JMSException e){
            e.printStackTrace();
        }
    }
}