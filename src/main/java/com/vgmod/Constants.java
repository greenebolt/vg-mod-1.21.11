package com.vgmod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Constants {
    static final String CONFIG_WB_MESSAGES = "WBMessages";

    public static final Map<String, Integer> games = Map.ofEntries(
            Map.entry("a", 1),
            Map.entry("otc", 2),
            Map.entry("s", 3),
            Map.entry("ttt", 4),
            Map.entry("rr", 5),
            Map.entry("p", 6),
            Map.entry("hp", 8),
            Map.entry("hns", 9),
            Map.entry("rfb", 10),
            Map.entry("kpp", 11),
            Map.entry("bi", 13),
            Map.entry("pw", 14),
            Map.entry("sw", 15),
            Map.entry("bh", 16),
            Map.entry("tfl", 17),
            Map.entry("sb", 18),
            Map.entry("pr", 19),
            Map.entry("sg", 20),
            Map.entry("mi", 22),
            Map.entry("er", 23),
            Map.entry("mp", 24),
            Map.entry("bw", 25),
            Map.entry("zd", 26),
            Map.entry("ctf", 28),
            Map.entry("t", 30),
            Map.entry("pkr", 31)
    );
    public static String[] secretWBMessages = {
            "## has stepped onto the stage! Everybody get up on your feet and clap for ##!",
            "Oh, ##... I never thought I would see you again but you have returned!",
            "So then, welcome ## with great joy and honor people like ##.",
            "##, if people do not welcome you, leave their town and shake the dust off your feet as a testimony against them... Unless they are AFK or course.",
            "You are welcome at VG, ##,” the old man said. “Let me supply whatever you need. Only don’t spend the night in the square.",
            "##, my fellow prisoner Aristarchus sends you his greetings, as does Mark"
    };
}
