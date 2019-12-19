package by.training.command;

import by.training.core.ServiceException;
import by.training.game.ComplexGameDto;
import by.training.game.GameService;
import by.training.resourse.AppSetting;
import by.training.resourse.AttributesContainer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.List;

@RunWith(JUnit4.class)
public class ToHomePageActionCommandException {



    @Test
    public void direct() throws ActionCommandExecutionException, NoSuchFieldException, IllegalAccessException {
        GameService gameService = Mockito.mock(GameService.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        AppSetting setting = Mockito.mock(AppSetting.class);


        ActionCommand toHomePage = new ToHomePageCommand(gameService);
        Field settingField =  toHomePage.getClass().getDeclaredField("setting");
        settingField.setAccessible(true);
        settingField.set(toHomePage, setting);

        Mockito.when(setting.get(AppSetting.SettingName.HOME_PAGE_MAX_GAME_RESULTS)).thenReturn("5");

        Assert.assertTrue(toHomePage.direct(request, null).isPresent());

    }



}
