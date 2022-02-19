package CraftWorld.Dimension;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import CraftWorld.Instance.Dimension.DimensionEarthSurface;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class Dimension implements IDSTBase {
    private IDimensionBase instance = null;

    public static final String id = "Dimension";
    public static final String prefix = id;
    static {
        DSTUtils.register(id, Dimension.class);
    }

    @Override
    public void read(DataInput input) throws IOException {
        String name = input.readUTF();
        if (name.equals("null")) {
            instance = null;
            return;
        }
        instance = DimensionUtils.get(DimensionUtils.dePrefix(name));
        if (instance == null) {
            instance = new DimensionEarthSurface();
            return;
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        if (instance == null) {
            output.writeUTF("null");
            return;
        }
        output.writeUTF(instance.getName());
    }

    public IDimensionBase getInstance() {
        return instance;
    }

    public void setInstance(IDimensionBase instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("Dimension{",
                "name=", (instance == null)? "null" : instance.getName(),
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof Dimension))
            return false;
        return Objects.equals(this.instance, ((Dimension) a).instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instance);
    }
}
