package cn.mccraft.pangu.core.client.input;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindKeyPress {
    /**
     * @return the key description, generally start with "key."
     */
    String description();

    /**
     * @return the code of the key to bind from {@link org.lwjgl.input.Keyboard}
     */
    int keyCode();

    /**
     *
     * @return the key category, generally start with "key.categories.",
     * etc. {@link KeyBindingHelper#CATEGORY_CREATIVE}
     */
    String category();
}
