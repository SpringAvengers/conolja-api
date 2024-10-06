package site.javaghost.conolja.common.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
public class CachedServletInputStream extends ServletInputStream {

  private final BufferedInputStream cachedInputStream;
  private boolean isFinished;

  protected CachedServletInputStream(byte[] cachedBytes) {
    this.cachedInputStream = new BufferedInputStream(new ByteArrayInputStream(cachedBytes));
    this.isFinished = cachedBytes.length == 0;
  }

  @Override
  public int read() throws IOException {
    int result = cachedInputStream.read();
    if(result == -1) {
      isFinished = true;
    }
    return result;
  }

  @Override
  public boolean isFinished() {
    try {
      return cachedInputStream.available() == 0;
    } catch (IOException e) {
      log.error("Error occurred while checking if the stream is finished", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public void setReadListener(ReadListener listener) {
    throw new UnsupportedOperationException();
  }

  public void setFinished(boolean finished) {
    isFinished = finished;
  }
}
