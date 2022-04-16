package Core.Addition.Implement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Announce at new mods element implement classes.
 * @author xuxiaocheng
 * @see ElementImplement
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewElementImplement {
    String modName() default "Craftworld";
    String elementName();
}