package utils;

import java.util.ArrayList;

import network.ServerConnection;

public class ServerState {
	private boolean disabled;
	private ArrayList<ServerConnection> connections;

	public ServerState() {
		this.connections = new ArrayList<ServerConnection>();
		this.disabled = false;
	}
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void addConnection(ServerConnection conn) {
		connections.add(conn);
	}
	
	public void removeConnection(ServerConnection conn) {
		connections.remove(conn);
	}

	public ArrayList<ServerConnection> getConnections() { // borde kanske inte lämna ut denna utan modifiera listan här inne istället kanske
		return connections;
	}
}
