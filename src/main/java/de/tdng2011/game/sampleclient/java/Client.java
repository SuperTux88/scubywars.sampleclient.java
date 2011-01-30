package de.tdng2011.game.sampleclient.java;

import de.tdng2011.game.library.EntityTypes;
import de.tdng2011.game.library.Player;
import de.tdng2011.game.library.Shot;
import de.tdng2011.game.library.util.ByteUtil;
import de.tdng2011.game.library.util.StreamUtil;
import scala.collection.JavaConversions;
import scala.collection.mutable.Buffer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    private static List<Object> entityList = new ArrayList<Object>();

    private static final Socket connection = connect();

    public static void main(String... args) throws IOException {
        System.out.println("Sample java client");

        handshakePlayer();

        final DataInputStream iStream = new DataInputStream(connection.getInputStream());

        connection.getOutputStream().write(ByteUtil.toByteArray(asScalaBuffer(true, false, true, false)));

        while (true) {
            final ByteBuffer buf = StreamUtil.read(iStream, 2);
            final int id = buf.getShort();

            if (id == EntityTypes.Player().id()) {
                entityList.add(new Player(iStream));
            } else if (id == EntityTypes.Shot().id()) {
                entityList.add(new Shot(iStream));
            } else if (id == EntityTypes.World().id()) {
                entityList = new ArrayList<Object>();
            } else {
                System.out.println("barbra streisand! (unknown bytes, wth?!) typeId: " + id);
                System.exit(-1);
            }
        }


    }

    public static void handshakePlayer() throws IOException {
        connection.getOutputStream().write(ByteUtil.toByteArray(asScalaBuffer((short) 0, "123456789012")));
        final ByteBuffer response = StreamUtil.read(new DataInputStream(connection.getInputStream()), 9);
        System.out.println("response code: " + response.get());
        System.out.println("publicId: " + response.getLong());
    }

    public static Socket connect() {
        Socket socket;
        try {
            socket = new Socket("localhost", 1337);
        } catch (Exception e) {
            System.out.println("connecting failed. retrying in 5 seconds");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                // ignore
            }
            socket = connect();
        }
        return socket;
    }

    public static Buffer asScalaBuffer(Object... objects) {
        return JavaConversions.asScalaBuffer(Arrays.asList(objects));
    }
}
