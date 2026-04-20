package cup.util;

public class RadioStation {
	
	private String stationIdentifier;
	
	private String stationName;
	
	private String streamLink;
	
	public RadioStation(String stationIdentifier, String stationName, String streamLink) {
		this.stationIdentifier = stationIdentifier;
		this.stationName = stationName;
		this.streamLink = streamLink;
	}
	
	public String getStationName() {
		return stationName;
	}
	
	public String getStreamLink() {
		return streamLink;
	}
	
	public String getStationIdentifier() {
		return stationIdentifier;
	}

}
