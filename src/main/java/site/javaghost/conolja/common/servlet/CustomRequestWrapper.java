package site.javaghost.conolja.common.servlet;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] cachedContents;

  public CustomRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    try (InputStream inputStream = request.getInputStream();
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      StreamUtils.copy(inputStream, baos); // 이때 원본스트림을 읽어서 캐시에 저장
      this.cachedContents = baos.toByteArray();
    }
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    return new CachedServletInputStream(cachedContents);
  }
}
