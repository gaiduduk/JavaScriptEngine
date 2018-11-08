package com.metabrain.djs.refactored;

import com.metabrain.djs.refactored.node.Node;
import com.metabrain.djs.refactored.node.NodeBuilder;
import com.metabrain.djs.refactored.node.NodeType;
import jdk.nashorn.internal.parser.TokenType;

public class Caller {

    public final static int UNARY_MINUS = -1;
    public final static int EQ = 0; // ==
    public final static int ADD = 1; // +
    public final static int SUB = 2; // -
    public final static int DIV = 3; // /
    public final static int MUL = 4; // *
    public final static int INC = 5; // +1
    public final static int DEC = 6; // -1
    public final static int GT = 7; // >
    public final static int GE = 8; // >=
    public final static int LT = 9; // <
    public final static int LE = 10; // <=
    private static NodeBuilder builder = new NodeBuilder();

    static int fromTokenType(TokenType tokenType) {
        switch (tokenType) {
            case EQ:
                return EQ;
            case ADD:
                return ADD;
            case SUB:
                return SUB;
            case DIV:
                return DIV;
            case MUL:
                return MUL;
            case ASSIGN_ADD:
                return ADD;
            case ASSIGN_SUB:
                return SUB;
            case ASSIGN_DIV:
                return DIV;
            case ASSIGN_MUL:
                return MUL;
            case INCPOSTFIX:
                return INC;
            case DECPOSTFIX:
                return DEC;
            case GT:
                return GT;
            case GE:
                return GE;
            case LT:
                return LT;
            case LE:
                return LE;
        }
        return EQ;
    }

    static Node trueValue = builder.create(NodeType.BOOL).setData("true").commit();
    static Node falseValue = builder.create(NodeType.BOOL).setData("false").commit();

    static void invoke(Node node, Node calledNodeId) {
        builder.set(node);

        Node left = builder.getParamNode(0);
        Node right = builder.getParamNode(1);

        if (left != null) left = builder.set(left).getValueOrSelf();
        if (right != null) right = builder.set(right).getValueOrSelf();

        Object leftObject = null;
        Object rightObject = null;

        if (left != null && left.type < NodeType.VAR) leftObject = builder.set(left).getData().getObject();
        if (right != null && right.type < NodeType.VAR) rightObject = builder.set(right).getData().getObject();

        Node resultNode = null;
        try {
            switch (node.functionId) {
                case EQ:
                    resultNode = (leftObject != null && leftObject.equals(rightObject)) ? trueValue : falseValue;
                    break;
                case ADD:
                    if (leftObject instanceof String && rightObject instanceof String) {
                        String newString = (String) leftObject + (String) rightObject;
                        resultNode = builder.create(NodeType.STRING).setData(newString).commit();
                    } else if (leftObject instanceof Double && rightObject instanceof Double) {
                        Double newString = (Double) leftObject + (Double) rightObject;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case SUB:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        Double newString = (Double) leftObject - (Double) rightObject;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case MUL:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        Double newString = (Double) leftObject * (Double) rightObject;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case DIV:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        Double newString = (Double) leftObject / (Double) rightObject;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case UNARY_MINUS:
                    if (leftObject instanceof Double) {
                        Double newString = -(Double) leftObject;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case INC:
                    if (leftObject instanceof Double) {
                        Double newString = (Double) leftObject + 1;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case DEC:
                    if (leftObject instanceof Double) {
                        Double newString = (Double) leftObject - 1;
                        resultNode = builder.create(NodeType.NUMBER).setData(newString).commit();
                    }
                    break;
                case GT:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        resultNode = ((Double) leftObject > (Double) rightObject) ? trueValue : falseValue;
                    }
                    break;
                case GE:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        resultNode = ((Double) leftObject >= (Double) rightObject) ? trueValue : falseValue;
                    }
                    break;
                case LT:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        resultNode = ((Double) leftObject < (Double) rightObject) ? trueValue : falseValue;
                    }
                    break;
                case LE:
                    if (leftObject instanceof Double && rightObject instanceof Double) {
                        resultNode = ((Double) leftObject <= (Double) rightObject) ? trueValue : falseValue;
                    }
                    break;
            }
        } catch (Exception e) {
            // TODO add exceptions to djs
        }
        builder.set(node).setValue(resultNode).commit();
    }
}
