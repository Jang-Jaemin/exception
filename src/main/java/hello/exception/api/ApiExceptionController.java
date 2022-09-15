//  ApiExceptionController -  API 예외컨트롤러
//  단순히회원을조회하는기능을하나만들었다.
//  예외테스트를위해 URL에전달된id의값이ex이면 예외가발생하도록코드를심어두었다.

//  Postman으로테스트
//  HTTP Header에Accept가application/json인것을꼭확인하자.

//  API를요청했는데, 정상의경우 API로 JSON 형식으로데이터가정상반환된다.
//  그런데오류가발생하면 우리가미리만들어둔오류페이지 HTML이반환된다.
//  이것은기대하는바가아니다. 클라이언트는정상 요청이든, 오류요청이든 JSON이반환되기를기대한다.
//  웹브라우저가아닌이상 HTML을직접받아서할 수있는것은별로없다.
//  문제를해결하려면오류페이지컨트롤러도 JSON 응답을할수있도록수정해야한다.

package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }

}
