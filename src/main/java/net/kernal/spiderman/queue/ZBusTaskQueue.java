package net.kernal.spiderman.queue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.zbus.broker.Broker;
import org.zbus.mq.Consumer;
import org.zbus.mq.MqConfig;
import org.zbus.mq.Producer;
import org.zbus.net.Sync.ResultCallback;
import org.zbus.net.http.Message;

import net.kernal.spiderman.K;
import net.kernal.spiderman.worker.Task;

public class ZBusTaskQueue implements TaskQueue {

	private Broker broker;
	private Producer producer;
	private Consumer consumer;
	
	public ZBusTaskQueue(Broker broker, String mq) {
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
	
	public Task take() {
		AtomicReference<Message> am = new AtomicReference<Message>();
		// FIXME 要用take，不能有timeout, 需要ZBUS提供支持
		consumer.onMessage((m, s) -> am.set(m));
		Message msg = am.get();
		if (msg == null) {
			return null;
		}
		byte[] data = msg.getBody();
		Task task = (Task)K.deserialize(data);
		return task;
	}

	public void append(Task task) {
		byte[] data = K.serialize(task);
		Message msg = new Message();
		msg.setHead("key", task.getUniqueKey());
		msg.setBody(data);
		try {
			producer.invokeAsync(msg, new ResultCallback<Message>() {
				public void onReturn(Message result) {
					// ignore
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		try {
			this.broker.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
