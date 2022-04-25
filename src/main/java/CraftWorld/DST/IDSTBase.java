package CraftWorld.DST;

import Core.Addition.Element.ElementImplement;
import Core.Addition.Element.NewElementImplementCore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

@NewElementImplementCore(modName = "CraftWorld", elementName = "DST")
public interface IDSTBase extends ElementImplement, Serializable {
    void read(DataInput input) throws IOException;
    void write(DataOutput output) throws IOException;
}
