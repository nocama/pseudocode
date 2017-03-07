package instruction;

import expression.Expression;
import view.Pseudocode;

import java.awt.*;

/**
 * // TODO write javadoc
 *
 * @author zmayhew
 */
public class Sleep extends Instruction {

    private int unit;
    private Expression time;

    public Sleep(int unit, Expression time) {
        this.unit = unit;
        this.time = time;
    }

    @Override
    public void execute(Graphics graphics, Block block) {
        int t = (int) (unit * time.evaluate(block));
        Thread.currentThread();
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
