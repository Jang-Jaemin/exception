//  서블릿예외처리 - 오류페이지작동원리
//  서블릿은Exception(예외)가발생해서서블릿밖으로전달되거나또는response.
//  sendError()가호출 되었을때설정된오류페이지를찾는다.

//  예외 발생 흐름
//  WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)

//  sendError 흐름
//  WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러 (response.sendError())
//  WAS는해당예외를처리하는오류페이지정보를확인한다.
//  new ErrorPage(RuntimeException.class, "/error-page/500")

//  예를들어서RuntimeException 예외가 WAS까지전달되면, WAS는오류페이지정보를확인한다.
//  확인해보니RuntimeException의오류페이지로/error-page/500이지정되어있다.
//  WAS는오류 페이지를출력하기위해/error-page/500를다시요청한다.

//  오류페이지요청흐름
//  WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/ 500) -> View

//  예외발생과오류페이지요청흐름
//  1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
//  2. WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500) -> View
//  중요한점은웹브라우저(클라이언트)는서버내부에서이런일이일어나는지전혀모른다는점이다.
//  오직 서버내부에서오류페이지를찾기위해추가적인호출을한다.

//  정리하면 1. 예외가발생해서 WAS까지전파된다.
//  2.  WAS는오류페이지경로를찾아서내부에서오류페이지를호출한다. 이때오류페이지경로로필터, 서블릿, 인터셉터, 컨트롤러가모두다시호출된다.



package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    //  ErrorPageController - API 응답추가
    //  produces = MediaType.APPLICATION_JSON_VALUE의뜻은클라이언트가요청하는 HTTP Header의 Accept의값이application/json일때해당메서드가호출된다는것이다.
    //  결국클라어인트가받고 싶은미디어타입이 json이면이컨트롤러의메서드가호출된다.
    //  응답데이터를위해서Map을만들고status, message키에값을할당했다.
    //  Jackson 라이브러리는 Map을 JSON 구조로변환할수있다.

    //  ResponseEntity를사용해서응답하기때문에메시지컨버터가동작하면서클라이언트에 JSON이 반환된다.
    //  포스트맨을통해서다시테스트해보자.
    //  HTTP Header에Accept가application/json인것을꼭확인하자.
    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    //  BasicErrorController 코드
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(
            HttpServletRequest request, HttpServletResponse response) {

        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }

    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }
}

//  request.attribute에서버가담아준정보
//  javax.servlet.error.exception: 예외
//  javax.servlet.error.exception_type: 예외타입
//  javax.servlet.error.message: 오류메시지
//  javax.servlet.error.request_uri: 클라이언트요청 URI
//  javax.servlet.error.servlet_name: 오류가발생한서블릿이름
//  javax.servlet.error.status_code: HTTP 상태코드