package gps.testing.positionreceiver.component;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import no.ntnu.item.arctis.runtime.Block;
import com.bitreactive.examples.amqp.Constants;
import com.bitreactive.library.amqp.AmqpError;
import com.bitreactive.library.amqp.AmqpMessage;
import com.bitreactive.library.amqp.BrokerAddress;
import com.bitreactive.library.amqp.Utils;
import com.bitreactive.library.amqp.brokeredamqpclient.BrokeredAMQPClient;

public class Component extends Block {
	private String host, queueName;
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
		if (sb.toString().isEmpty()) {
			logger.info("Properties for Position Receiver: amqpBrokerHost=" + host + ", amqpBrokerPort=" + port
					+ ", amqpBrokerQueueName=" + queueName);
			return null;
		}
		return sb.toString();
	}

	public BrokeredAMQPClient.Parameter createParameter() {
		logger.info("About to start AMQP Client...");
		Set<BrokerAddress> addr = new HashSet<BrokerAddress>();
		addr.add(new BrokerAddress(host, port, queueName));
		return new BrokeredAMQPClient.Parameter(addr);
	}

	public void received(AmqpMessage m) {
		logger.info("Message received, createdAt=" + m.creationTime.toString() + ", subject=" + m.subject + ", content="
				+ m.body);
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

	public void ready() {
		logger.info("Ready!");
	}
}
