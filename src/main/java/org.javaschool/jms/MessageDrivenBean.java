package org.javaschool.jms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.javaschool.service.TimetableService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDrivenBean implements MessageListener {

    private TimetableService timetableService;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                log.info("Received message: " + ((TextMessage) message).getText());
                timetableService.updateSchedules();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}