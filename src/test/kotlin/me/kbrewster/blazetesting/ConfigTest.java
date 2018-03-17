package me.kbrewster.blazetesting;

import me.kbrewster.blazeapi.api.config.SaveableString;
import me.kbrewster.blazeapi.api.config.impl.GsonConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ConfigTest {

    private GsonConfig config = new GsonConfig(new File("world.json"));

    @SaveableString("hello_world")
    private String world = "hello world!";

    @Before
    public void saveConfig() {
        config.register(this);
        world = "bye world!";
        config.save();
    }

    @Test
    public void loadConfig() {
        config.register(this);
        world = "hello world!";
        config.load();
        assertTrue(world.equals("bye world!"));
    }


}
