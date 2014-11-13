package io.vertigo.nitro.impl.redis.resp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public final class RespClient implements AutoCloseable {
	private final Socket socket;
	private final BufferedReader in;
	private final BufferedOutputStream out;

	public RespClient(final String host, final int port) {
		try {
			//			System.out.println(">>");
			//			SocketAddress socketAddress = new InetSocketAddress("172.20.0.9", 3128);
			//			Proxy proxy = new Proxy(Proxy.Type.SOCKS, socketAddress);
			//			System.out.println(">> + proxy");
			//			socket = new Socket(proxy);
			//			//socket = new Socket(Proxy.NO_PROXY);
			//
			//			InetSocketAddress dest = new InetSocketAddress(host, port);
			//			System.out.println(">> + connecting...");
			//			socket.connect(dest, 10000);
			//			System.out.println(">> + connected");

			socket = new Socket(host, port);

			// socket.setReuseAddress(true);
			// socket.setKeepAlive(true); //Will monitor the TCP connection is valid
			// socket.setTcpNoDelay(true); //Socket buffer Whetherclosed, to ensure timely delivery of data
			// socket.setSoLinger(true, 0); //Control calls close () method, the underlying socket is closed immediately

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedOutputStream(socket.getOutputStream());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		try {
			doClose();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void doClose() throws IOException {
		try {
			try {
				out.close();
			} finally {
				in.close();
			}
		} finally {
			//On ferme tjrs la socket
			socket.close();
		}
	}

	public Object execEval(final String command, final String... args) {
		return RespProtocol.pushPull(RespProtocol.RespType.RESP_EVAL, in, out, command, args);
	}

	public List<String> execArray(final String command, final String... args) {
		return (List<String>) RespProtocol.pushPull(RespProtocol.RespType.RESP_ARRAY, in, out, command, args);
	}

	public long execLong(final String command, final String... args) {
		return (Long) RespProtocol.pushPull(RespProtocol.RespType.RESP_INTEGER, in, out, command, args);
	}

	public String execString(final String command, final String... args) {
		return (String) RespProtocol.pushPull(RespProtocol.RespType.RESP_STRING, in, out, command, args);
	}

	public String execBulk(final String command, final String... args) {
		return (String) RespProtocol.pushPull(RespProtocol.RespType.RESP_BULK, in, out, command, args);
	}

	@Override
	public String toString() {
		return socket.toString();
	}
}
