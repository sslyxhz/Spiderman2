package net.kernal.spiderman.worker.extract;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.kernal.spiderman.Context;
import net.kernal.spiderman.Counter;
import net.kernal.spiderman.K;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.queue.Queue.Element;
import net.kernal.spiderman.queue.QueueManager;
import net.kernal.spiderman.worker.AbstractTask;
import net.kernal.spiderman.worker.Worker;
import net.kernal.spiderman.worker.WorkerManager;
import net.kernal.spiderman.worker.WorkerResult;
import net.kernal.spiderman.worker.download.DownloadTask;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.conf.Page;
import net.kernal.spiderman.worker.result.ResultTask;

public class ExtractManager extends WorkerManager {
	
	private List<Page> pages;
	
	public ExtractManager(int nWorkers, QueueManager queueManager, Counter counter, Logger logger, List<Page> pages) {
		super(nWorkers, queueManager, counter, logger);
		this.pages = pages;
		if (K.isEmpty(pages)) {
			throw new Spiderman.Exception("缺少页面抽取配置");
		}
	}
	
	public List<Page> getPages() {
		return this.pages;
	}
	
	protected void handleResult(WorkerResult wr) {
		final AbstractTask task = wr.getTask();
		final Object result = wr.getResult();
		final Page page = wr.getPage();
		final boolean isUnique = page == null ? false : page.isTaskDuplicateCheckEnabled();
		if (result instanceof ExtractResult) {
			// 计数器加1
			final long count = getCounter().plus();
			getLogger().info("解析了第"+count+"个模型");
			// 将成果放入结果处理队列
			final ExtractResult extractResult = (ExtractResult)result;
			getQueueManager().append(new ResultTask(isUnique, task.getSeed(), task.getRequest(), extractResult));
		} else if (result instanceof Downloader.Request) {
			final Downloader.Request request = (Downloader.Request)result;
			getQueueManager().append(new DownloadTask(isUnique, task.getSeed(), request));
		}
	}

	protected Element takeTask() {
		return getQueueManager().getExtractQueue().take();
	}

	protected Worker buildWorker() {
		return new ExtractWorker(this);
	}
	
	public static interface ResultHandler { 
		AtomicReference<Context> context = new AtomicReference<Context>();
		public default void init(Context ctx){
			context.set(ctx);
		}
		public void handle(ResultTask result, Counter c);
	}

	protected void clear() {
	}

}
