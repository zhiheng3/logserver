package edu.thu.ss.logserver;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.thu.ss.logserver.request.ReadEmployeeRequest;
import edu.thu.ss.logserver.request.ReadIntervalRequest;
import edu.thu.ss.logserver.request.ReadStatusRequest;
import edu.thu.ss.logserver.request.ReadTimeRequest;
import edu.thu.ss.logserver.request.Request;
import edu.thu.ss.logserver.request.WriteRequest;
import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.LogFileReader;

public class RequestProcess implements Runnable {
	private Request request;
	private static int readcount = 0;
	private static int writecount = 0;
	public static Object lock = new Object();

	public static List<Request> ProcessList = new LinkedList<Request>();

	public RequestProcess(Request request) {
		this.request = request;
	}

	private int RequestType(Request request) {
		if (request instanceof WriteRequest) {
			return 0;
		}
		if (request instanceof ReadStatusRequest) {
			return 1;
		}
		if (request instanceof ReadEmployeeRequest) {
			return 2;
		}
		if (request instanceof ReadTimeRequest) {
			return 3;
		}
		if (request instanceof ReadIntervalRequest) {
			return 4;
		}
		return -1;
	}

	public boolean isConflict() {
		Iterator<Request> iter = ProcessList.iterator();
		while (iter.hasNext()){
			Request request = iter.next();
			if (request.id < this.request.id){
				if (RequestType(this.request) == 0 || RequestType(request) == 0){
					return true;
				}
			}
		}
		return false;
	}

	public void startProcess() {
		synchronized (RequestProcess.lock) {
			try {
				if (this.request instanceof WriteRequest) {
					while (RequestProcess.readcount != 0
							|| RequestProcess.writecount != 0 || isConflict()) {
						RequestProcess.lock.wait();
					}
					RequestProcess.writecount++;
				} else {
					while (RequestProcess.writecount != 0 || isConflict()) {
						RequestProcess.lock.wait();
					}
					RequestProcess.readcount++;
					System.out.println(String.format("Current reader: %d",
							RequestProcess.readcount));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	public void endProcess() {
		synchronized (RequestProcess.lock) {

			if (this.request instanceof WriteRequest) {
				RequestProcess.writecount--;
			} else {
				RequestProcess.readcount--;
			}
			ProcessList.remove(this.request);
			RequestProcess.lock.notifyAll();
		}
		return;
	}

	public void run() {
		// Read all data
		startProcess();
		try {
			LogFileReader reader = new LogFileReader(new File("log.txt"));
			reader.startRead();
			String singleLine;
			while ((singleLine = reader.readLine()) != null) {
				String[] tmp = singleLine.split(",");
				Type type = tmp[3].equals("Enter") ? Type.Enter : Type.Leave;
				if (!this.request.ProcessData(Long.parseLong(tmp[0]), tmp[1],
						tmp[2], type)) {
					break;
				}
			}
			reader.endRead();
			reader.close();
			this.request.EndRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		endProcess();
	}
}
