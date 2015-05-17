package edu.thu.ss.logserver.request.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogFileWriter extends PrintWriter {

	public LogFileWriter(File file) throws IOException {
		super(new BufferedWriter(new FileWriter(file, true)));
	}

	public void startWrite() {

	}

	public void endWrite() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}

}
