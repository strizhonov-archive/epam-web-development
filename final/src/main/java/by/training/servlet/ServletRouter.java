package by.training.servlet;

import by.training.appentry.ApplicationConstantContainer;

public class ServletRouter {

    private String path;
    private ServletRouterState state;

    public ServletRouter() {
        this.path = ApplicationConstantContainer.INDEX_PAGE_PATH;
        this.state = new ForwardRouterState(path);
    }

    public ServletRouter(String path) {
        this.path = path;
        this.state = new ForwardRouterState(path);
    }

    public ServletRouter(String path, RouterType routeType) {
        this.path = path;
        if (routeType == RouterType.FORWARD) {
            this.state = new ForwardRouterState(path);
        } else {
            this.state = new RedirectForwardState(path);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ServletRouterState getState() {
        return state;
    }

    public void setState(RouterType routerType) {
        if (routerType == RouterType.FORWARD) {
            this.state = new ForwardRouterState(path);
        } else {
            this.state = new RedirectForwardState(path);
        }
    }

    public enum RouterType {
        FORWARD, REDIRECT
    }
}
