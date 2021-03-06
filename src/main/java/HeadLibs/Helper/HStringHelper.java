package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Some tools about {@link String}
 */
@SuppressWarnings({"unused", "OverloadedVarargsMethod"})
public class HStringHelper {
    /**
     * No more null string.
     * @param a source string
     * @return fixed string
     */
    public static @NotNull String notNull(@Nullable String a) {
        if (a == null)
            return "";
        return a;
    }

    /**
     * No more null string.
     * @param a source string
     * @return fixed string
     */
    public static @NotNull String notNullOrEmpty(@Nullable String a) {
        if (a == null || a.isEmpty())
            return "null";
        return a;
    }

    /**
     * No more null string.
     * @param a source string
     * @return fixed string
     */
    public static @NotNull String notNullOrBlank(@Nullable String a) {
        if (a == null || a.isBlank())
            return "null";
        return a;
    }

    /**
     * Strip string.
     * @param a source string
     * @return fixed string
     */
    public static @Nullable String nullableStrip(@Nullable String a) {
        if (a == null)
            return null;
        return a.strip();
    }

    /**
     * Strip string.
     * @param a source string
     * @return fixed string
     */
    public static @NotNull String notNullStrip(@Nullable String a) {
        if (a == null || a.isBlank())
            return "";
        return a.strip();
    }

    /**
     * Strip string.
     * @param a source string
     * @return fixed string
     */
    public static @NotNull String notEmptyStrip(@Nullable String a) {
        if (a == null || a.isBlank())
            return "null";
        return a.strip();
    }

    /**
     * Does the String mean null?
     * @param a the string
     * @return true - meaningful. false - meaningless.
     */
    public static boolean meanNull(@Nullable String a) {
        if (a == null)
            return true;
        String s = notNullStrip(a);
        if (s.isEmpty())
            return true;
        return "null".equalsIgnoreCase(s);
    }

    /**
     * No more null strings.
     * @param a source strings
     * @return fixed strings
     */
    public static @NotNull String[] notNullOrEmpty(@NotNull String[] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = notNullOrEmpty(a[i]);
        return b;
    }

    /**
     * No more null strings.
     * @param a source strings
     * @return fixed strings
     */
    public static @NotNull String[] notNullOrBlank(@NotNull String[] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = notNullOrBlank(a[i]);
        return b;
    }

    /**
     * Strip strings.
     * @param a source strings
     * @return fixed strings
     */
    public static @NotNull String[] strip(@NotNull String[] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = a[i].strip();
        return b;
    }

    /**
     * Strip strings.
     * @param a source strings
     * @return fixed strings
     */
    public static @NotNull String[] notEmptyStrip(@NotNull String[] a) {
        int length = a.length;
        String[] b = new String[length];
        for (int i = 0; i < length; ++i)
            b[i] = notEmptyStrip(a[i]);
        return b;
    }

    /**
     * Concat objects to string.
     * Suggest using {@code string + string}
     * @param objects source objects
     * @return concatenate string
     */
    //@Deprecated
    public static @NotNull String concat(@NotNull Object ...objects) {
        if (objects.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(3 * objects.length);
        for (Object i: objects)
            builder.append(i);
        return builder.toString();
    }

    /**
     * Concat strings to string
     * Suggest using {@code string + string}
     * @param strings source strings
     * @return concatenate string
     */
    //@Deprecated
    public static @NotNull String concat(@NotNull String ...strings) {
        if (strings.length == 0)
            return "";
        StringBuilder builder = new StringBuilder(5 * strings.length);
        for (String i: strings)
            builder.append(i);
        return builder.toString();
    }
}
