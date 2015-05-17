package edu.thu.ss.logserver.request;

import edu.thu.ss.logserver.request.WriteRequest.Type;

public abstract class Request {
	private static volatile int nextId = 0;

	public final int id = (++nextId);

	public abstract boolean isRead();

	public abstract boolean isWrite();

	public abstract boolean ProcessData(long timestmp, String name,
			String roomId, Type type);

	public abstract boolean EndRequest();
}
