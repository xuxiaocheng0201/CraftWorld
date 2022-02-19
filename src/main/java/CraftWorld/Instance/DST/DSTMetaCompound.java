package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DSTMetaCompound implements IDSTBase {
    public static final String id = "Compound";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        DSTUtils.register(id, DSTMetaCompound.class);
    }

    private String name = "";
    private final Map<String, IDSTBase> dstMap = new HashMap<>();

    public DSTMetaCompound() {
        super();
    }

    public DSTMetaCompound(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        dstMap.clear();
        this.name = input.readUTF();
        String name = input.readUTF();
        while (!suffix.equals(name)) {
            IDSTBase dst = DSTUtils.get(DSTUtils.dePrefix(input.readUTF()));
            if (dst != null)
                dst.read(input);
            dstMap.put(name, dst);
            name = input.readUTF();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(name);
        for (String name: dstMap.keySet()) {
            output.writeUTF(name);
            dstMap.get(name).write(output);
        }
        output.writeUTF(suffix);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, IDSTBase> getDstMap() {
        return dstMap;
    }

    @Override
    public String toString() {
        return HStringHelper.merge("DSTMetaCompound{",
                "name='", name, '\'',
                ", dstMap=", dstMap,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof DSTMetaCompound))
            return false;
        return Objects.equals(this.name, ((DSTMetaCompound) a).name) &&
                Objects.equals(this.dstMap, ((DSTMetaCompound) a).dstMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dstMap);
    }
}