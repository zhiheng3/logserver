package edu.thu.ss.logserver.request;

import java.io.File;
import java.io.IOException;

import edu.thu.ss.logserver.request.util.LogFileWriter;
import edu.thu.ss.logserver.request.util.ResponseUtil;

public final class WriteRequest extends Request {
	public enum Type {
		Enter, Leave;
	}

	public long timestmp;
	public String name;
	public String roomId;
	public Type type;

	private String curRoom;
	private long curTime;;

	public WriteRequest(long timestmp, String name, String roomId, Type type) {
		super();
		this.timestmp = timestmp;
		this.name = name;
		this.roomId = roomId;
		this.type = type;

		this.curRoom = "Hall";
		this.curTime = -1;
	}

	public boolean ProcessData(long timestmp, String name, String roomId,
			Type type) {
		if (!this.name.equals(name)) {
			return true;
		}
		curTime = timestmp;
		if (type == Type.Enter) {
			curRoom = roomId;
		} else {
			curRoom = "Hall";
		}
		return true;
	}

	public boolean EndRequest() {
		// Illegal Case 1 timestamp
		if (curTime >= this.timestmp) {
			ResponseUtil.response(id, "error");
			return true;
		}
		// Illegal Case 2 leave before enter
		if (type == Type.Leave && !curRoom.equals(roomId)) {
			ResponseUtil.response(id, "error");
			return true;
		}
		// Illegal Case 3 enter before leave
		if (type == Type.Enter && !curRoom.equals("Hall")) {
			ResponseUtil.response(id, "error");
			return true;
		}
		try {
			LogFileWriter writer = new LogFileWriter(new File("log.txt"));
			writer.startWrite();
			writer.write(String.format("%d,%s,%s,%s\n", this.timestmp,
					this.name, this.roomId, this.type.toString()));

			writer.endWrite();
			writer.close();

			ResponseUtil.response(id, "correct");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean isRead() {
		return false;
	}

	@Override
	public boolean isWrite() {
		return true;
	}
}
