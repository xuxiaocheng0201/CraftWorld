package CraftWorld.Instance.DST;

import CraftWorld.DST.DSTFormatException;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Logger.HLog;
import HeadLibs.Logger.HLogLevel;
import HeadLibs.Registerer.HElementRegisteredException;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public class DSTTagDoubleCollection implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 671316780976864462L;
    public static final String id = "DSTTagDoubleCollection";
    public static final String prefix = DSTUtils.prefix(id);
    public static final String suffix = DSTUtils.suffix(id);
    static {
        try {
            DSTUtils.getInstance().register(id, DSTTagDoubleCollection.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HLogLevel.ERROR, exception);
        }
    }

    private String name = id;
    private final @NotNull Collection<Double> data;

    public DSTTagDoubleCollection(Supplier<? extends Collection<Double>> supplier) {
        super();
        this.data = supplier.get();
    }

    public DSTTagDoubleCollection(String name, Supplier<? extends Collection<Double>> supplier) {
        super();
        this.name = name;
        this.data = supplier.get();
    }

    public DSTTagDoubleCollection(@NotNull Collection<Double> collection) {
        super();
        this.data = collection;
    }

    public DSTTagDoubleCollection(String name, @NotNull Collection<Double> collection) {
        super();
        this.name = name;
        this.data = collection;
    }

    @Override
    public void read(@NotNull DataInput input) throws IOException {
        this.name = input.readUTF();
        int size = input.readInt();
        for (int i = 0; i < size; ++i)
            this.data.add(input.readDouble());
        if (!suffix.equals(input.readUTF()))
            throw new DSTFormatException();
    }

    @Override
    public void write(@NotNull DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.name);
        output.writeInt(this.data.size());
        for (double datum: this.data)
            output.writeDouble(datum);
        output.writeUTF(suffix);
    }

    public String getDSTName() {
        return this.name;
    }

    public void setDSTName(String name) {
        this.name = name;
    }

    public @NotNull Collection<Double> getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "DSTTagDoubleCollection{" +
                "name='" + this.name + '\'' +
                ", data=" + this.data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DSTTagDoubleCollection that)) return false;
        return Objects.equals(this.name, that.name) && this.data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.data);
    }
}