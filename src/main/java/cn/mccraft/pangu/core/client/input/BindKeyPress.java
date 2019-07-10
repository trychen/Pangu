package cn.mccraft.pangu.core.client.input;

import net.minecraftforge.client.settings.KeyModifier;

import java.lang.annotation.*;

/**
 * Bind key pressed to method.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindKeyPress {

    /**
     * @return the code of the key to bind from {@link org.lwjgl.input.Keyboard}
     */
    int value() default -1;

    @Deprecated
    int keyCode() default -1;

    /**
     * If this meta is empty, injector will use method name
     * (separated by {@link cn.mccraft.pangu.core.util.NameBuilder#apart(String)}
     * and connected with '.') as description.
     *
     * @return the key description, generally start with "key."
     */
    String description() default "";

    KeyModifier modifier() default KeyModifier.NONE;

    /**
     * @return the key category, generally start with "key.categories.",
     * etc. {@link KeyBindingHelper#CATEGORY_CREATIVE}
     */
    String category() default KeyBindingHelper.CATEGORY_MISC;

    /**
     * @return whether responding in game
     */
    boolean enableInGame() default true;

    /**
     * @return whether responding in GUI
     */
    boolean enableInGUI() default false;

    /**
     * @return true that only active in development env
     */
    boolean devOnly() default false;
}
