package net.kernal.spiderman.conf;

import javax.script.ScriptEngine;

import org.zbus.broker.Broker;

import net.kernal.spiderman.Properties;
import net.kernal.spiderman.downloader.Downloader;
import net.kernal.spiderman.queue.TaskQueue;
import net.kernal.spiderman.reporting.Reporting;
import net.kernal.spiderman.reporting.Reportings;

public class Conf {
	
	public Conf() {
		seeds = new Seeds();
		targets = new Targets();
		properties = new Properties();
		reportings = new Reportings();
	}
	
	private Seeds seeds;
	private Targets targets;
	private Properties properties;
	private Downloader downloader;
	private Reportings reportings;
	private TaskQueue primaryDownloadTaskQueue;
	private TaskQueue secondaryDownloadTaskQueue;
	private TaskQueue primaryParseTaskQueue;
	private TaskQueue secondaryParseTaskQueue;
	private TaskQueue resultTaskQueue;
	private ScriptEngine scriptEngine;
	private Broker zbusBroker;
	
	public TaskQueue getPrimaryDownloadTaskQueue() {
		return primaryDownloadTaskQueue;
	}
	public Conf setPrimaryDownloadTaskQueue(TaskQueue primaryDownloadTaskQueue) {
		this.primaryDownloadTaskQueue = primaryDownloadTaskQueue;
		return this;
	}
	public TaskQueue getSecondaryDownloadTaskQueue() {
		return secondaryDownloadTaskQueue;
	}
	public Conf setSecondaryDownloadTaskQueue(TaskQueue secondaryDownloadTaskQueue) {
		this.secondaryDownloadTaskQueue = secondaryDownloadTaskQueue;
		return this;
	}
	public TaskQueue getPrimaryParseTaskQueue() {
		return primaryParseTaskQueue;
	}
	public Conf setPrimaryParseTaskQueue(TaskQueue primaryParseTaskQueue) {
		this.primaryParseTaskQueue = primaryParseTaskQueue;
		return this;
	}
	public TaskQueue getSecondaryParseTaskQueue() {
		return secondaryParseTaskQueue;
	}
	public Conf setSecondaryParseTaskQueue(TaskQueue secondaryParseTaskQueue) {
		this.secondaryParseTaskQueue = secondaryParseTaskQueue;
		return this;
	}
	public Conf setResultTaskQueue(TaskQueue resultTaskQueue) {
		this.resultTaskQueue = resultTaskQueue;
		return this;
	}
	public TaskQueue getResultTaskQueue() {
		return this.resultTaskQueue;
	}
	public static interface Builder {
		public Conf build() throws Exception;
	}
	
	public Conf addSeed(String url) {
		seeds.add(new Downloader.Request(url));
		return this;
	}
	public Conf addSeed(String url, String httpMethod) {
		seeds.add(new Downloader.Request(url, httpMethod));
		return this;
	}
	public Conf addSeed(Downloader.Request request) {
		seeds.add(request);
		return this;
	}
	public Conf addTarget(Target target) {
		targets.add(target);
		return this;
	}
	public Conf set(String property, Object value) {
		this.properties.put(property, value);
		return this;
	}
	public Conf setDownloader(Downloader downloader) {
		this.downloader = downloader;
		return this;
	}
	public Conf addReporting(Reporting reporting) {
		this.reportings.add(reporting);
		return this;
	}
	public Conf setScriptEngine(ScriptEngine scriptEngine) {
		this.scriptEngine = scriptEngine;
		return this;
	}
	public ScriptEngine getScriptEngine() {
		return this.scriptEngine;
	}
	public Seeds getSeeds() {
		return seeds;
	}
	public Targets getTargets() {
		return targets;
	}
	public Properties getProperties() {
		return properties;
	}
	public Downloader getDownloader() {
		return downloader;
	}
	public Reportings getReportings() {
		return reportings;
	}
	public Conf setZbusBroker(Broker broker) {
		this.zbusBroker = broker;
		return this;
	}
	public Broker getZbusBroker() {
		return this.zbusBroker;
	}
	
}
