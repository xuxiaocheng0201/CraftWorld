package CraftWorld.Chunk;

import CraftWorld.Block.BlockPos;
import CraftWorld.DST.DSTUtils;
import CraftWorld.DST.IDSTBase;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Logger.HELogLevel;
import HeadLibs.Logger.HLog;
import HeadLibs.Registerer.HElementRegisteredException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serial;
import java.math.BigInteger;
import java.util.Objects;

public class ChunkPos implements IDSTBase {
    @Serial
    private static final long serialVersionUID = 1974205833401624407L;

    private BigInteger x, y, z;

    public ChunkPos() {
        this(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public ChunkPos(int x, int y, int z) {
        super();
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public ChunkPos(BigInteger x, BigInteger y, BigInteger z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static final String id = "ChunkPos";
    public static final String prefix = id;
    static {
        try {
            DSTUtils.getInstance().register(id, ChunkPos.class);
        } catch (HElementRegisteredException exception) {
            HLog.logger(HELogLevel.ERROR, exception);
        }
    }

    private String name = id;

    @Override
    public String getDSTName() {
        return this.name;
    }

    @Override
    public void setDSTName(String name) {
        this.name = name;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.x = new BigInteger(input.readUTF());
        this.y = new BigInteger(input.readUTF());
        this.z = new BigInteger(input.readUTF());
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(prefix);
        output.writeUTF(this.x.toString());
        output.writeUTF(this.y.toString());
        output.writeUTF(this.z.toString());
    }

    public void setX(int x) {
        this.x = BigInteger.valueOf(x);
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = BigInteger.valueOf(y);
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = BigInteger.valueOf(z);
    }

    public void setZ(BigInteger z) {
        this.z = z;
    }

    public void set(int x, int y, int z) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
        this.z = BigInteger.valueOf(z);
    }

    public void set(BigInteger x, BigInteger y, BigInteger z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(ChunkPos pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public int getX() {
        return this.x.intValue();
    }

    public BigInteger getBigX() {
        return this.x;
    }

    public int getY() {
        return this.y.intValue();
    }

    public BigInteger getBigY() {
        return this.y;
    }

    public int getZ() {
        return this.z.intValue();
    }

    public BigInteger getBigZ() {
        return this.z;
    }

    public void addX(int x) {
        this.x = this.x.add(BigInteger.valueOf(x));
    }

    public void addX(BigInteger x) {
        this.x = this.x.add(x);
    }

    public void addY(int y) {
        this.y = this.y.add(BigInteger.valueOf(y));
    }

    public void addY(BigInteger y) {
        this.y = this.y.add(y);
    }

    public void addZ(int z) {
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void addZ(BigInteger z) {
        this.z = this.z.add(z);
    }

    public void add(int x, int y, int z) {
        this.x = this.x.add(BigInteger.valueOf(x));
        this.y = this.y.add(BigInteger.valueOf(y));
        this.z = this.z.add(BigInteger.valueOf(z));
    }

    public void add(BigInteger x, BigInteger y, BigInteger z) {
        this.x = this.x.add(x);
        this.y = this.y.add(y);
        this.z = this.z.add(z);
    }

    public void add(ChunkPos pos) {
        this.x = this.x.add(pos.x);
        this.y = this.y.add(pos.y);
        this.z = this.z.add(pos.z);
    }

    public void subtractX(int x) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
    }

    public void subtractX(BigInteger x) {
        this.x = this.x.subtract(x);
    }

    public void subtractY(int y) {
        this.y = this.y.subtract(BigInteger.valueOf(y));
    }

    public void subtractY(BigInteger y) {
        this.y = this.y.subtract(y);
    }

    public void subtractZ(int z) {
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtractZ(BigInteger z) {
        this.z = this.z.subtract(z);
    }

    public void subtract(int x, int y, int z) {
        this.x = this.x.subtract(BigInteger.valueOf(x));
        this.y = this.y.subtract(BigInteger.valueOf(y));
        this.z = this.z.subtract(BigInteger.valueOf(z));
    }

    public void subtract(BigInteger x, BigInteger y, BigInteger z) {
        this.x = this.x.subtract(x);
        this.y = this.y.subtract(y);
        this.z = this.z.subtract(z);
    }

    public void subtract(ChunkPos pos) {
        this.x = this.x.subtract(pos.x);
        this.y = this.y.subtract(pos.y);
        this.z = this.z.subtract(pos.z);
    }

    public void up() {
        this.y = this.y.add(BigInteger.ONE);
    }

    public void up(int n) {
        this.y = this.y.add(BigInteger.valueOf(n));
    }

    public void up(BigInteger n) {
        this.y = this.y.add(n);
    }

    public void down() {
        this.y = this.y.subtract(BigInteger.ONE);
    }

    public void down(int n) {
        this.y = this.y.subtract(BigInteger.valueOf(n));
    }

    public void down(BigInteger n) {
        this.y = this.y.subtract(n);
    }

    public void north() {
        this.x = this.x.add(BigInteger.ONE);
    }

    public void north(int n) {
        this.x = this.x.add(BigInteger.valueOf(n));
    }

    public void north(BigInteger n) {
        this.x = this.x.add(n);
    }

    public void south() {
        this.x = this.x.subtract(BigInteger.ONE);
    }

    public void south(int n) {
        this.x = this.x.subtract(BigInteger.valueOf(n));
    }

    public void south(BigInteger n) {
        this.x = this.x.subtract(n);
    }

    public void east() {
        this.z = this.z.add(BigInteger.ONE);
    }

    public void east(int n) {
        this.z = this.z.add(BigInteger.valueOf(n));
    }

    public void east(BigInteger n) {
        this.z = this.z.add(n);
    }

    public void west() {
        this.z = this.z.subtract(BigInteger.ONE);
    }

    public void west(int n) {
        this.z = this.z.subtract(BigInteger.valueOf(n));
    }

    public void west(BigInteger n) {
        this.z = this.z.subtract(n);
    }

    public void offset(BlockPos.EFacing facing) {
        switch (facing) {
            case UP -> this.up();
            case DOWN -> this.down();
            case EAST -> this.east();
            case WEST -> this.west();
            case NORTH -> this.north();
            case SOUTH -> this.south();
        }
    }

    public void offset(BlockPos.EFacing facing, int n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    public void offset(BlockPos.EFacing facing, BigInteger n) {
        switch (facing) {
            case UP -> this.up(n);
            case DOWN -> this.down(n);
            case EAST -> this.east(n);
            case WEST -> this.west(n);
            case NORTH -> this.north(n);
            case SOUTH -> this.south(n);
        }
    }

    @Override
    public String toString() {
        return HStringHelper.concat("ChunkPos{",
                "x=", this.x,
                ", y=", this.y,
                ", z=", this.z,
                '}');
    }

    @Override
    public boolean equals(Object a) {
        if (!(a instanceof ChunkPos))
            return false;
        return Objects.equals(this.x, ((ChunkPos) a).x) &&
                Objects.equals(this.y, ((ChunkPos) a).y) &&
                Objects.equals(this.z, ((ChunkPos) a).z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }
}
