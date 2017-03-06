package expression;

import instruction.Block;

import java.awt.*;

/**
 * // TODO write javadoc
 *
 * @author zmayhew
 */
public class Function {

    private String name;
    private Block block;
    private String[] initArgs;

    public Function(String name, Block block, String[] initArgs) {
        this.name = name;
        this.block = block;
        this.initArgs = initArgs;
    }

    public void execute(Graphics graphics, Expression[] arguments) {
        if (arguments.length != initArgs.length) {
            System.out.println("invalid args");
            return;
        }
        for (int i = 0; i < initArgs.length; i++) {
            block.assign(initArgs[i], arguments[i]);
        }
        block.execute(graphics, block);
    }

    /**
     * TODO: Create javadoc
     */
    public String getName() {
        return name;
    }

    /**
     * TODO: Create javadoc
     */
    public Block getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "Function {" +
                "name='" + name + '\'' +
                ", block=" + block.toString() +
                '}';
    }
}
