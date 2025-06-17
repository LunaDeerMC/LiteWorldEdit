package cn.lunadeer.liteworldedit;

import cn.lunadeer.liteworldedit.utils.XLogger;
import cn.lunadeer.liteworldedit.utils.configuration.Comments;
import cn.lunadeer.liteworldedit.utils.configuration.ConfigurationFile;
import cn.lunadeer.liteworldedit.utils.configuration.ConfigurationPart;
import cn.lunadeer.liteworldedit.utils.configuration.PostProcess;

public class Configuration extends ConfigurationFile {

    @Comments({
            "The maximum size of the cuboid player can select.",
            "Too large may cause operation out of chunk load range.",
    })
    public static MaximumSize maximumSize = new MaximumSize();

    public static class MaximumSize extends ConfigurationPart {
        public int x = 64;
        public int y = 64;
        public int z = 64;
    }

    @Comments({
            "The speed of task execution.",
            "For 1 means execute 1 task per tick.",
    })
    public static int multiplier = 1;

    @Comments({
            "Weather to drop items when doing empty(dig) job.",
            "Not recommended, because it may cause a lot of items dropped on the ground",
            "resulting in lag for the server.",
    })
    public static boolean dropItems = false;

    public static boolean debug = false;

    @PostProcess
    public void setDebug() {
        XLogger.setDebug(debug);
    }
}
