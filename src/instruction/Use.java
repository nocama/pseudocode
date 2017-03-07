package instruction;

import java.awt.*;

/**
 * Use is like import.  On use execution, the other file is parsed,
 * and its functions and fields are copied into the main block
 *
 * @author zmayhew
 */
public class Use extends Instruction {
    @Override
    public void execute(Graphics graphics, Block block) {

    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Instruction instruction, Block block) {
        return false;
    }
}
