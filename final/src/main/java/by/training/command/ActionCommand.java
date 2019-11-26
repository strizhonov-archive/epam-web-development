package by.training.command;

import by.training.servlet.ServletRouter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ActionCommand {
    ServletRouter execute(HttpServletRequest request, HttpServletResponse response) throws ActionCommandExecutionException;

    ActionCommandType getType();
}