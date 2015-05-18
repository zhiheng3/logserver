package edu.thu.ss.logserver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.thu.ss.logserver.request.Request;

public class LogServer {

	private BlockingQueue<Request> queue;

	public LogServer(BlockingQueue<Request> queue) {
		this.queue = queue;
	}

	public void start() {
		try {
			File logfile = new File("log.txt");
			if (logfile.exists()) {
				logfile.delete();
			}
			logfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				Request request = queue.take();
				synchronized (RequestProcess.lock) {
					RequestProcess.ProcessList.add(request);
				}
				ExecutorService exec = Executors.newCachedThreadPool();
				exec.execute(new RequestProcess(request));

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
