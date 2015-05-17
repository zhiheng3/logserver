package edu.thu.ss.logserver.request;

import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.ResponseUtil;

public final class ReadIntervalRequest extends ReadRequest {
	public long low;
	public long up;

	private String record;

	public ReadIntervalRequest() {
		super();
		record = new String();
	}

	public boolean ProcessData(long timestmp, String name, String roomId,
			Type type) {
		if (timestmp >= low && timestmp <= up) {
			record += String.format("%s\t%s\t%s\n", type.toString(), name, roomId);
		}
		return true;
	}

	public boolean EndRequest() {
		ResponseUtil.response(id, record);
		return true;
	}
}
