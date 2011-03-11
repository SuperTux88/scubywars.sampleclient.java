package de.tdng2011.game.sampleclient.java;

import java.io.IOException;

import de.tdng2011.game.library.World;
import de.tdng2011.game.library.connection.AbstractClient;
import de.tdng2011.game.library.connection.RelationTypes;

public class Client extends AbstractClient {

	private static Client client;

	public Client(final String hostname) {
		super(hostname, RelationTypes.Player(), true);
	}

	public static void main(final String... args) throws IOException {
		System.out.println("Sample java client");

		client = new Client("test.scubywars.de");

		client.action(true, false, true, true);
	}

	@Override
	public String name() {
		return "Java-Client";
	}

	@Override
	public void processWorld(final World world) {
		// TODO
	}
}
