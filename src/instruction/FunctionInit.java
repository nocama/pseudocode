package instruction;

import expression.Function;
import expression.NullExpression;
import expression.SymbolTerminal;

import java.awt.*;
import java.util.Arrays;

/**
 * // TODO write javadoc
 *
 * @author zmayhew
 */
public class FunctionInit extends Instruction {

    private String name;
    private Block block;
    private SymbolTerminal[] arguments;

    public FunctionInit(String name, Block block, SymbolTerminal[] arguments) {
        this.name = name;
        this.block = block;
        this.arguments = arguments;
    }

    @Override
    public void execute(Graphics graphics, Block block) {
        String[] argTemplate = new String[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            SymbolTerminal s = arguments[i];
            this.block.assign(s, new NullExpression());
            argTemplate[i] = arguments[i].toString();
        }
        Function f = new Function(name, this.block, argTemplate);
        block.addFunction(name, f);
    }

    @Override
    public String toString() {
        return String.format("FunctionInit (name: %s, block: %s, arguments: %s)", name, block.toString(), Arrays.toString(arguments));
    }

    @Override
    public boolean equals(Instruction instruction, Block block) {
        return false;
    }
}
