package cup.util;

public class RadioStation {
	
	private String stationName;
	
	private String streamLink;
	
	public RadioStation(String stationName, String streamLink) {
		this.stationName = stationName;
		this.streamLink = streamLink;
	}
	
	public String getStationName() {
		return stationName;
	}
	
	public String getStreamLink() {
		return streamLink;
	}

}
