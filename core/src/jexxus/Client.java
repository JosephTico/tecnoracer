package jexxus;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Consumer;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client {

  private final String ip;
  private int port = Server.DEFAULT_PORT;
  private boolean ssl = false, compressed = false;
  private Connection conn;
  private Consumer<byte[]> messageCallback = (byte[] data) -> {
  };

  public Client(String ip) {
    this.ip = ip;
  }

  public Client port(int port) {
    this.port = port;
    return this;
  }

  public Client useSSL() {
    ssl = true;
    return this;
  }

  public Client compress() {
    compressed = true;
    return this;
  }

  public Client onMessage(Consumer<byte[]> callback) {
    messageCallback = callback;
    if (conn != null) {
      conn.onMessage(messageCallback);
    }
    return this;
  }

  public boolean isConnected() {
    return conn != null && conn.isConnected();
  }

  public Client send(byte[] data) {
    conn.send(data);
    return this;
  }

  public boolean connect() {
    try {
      SocketFactory socketFactory =
          ssl ? SSLSocketFactory.getDefault() : SocketFactory.getDefault();
      Socket tcpSocket = socketFactory.createSocket();

      if (ssl) {
        SSLSocket sslSocket = (SSLSocket) tcpSocket;
        sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
      }

      tcpSocket.connect(new InetSocketAddress(ip, port), 2000);

      conn = new Connection(tcpSocket, compressed);
      conn.onMessage(messageCallback);
      conn.listen();

      return true;
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

  public Connection getConnection() {
    return conn;
  }

  public void exit() {
    conn.close();
  }

}
