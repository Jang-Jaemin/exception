//  API 예외처리 - HandlerExceptionResolver 활용

//  예외를여기서마무리하기
//  예외가발생하면 WAS까지예외가던져지고, WAS에서오류페이지정보를찾아서다시/error를 호출하는과정은생각해보면너무복잡하다.
//  ExceptionResolver를활용하면예외가발생했을때이런 복잡한과정없이여기에서문제를깔끔하게해결할수있다.


package hello.exception.exception;

public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


//  <해석>
//  ExceptionResolver를사용하면컨트롤러에서예외가발생해도ExceptionResolver에서예외를 처리해버린다.
//  따라서예외가발생해도서블릿컨테이너까지예외가전달되지않고, 스프링 MVC에서예외처리는끝이난다.

//  결과적으로 WAS 입장에서는정상처리가된것이다. 이렇게예외를이곳에서모두처리할수있다는것이 핵심이다.
//  서블릿컨테이너까지예외가올라가면복잡하고지저분하게추가프로세스가실행된다.
//  반면에 ExceptionResolver를사용하면예외처리가상당히깔끔해진다.
//  그런데직접ExceptionResolver를구현하려고하니상당히복잡하다.
//  지금부터스프링이제공하는 ExceptionResolver들을알아보자.