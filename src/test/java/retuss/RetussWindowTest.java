package retuss;

import javafx.stage.Stage;
import mockit.Expectations;
import mockit.Mocked;

import javafx.application.Application;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class RetussWindowTest {
    @Mocked
    private Application app;

    @Tested
    private RetussWindow retussWindow = new RetussWindow();

    @Ignore( "app.launchが動かなくなった" )
    @Test
    public void main文を実行するとlaunchメソッドを1回実行する() {
        // 初期化
        new Expectations() {{
            String[] args = {};
            app.launch( args );
            times = 1;
        }};

        // 実行
        String[] args = {};

        // 検証
        retussWindow.main( args );
    }
}