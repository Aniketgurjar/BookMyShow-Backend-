package Controllers;

import Dtos.ResponseStatus;
import Dtos.SignInRequestDto;
import Dtos.SignUpRequestDto;
import Dtos.SignUpResponseDto;
import Models.User;
import Services.UserServices;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;
    }

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        User user = userService.signUp(requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword());

        SignUpResponseDto responseDto = new SignUpResponseDto();
        responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        responseDto.setUser(user);
        return responseDto;
    }

    public void signIn(SignInRequestDto requestDto) {
        userService.signIn(
                requestDto.getEmail(),
                requestDto.getPassword()
        );
    }
}