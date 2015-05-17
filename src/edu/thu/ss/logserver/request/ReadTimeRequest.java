package edu.thu.ss.logserver.request;

import edu.thu.ss.logserver.request.WriteRequest.Type;
import edu.thu.ss.logserver.request.util.ResponseUtil;

public final class ReadTimeRequest extends ReadRequest {
	public String name;
	public String roomId;

	public boolean ProcessData(long timestmp, String name, String roomId,
			Type type) {
		return true;
	}

	public boolean EndRequest() {
		ResponseUtil.response(id, "Time");
		return true;
	}
}
