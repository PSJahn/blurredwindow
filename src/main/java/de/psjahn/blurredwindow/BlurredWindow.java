package de.psjahn.blurredwindow;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.util.Identifier;

public class BlurredWindow implements ClientModInitializer {
    public static final Identifier MENU_BACKGROUND_TEXTURE = new Identifier("blurredwindow","textures/gui/menu_background.png");
    public static final Identifier HEADER_SEPARATOR_TEXTURE = new Identifier("blurredwindow","textures/gui/header_separator.png");
    public static final Identifier FOOTER_SEPARATOR_TEXTURE = new Identifier("blurredwindow","textures/gui/footer_separator.png");

    public static final Identifier INWORLD_HEADER_SEPARATOR_TEXTURE = new Identifier("blurredwindow","textures/gui/inworld_header_separator.png");
    public static final Identifier INWORLD_FOOTER_SEPARATOR_TEXTURE = new Identifier("blurredwindow","textures/gui/inworld_footer_separator.png");

    public static final ButtonTextures NEW_TAB_BUTTON_TEXTURES = new ButtonTextures(new Identifier("blurredwindow","widget/tab_selected"), new Identifier("blurredwindow","widget/tab"), new Identifier("blurredwindow","widget/tab_selected_highlighted"), new Identifier("blurredwindow","widget/tab_highlighted"));

    @Override
    public void onInitializeClient() {

    }
}
