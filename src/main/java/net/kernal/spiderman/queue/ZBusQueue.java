package net.kernal.spiderman.queue;

import java.io.IOException;

import org.zbus.broker.Broker;
import org.zbus.mq.Consumer;
import org.zbus.mq.MqConfig;
import org.zbus.mq.Producer;
import org.zbus.net.Sync.ResultCallback;
import org.zbus.net.http.Message;

import net.kernal.spiderman.K;
import net.kernal.spiderman.Spiderman;

/**
 * PS:由于ZBus支持队列元素的重复检查，所以此类不需要继承CheckableQueue
 * @author 赖伟威 l.weiwei@163.com 2016-01-19
 *
 */
public class ZBusQueue implements Queue {

	private int beatPeriod = 5000;
	private Broker broker;
	private Producer producer;
	private Consumer consumer;
	
	public ZBusQueue(Broker broker, String mq) {
		this.broker = broker;
	    final MqConfig cfg = new MqConfig(); 
	    cfg.setBroker(broker);
	    cfg.setMq(mq);
	    
	    this.producer = new Producer(cfg);
		try {
			this.producer.createMQ();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		this.consumer = new Consumer(cfg);
	}
	
	public Element take() {
		Message msg = null;
		while(true){
			try {
				// 啥时候可以把beatPeriod干掉，这个应该是底层去实现的。
				msg = consumer.recv(beatPeriod);
				if(msg != null) {
					break;
				}
			} catch (IOException | InterruptedException e) {
				throw new Spiderman.Exception("zbus consumer recv error", e);
			}
		}
		final byte[] data = msg.getBody();
		final Element e = (Element)K.deserialize(data);
		return e;
	}

	public void append(Element e) {
		byte[] data = K.serialize(e);
		Message msg = new Message();
		if (e instanceof AbstractElement) {
			msg.setHead("key", ((AbstractElement)e).getKey());
		}
		msg.setBody(data);
		try {
			producer.invokeAsync(msg, new ResultCallback<Message>() {
				public void onReturn(Message result) {
					// ignore
				}
			});
		} catch (IOException ex) {
			throw new Spiderman.Exception("zbus producer invoke error", ex);
		}
	}

	public void clear() {
		try {
			this.consumer.close();
			this.broker.close();
		} catch (IOException e) {
			throw new Spiderman.Exception("zbus client close error", e);
		}
	}

}