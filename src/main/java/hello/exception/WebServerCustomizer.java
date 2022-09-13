//  서블릿 오류 페이지 등록

package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}

//  response.sendError(404): errorPage404 호출
//  response.sendError(500): errorPage500 호출
//  RuntimeException 또는그자식타입의예외: errorPageEx 호출
//  500 예외가 서버 내부에서 발생한 오류라는 뜻을 포함하고 있기 때문에 여기서는 예외가 발생한 경우도 500 오류 화면을 보여준다.
//  오류페이지는예외를다룰때해당예외와그자식타입의오류를함께처리한다. 예를들어서위의경우 RuntimeException은물론이고 RuntimeException의자식도함께처리한다.
//  오류가발생했을때처리할수있는컨트롤러가필요하다.
//  예를들어서RuntimeException 예외가 발생하면errorPageEx에서지정한/error-page/500이호출된다.