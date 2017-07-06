package expression;

import instruction.Block;

/**
 * // TODO write javadoc
 *
 * @author zmayhew
 */
public class NullExpression extends Expression {
    @Override
    public double evaluate(Block block) {
        return 0.0d;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public String getStringValue() {
        return "null";
    }
}
