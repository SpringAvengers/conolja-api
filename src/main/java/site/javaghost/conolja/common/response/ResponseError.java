package site.javaghost.conolja.common.response;

import org.springframework.http.ProblemDetail;

import java.util.ArrayList;
import java.util.List;

public class ResponseError extends ProblemDetail {
  private List<ResponseBadRequest> invalids = new ArrayList<>();

  public ResponseError(ProblemDetail problem) {
    setTitle(problem.getTitle());
    setDetail(problem.getDetail());
    setStatus(problem.getStatus());
    setInstance(problem.getInstance());
  }
}
