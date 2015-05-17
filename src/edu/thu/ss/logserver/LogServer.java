package edu.thu.ss.logserver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import edu.thu.ss.logserver.request.Request;
import edu.thu.ss.logserver.request.WriteRequest;
import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.LogFileReader;
import edu.thu.ss.logserver.request.util.ResponseUtil;

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
				// Read all data
				LogFileReader reader = new LogFileReader(new File("log.txt"));
				reader.startRead();
				String singleLine;
				while ((singleLine = reader.readLine()) != null) {
					String[] tmp = singleLine.split(",");
					Type type = tmp[3].equals("Enter") ? Type.Enter
							: Type.Leave;
					if (!request.ProcessData(Long.parseLong(tmp[0]), tmp[1],
							tmp[2], type)) {
						break;
					}
				}
				reader.endRead();
				reader.close();

				request.EndRequest();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
