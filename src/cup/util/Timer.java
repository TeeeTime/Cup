package cup.util;

public class Timer {
	
	private long accumulatedMillis = 0;
	private long lastStartTime = -1;
	
	public void start() {
		if(isRunning()) return;
		lastStartTime = System.currentTimeMillis();
	}
	
	public void stop() {
		if(!isRunning()) return;
		
		accumulatedMillis += System.currentTimeMillis() - lastStartTime;
		lastStartTime = -1;
	}
	
	public long getCurrentTimeInMillis() {
		if(isRunning()) {
			long currentSession = System.currentTimeMillis() - lastStartTime;
            return accumulatedMillis + currentSession;
		}else {
			return accumulatedMillis;
		}
	}
	
	public boolean isRunning() {
		return lastStartTime != -1;
	}

}
