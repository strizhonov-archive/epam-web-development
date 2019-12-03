package by.training.command.impl;

import by.training.command.ActionCommand;
import by.training.command.ActionCommandExecutionException;
import by.training.command.ActionCommandType;
import by.training.constant.AttributesContainer;
import by.training.constant.MessagesContainer;
import by.training.constant.ValuesContainer;
import by.training.dto.UserDto;
import by.training.service.ServiceException;
import by.training.service.UserService;
import by.training.servlet.HttpRouter;
import by.training.servlet.RelativePathRedirector;
import by.training.servlet.ServletForwarder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogInCommand implements ActionCommand {

    private static final Logger LOGGER = LogManager.getLogger(LogInCommand.class);
    private final ActionCommandType type = ActionCommandType.LOGIN;
    private UserService userService;

    public LogInCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpRouter direct(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response)
            throws ActionCommandExecutionException {        String login = request.getParameter(AttributesContainer.USERNAME.toString());
        String password = request.getParameter(AttributesContainer.PASSWORD.toString());

        UserDto userDto;
        try {
            userDto = userService.findBy(login, password);
        } catch (ServiceException e) {
            LOGGER.error("Unable to retrieve user.", e);
            request.setAttribute(AttributesContainer.USERNAME_ERROR.toString(),
                    MessagesContainer.LOGIN_ERROR_MESSAGE);
            return new ServletForwarder(servlet, request.getContextPath());
        }
        if (userDto == null) {
            request.setAttribute(AttributesContainer.USERNAME_OR_PASSWORD_ERROR.toString(),
                    MessagesContainer.USERNAME_OR_PASSWORD_ERROR_MESSAGE);
            return new RelativePathRedirector(response.getHeader(ValuesContainer.REFERER));
        }

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(AttributesContainer.USER_ROLE.toString(), userDto.getType().name());
        httpSession.setAttribute(AttributesContainer.USER.toString(), userDto);
        httpSession.setAttribute(AttributesContainer.USER.toString(), userDto);
        request.setAttribute(AttributesContainer.USER_TO_PROCESS.toString(), userDto);

        String lang = userDto.getLanguage().name();
        httpSession.setAttribute(AttributesContainer.LANGUAGE.toString(), lang);
//        String avatar = Base64.getEncoder().encodeToString(userDto.getAvatar());
//        httpSession.setAttribute("avatar", avatar);

        return new ServletForwarder(servlet, "/jsp/user-profile.jsp");
    }

    @Override
    public ActionCommandType getType() {
        return type;
    }
}
