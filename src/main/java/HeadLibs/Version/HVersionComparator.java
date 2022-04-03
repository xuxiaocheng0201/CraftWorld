package HeadLibs.Version;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.Nullable;

public class HVersionComparator {
    public static int compareVersion(@Nullable String a, @Nullable String b) {
        if (a == null) {
            if (b == null)
                return 0;
            return -1;
        }
        if (b == null)
            return 1;
        String[] versionArray1 = HStringHelper.strip(a.split("\\."));
        String[] versionArray2 = HStringHelper.strip(b.split("\\."));
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
            ++idx;
        }
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
}
