import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;

import java.awt.*;

public class FrameworkParameters {

    private static volatile FrameworkParameters instance = null;

    private FrameworkParameters() {
    }

    public static synchronized FrameworkParameters getInstance() {
        if (instance == null) {
            instance = new FrameworkParameters();
        }
        return instance;
    }

    /* command line arguments */

    @Parameter(names = {"--seleniumAddress"}, description = "IP address of the selenium server")
    private String seleniumAddress;

    @Parameter(names = {"--baseUrl"}, description = "Url of the application")
    private String baseUrl;

    @Parameter(names = {"--username"}, description = "Admin username for the application")
    private String userName;

    @Parameter(names = {"--password"}, description = "Admin username for the application")
    private String password;

    @Parameter(names = {"--screenSize"}, description = "Admin username for the application", converter = ScreenSizeConverter.class)
    private Dimension screenSize;


    /* Access methods */

    public String getSeleniumAddress() {
        return seleniumAddress;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

}

class ScreenSizeConverter implements IStringConverter<Dimension> {
    @Override
    public Dimension convert(String value) {
        String[] dimensions = value.split("x");
        if (dimensions.length == 2) {
            return new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
        }
        return null;
    }
}


