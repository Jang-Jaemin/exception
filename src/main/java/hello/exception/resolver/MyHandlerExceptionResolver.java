//  API 예외처리 - HandlerExceptionResolver 시작
//  예외가발생해서서블릿을넘어 WAS까지예외가전달되면 HTTP 상태코드가 500으로처리된다.
//  발생하는예외에따라서 400, 404 등등다른상태코드로처리하고싶다.
//  오류메시지, 형식등을 API마다다르게처리하고싶다.

//  상태코드변환
//  예를들어서IllegalArgumentException을 처리하지 못해서 컨트롤러 밖으로넘어가는 일이 발생하면 HTTP 상태코드를 400으로처리하고싶다.


//  HandlerExceptionResolver
//  스프링 MVC는컨트롤러(핸들러) 밖으로예외가던져진경우예외를해결하고, 동작을새로정의할수있는 방법을제공한다.
//  컨트롤러밖으로던져진예외를해결하고, 동작방식을변경하고싶으면 HandlerExceptionResolver를사용하면된다.
//  줄여서ExceptionResolver라한다.


package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//HandlerExceptionResolver - 인터페이스
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call resolver", ex); //   handler 핸들러(컨트롤러)정보, Exception ex : 핸들러에서 발생한 예외

        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}

//  ExceptionResolver가ModelAndView를반환하는이유는마치 try, catch를하듯이, Exception을 처리해서정상흐름처럼변경하는것이목적이다.
//  이름그대로Exception을 Resolver(해결)하는것이 목적이다.
//  여기서는IllegalArgumentException이발생하면response.sendError(400)를호출해서 HTTP 상태코드를 400으로지정하고, 빈ModelAndView를반환한다.

//  반환값에따른동작방식 :
//  HandlerExceptionResolver의반환값에따른DispatcherServlet의동작방식은다음과같다.

//  빈 ModelAndView: new ModelAndView()처럼빈ModelAndView를반환하면뷰를렌더링하지 않고, 정상흐름으로서블릿이리턴된다.
//  ModelAndView 지정: ModelAndView에View, Model 등의정보를지정해서반환하면뷰를렌더링 한다.
//  null: null을반환하면, 다음ExceptionResolver를찾아서실행한다. 만약처리할수있는 ExceptionResolver가없으면예외처리가안되고, 기존에발생한예외를서블릿밖으로던진다.

//  ExceptionResolver 활용
//  예외상태코드변환 :
//  예외를response.sendError(xxx) 호출로변경해서서블릿에서상태코드에따른오류를 처리하도록위임
//  이후 WAS는서블릿오류페이지를찾아서내부호출, 예를들어서스프링부트가기본으로설정한/ error가호출됨
//  뷰템플릿처리 :
//  ModelAndView에값을채워서예외에따른새로운오류화면뷰렌더링해서고객에게제공
//  API 응답처리 :
//  response.getWriter().println("hello");처럼 HTTP 응답바디에직접데이터를넣어주는 것도가능하다. 여기에 JSON 으로응답하면 API 응답처리를할수있다.