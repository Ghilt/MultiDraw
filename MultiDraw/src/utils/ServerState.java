package utils;

public class ServerState {
	private boolean disabled;

	public ServerState() {
		this.disabled = false;
	}
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
