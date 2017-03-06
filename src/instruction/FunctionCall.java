package instruction;

import expression.Expression;
import expression.Function;
import expression.SymbolTerminal;

import java.awt.*;

/**
 * // TODO write javadoc
 *
 * @author zmayhew
 */
public class FunctionCall extends Instruction {

    private String name;
    private Expression[] arguments;

    public FunctionCall(String name, Expression[] arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public void execute(Graphics graphics, Block block) {
        Function fun = block.getFunction(name);
        fun.execute(graphics, arguments);
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
