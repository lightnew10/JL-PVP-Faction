package fr.lightnew.tools;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClickMSG {
    public static TextComponent clickMSG(String message, ChatColor color, boolean bold, boolean italic, boolean obfuscated, boolean strikethrough, boolean underlined,
                                         HoverEvent.Action hoveraction, String var1, ClickEvent.Action clickAction, String var2) {
        TextComponent msg = new TextComponent(message);

        msg.setColor(color);
        msg.setBold(bold);
        msg.setItalic(italic);
        msg.setObfuscated(obfuscated);
        msg.setStrikethrough(strikethrough);
        msg.setUnderlined(underlined);

        msg.setHoverEvent(new HoverEvent(hoveraction, new ComponentBuilder(var1).create()));
        msg.setClickEvent(new ClickEvent(clickAction, var2));
        return msg;
    }

    public static TextComponent clickMSG(String message, HoverEvent.Action hoveraction, String var1, ClickEvent.Action clickAction, String var2) {
        return clickMSG(message, null, false, false, false, false, false, hoveraction, var1, clickAction, var2);
    }

    public static TextComponent clickMSG(String message, ChatColor color, HoverEvent.Action hoveraction, String var1, ClickEvent.Action clickAction, String var2) {
        return clickMSG(message, color, false, false, false, false, false, hoveraction, var1, clickAction, var2);
    }

    public static TextComponent clickMSG(String message, ChatColor color, boolean bold, HoverEvent.Action hoveraction, String var1, ClickEvent.Action clickAction, String var2) {
        return clickMSG(message, color, bold, false, false, false, false, hoveraction, var1, clickAction, var2);
    }
}
