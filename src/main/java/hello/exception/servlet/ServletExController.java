//  서블릿예외처리 - 시작
//  스프링이아닌순수서블릿컨테이너는예외를어떻게처리하는지알아보자. 서블릿은다음 2가지방식으로예외처리를지원한다.
//  Exception(예외)
//  response.sendError(HTTP 상태 코드, 오류 메시지)

//  Exception(예외) 자바직접실행
//  자바의 메인 메서드를 직접 실행하는 경우 main이라는 이름의 쓰레드가 실행된다.
//  실행 도중에 예외를 잡지 못하고 처음 실행한 main() 메서드를넘어서예외가던져지면, 예외정보를 남기고해당쓰레드는종료된다.


//  웹애플리케이션
//  웹애플리케이션은사용자요청별로별도의쓰레드가할당되고, 서블릿컨테이너안에서실행된다.
//  애플리케이션에서예외가발생했는데, 어디선가 try ~ catch로예외를잡아서처리하면아무런문제가 없다.
//  그런데만약에애플리케이션에서예외를잡지못하고, 서블릿밖으로까지예외가전달되면어떻게 동작할까?
//  WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
//  결국톰캣같은 WAS 까지예외가전달된다.
//  WAS는예외가올라오면어떻게처리해야할까?

//  application.properties
//  server.error.whitelable.enabled=false


package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//  ServletExController - 서블릿예외컨트롤러
@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생!");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!");
    }

    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "400 오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}

//  response.sendError(HTTP 상태코드, 오류메시지)
//  오류가발생했을때HttpServletResponse가제공하는sendError라는메서드를사용해도된다. 이것을호출한다고당장예외가발생하는것은아니지만, 서블릿컨테이너에게오류가발생했다는점을 전달할수있다.
//  이메서드를사용하면 HTTP 상태코드와오류메시지도추가할수있다.
//  response.sendError(HTTP 상태 코드)
//  response.sendError(HTTP 상태 코드, 오류 메시지)

//  sendError 흐름
//  WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러 (response.sendError())
//  response.sendError()를호출하면response내부에는오류가발생했다는상태를저장해둔다.
//  그리고서블릿컨테이너는고객에게응답전에response에sendError()가호출되었는지확인한다.
//  그리고호출되었다면설정한오류코드에맞추어기본오류페이지를보여준다.
//  실행해보면다음처럼서블릿컨테이너가기본으로제공하는오류화면을볼수있다.

//  정리
//  서블릿컨테이너가제공하는기본예외처리화면은사용자가보기에불편하다.