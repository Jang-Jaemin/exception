package hello.exception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExceptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExceptionApplication.class, args);
	}

}


//	메모장
//	스프링부트 - 오류페이지

//	지금까지예외처리페이지를만들기위해서다음과같은복잡한과정을거쳤다. WebServerCustomizer를만들고
//	예외종류에따라서ErrorPage를추가하고
//	예외처리용컨트롤러ErrorPageController를만듬

//	스프링부트는이런과정을모두기본으로제공한다.
//	ErrorPage를자동으로등록한다. 이때/error라는경로로기본오류페이지를설정한다.
//	new ErrorPage("/error"), 상태코드와예외를설정하지않으면기본오류페이지로사용된다. 서블릿밖으로예외가발생하거나, response.sendError(...)가호출되면모든오류는/error를 호출하게된다.
//	BasicErrorController라는스프링컨트롤러를자동으로등록한다. ErrorPage에서등록한/error를매핑해서처리하는컨트롤러다.

//	참고
// 	ErrorMvcAutoConfiguration이라는클래스가오류페이지를자동으로등록하는역할을한다.

//	주의
//	스프링부트가제공하는기본오류메커니즘을사용하도록WebServerCustomizer에있는 @Component를주석처리하자.
//	이제오류가발생했을때오류페이지로/error를기본요청한다.
//	스프링부트가자동등록한 BasicErrorController는이경로를기본으로받는다.

//	개발자는오류페이지만등록
//	BasicErrorController는기본적인로직이모두개발되어있다.
//	개발자는오류페이지화면만BasicErrorController가제공하는룰과우선순위에따라서등록하면 된다.
// 	정적 HTML이면정적리소스, 뷰템플릿을사용해서동적으로오류화면을만들고싶으면뷰템플릿 경로에오류페이지파일을만들어서넣어두기만하면된다.

//	뷰 선택 우선순위
//	BasicErrorController의처리순서

//	1.  뷰템플릿
//	resources/templates/error/500.html
//	resources/templates/error/5xx.html

//	2. 정적 리소스
//	resources/static/error/400.html
//	resources/static/error/404.html
//	resources/static/error/4xx.html

//	3. 적용 대상이 없을 때 뷰 이름
//	resources/templates/error.html

