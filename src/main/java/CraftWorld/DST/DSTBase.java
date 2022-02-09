package CraftWorld.DST;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class DSTBase {
    public static final String id = "Base";
    static  {
        DSTUtils.register(id, DSTBase.class);
    }

    public DSTBase() {
        super();
    }

    public abstract void write(DataOutput output) throws IOException;
    public abstract void read(DataInput input) throws IOException;
}
