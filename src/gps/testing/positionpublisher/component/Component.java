package gps.testing.positionpublisher.component;

import java.util.Properties;
import no.ntnu.item.arctis.runtime.Block;
import com.bitreactive.examples.amqp.Constants;
import com.bitreactive.library.amqp.AmqpError;
import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.AmqpMessage.SendProperty;
import com.bitreactive.library.amqp.Utils;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient;

public class Component extends Block {
	private String host, queueName, subject = "Subject";
	private int port = Utils.DEFAULT_AMQP_PORT;

	public String verifyConfig(Properties p) {
		StringBuffer sb = new StringBuffer();
		if (p.containsKey(Constants.P_AMQP_BROKER_HOST)) {
			host = p.getProperty(Constants.P_AMQP_BROKER_HOST);
		} else {
			sb.append("Missing property AMQP-Broker-Host!\n");
		}
		if (p.containsKey(Constants.P_AMQP_BROKER_PORT)) {
			String t = p.getProperty(Constants.P_AMQP_BROKER_PORT);
			try {
				port = Integer.parseInt(t);
			} catch (NumberFormatException e) {
			}
		}
		if (p.containsKey(Constants.P_AMQP_BROKER_QUEUE_NAME)) {
			queueName = p.getProperty(Constants.P_AMQP_BROKER_QUEUE_NAME);
		} else {
			sb.append("Missing property AMQP-Broker-Queue-Name!\n");
		}
		if (p.containsKey(Constants.P_AMQP_MESSAGE_SUBJECT)) {
			subject = p.getProperty(Constants.P_AMQP_MESSAGE_SUBJECT);
		}
		if (sb.toString().isEmpty()) {
			logger.info("Properties for Position Publisher: amqpBrokerHost=" + host + ", amqpBrokerPort=" + port
					+ ", amqpBrokerQueueName=" + queueName + ", amqpMessageSubject=" + subject);
			return null;
		}
		return sb.toString();
	}

	public BrokeredAMQPClient.Parameter createParameter() {
		logger.info("About to start AMQP Client...");
		return new BrokeredAMQPClient.Parameter();
	}

	public AmqpMessage createMsg(String content) {
		AmqpMessage m = new AmqpMessage(host, port, queueName, subject, content);
		logger.info(content);
		m.sendProperty = new SendProperty(2000);
		return m;
	}

	public void published(AmqpMessage m) {
		//logger.info("Message published, to=" + m.toAddr + ", content=" + m.body);
	}

	public void sensorErr() {
		logger.error("Error initializing GPS Sensor Simulator!");
	}

	public void amqpErr(AmqpError e) {
		if (e.message != null) {
			logger.error(e.errorMessage + " for AMQP message, content= " + e.message.body);
		} else {
			logger.error(e.errorMessage);
		}
	}

	public void error(String e) {
		logger.error(e);
	}
}
