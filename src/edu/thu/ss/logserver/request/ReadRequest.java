package edu.thu.ss.logserver.request;

import edu.thu.ss.logserver.request.WriteRequest.Type;

public abstract class ReadRequest extends Request {

	@Override
	public boolean isRead() {
		return true;
	}

	@Override
	public boolean isWrite() {
		return false;
	}

	public abstract boolean ProcessData(long timestmp, String name,
			String roomId, Type type);

	public abstract boolean EndRequest();
}
