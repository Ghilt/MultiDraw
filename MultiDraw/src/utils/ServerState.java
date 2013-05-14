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

	public synchronized void addConnection(ServerConnection conn) {
		connections.add(conn);
	}
	
	public synchronized void removeConnection(ServerConnection conn) {
		connections.remove(conn);
	}

	public synchronized ArrayList<ServerConnection> getConnections() { // borde kanske inte l�mna ut denna utan modifiera listan h�r inne ist�llet kanske
		ArrayList<ServerConnection> temp = new ArrayList<ServerConnection>();
		temp.addAll(connections);
		return temp;
	}
}
