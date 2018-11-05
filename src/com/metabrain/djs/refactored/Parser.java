package com.metabrain.djs.refactored;

import com.metabrain.djs.refactored.node.Node;
import com.metabrain.djs.refactored.node.NodeBuilder;
import com.metabrain.djs.refactored.node.NodeType;
import jdk.nashorn.internal.ir.*;
import jdk.nashorn.internal.parser.TokenType;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

public class Parser {

    private NodeBuilder builder = new NodeBuilder();

    Node jsLine(Node module, jdk.nashorn.internal.ir.Node statement) {

        if (statement instanceof VarNode) {
            VarNode varNode = (VarNode) statement;
            Node node = jsLine(module, varNode.getName());
            if (varNode.getInit() != null) {
                Node setLink = jsLine(node, varNode.getInit());
                node = builder.create()
                        .setSource(node)
                        .setSet(setLink)
                        .commit();
            }
            return node;
        }

        if (statement instanceof ForNode) {
            ForNode forNode = (ForNode) statement;
            // TODO problem with parsing for init
            Node forInitNode = builder.create().commit();
            builder.set(module).addLocal(forInitNode).commit();
            builder.set(forInitNode).addNext(jsLine(forInitNode, forNode.getInit()));
            Node forBodyNode = jsLine(forInitNode, forNode.getBody());
            Node forStartNode = builder.create()
                    .setWhile(forBodyNode)
                    .setIf(jsLine(forInitNode, forNode.getTest()))
                    .commit();
            Node forModify = jsLine(forInitNode, forNode.getModify());
            builder.set(forBodyNode).addNext(forModify).commit();
            builder.set(forInitNode).addNext(forStartNode);
            builder.set(module).removeLocal(forInitNode);
            return builder.set(forInitNode).removeLocal(forStartNode).commit(); // TODO remove this line
        }

        if (statement instanceof UnaryNode) {
            UnaryNode unaryNode = (UnaryNode) statement;
            // TODO add all unary ++a --a a++ a--
            if (unaryNode.tokenType() == TokenType.SUB)
                return builder.create(NodeType.FUNCTION)
                        .setFunctionId(Caller.UNARY_MINUS)
                        .addParam(jsLine(module, unaryNode.getExpression()))
                        .commit();
        }

        if (statement instanceof ExpressionStatement) {
            ExpressionStatement expressionStatement = (ExpressionStatement) statement;
            return jsLine(module, expressionStatement.getExpression());
        }

        if (statement instanceof BinaryNode) {
            BinaryNode binaryNode = (BinaryNode) statement;
            if (binaryNode.isAssignment()) {
                Node left = jsLine(module, binaryNode.lhs());
                Node right = jsLine(module, binaryNode.rhs());
                if (binaryNode.tokenType() == TokenType.ASSIGN) {
                    return builder.create()
                            .setSource(left)
                            .setSet(right)
                            .commit();
                } else {
                    Node functionCalc = builder.create(NodeType.FUNCTION)
                            .setFunctionId(Caller.fromTokenType(binaryNode.tokenType()))
                            .addParam(left)
                            .addParam(right)
                            .commit();
                    return builder.create()
                            .setSource(left)
                            .setSet(functionCalc)
                            .commit();
                }
            }
            if (binaryNode.isComparison()) {
                Node left = jsLine(module, binaryNode.lhs());
                Node right = jsLine(module, binaryNode.rhs());
                return builder.create(NodeType.FUNCTION)
                        .setFunctionId(Caller.fromTokenType(binaryNode.tokenType()))
                        .addParam(left)
                        .addParam(right)
                        .commit();
            }
        }


        if (statement instanceof Block) {
            Block block = (Block) statement;
            for (jdk.nashorn.internal.ir.Node line : block.getStatements()) {
                Node lineNode = jsLine(module, line);
                builder.set(module).addNext(lineNode);
            }
            return builder.set(module).commit();
        }

        if (statement instanceof IfNode) {
            IfNode ifStatement = (IfNode) statement;
            Node ifQuestionNode = jsLine(module, ifStatement.getTest());
            Node ifTrueNode = jsLine(module, ifStatement.getPass());
            Node ifElseNode = null;
            if (ifStatement.getFail() != null) {
                ifElseNode = jsLine(module, ifStatement.getFail());
                return builder.create()
                        .setIf(ifQuestionNode)
                        .setTrue(ifTrueNode)
                        .setElse(ifElseNode)
                        .commit();
            }
        }

        if (statement instanceof FunctionNode) {
            FunctionNode functionNode = (FunctionNode) statement;
            Node func = builder.create().getNode();
            for (IdentNode param : functionNode.getParameters()) {
                Node titleData = builder.create(NodeType.STRING).setData(param.getName()).commit();
                Node paramNode = builder.create().setTitle(titleData).commit();
                builder.set(func).addParam(paramNode);
            }
            builder.set(func).commit();
            return jsLine(func, functionNode.getBody());
        }

        if (statement instanceof ReturnNode) {
            ReturnNode returnNode = (ReturnNode) statement;
            Node setNode = jsLine(module, returnNode.getExpression());
            return builder.create()
                    .setSource(module)
                    .setSet(setNode)
                    .setExit(module)
                    .commit();
        }

        if (statement instanceof IdentNode) {
            IdentNode identNode = (IdentNode) statement;

            Node ident = builder.set(module).findLocal(identNode.getName());
            if (ident == null) {
                ident = builder.create().commit();
                Node titleData = builder.create(NodeType.STRING).setData(identNode.getName()).commit();
                builder.set(ident).setTitle(titleData).commit();
                builder.set(module).addLocal(ident).commit();
            }
            return ident;
        }

        if (statement instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) statement;
            Node obj = builder.create().commit();
            builder.set(module).addLocal(obj).commit(); // TODO commit ?
            for (PropertyNode item : objectNode.getElements())
                builder.set(obj).addProperty(jsLine(obj, item)); // TODO add 3th param to jsLine $add_to_local_path
            builder.set(module).removeLocal(obj).commit(); // TODO commit ?
            return builder.set(obj).commit();
        }

        if (statement instanceof PropertyNode) {
            PropertyNode propertyNode = (PropertyNode) statement;
            String key = "";
            if (propertyNode.getKey() instanceof LiteralNode) {
                LiteralNode literalNode = (LiteralNode) propertyNode.getKey();
                key = literalNode.getString();
            }
            if (propertyNode.getValue() instanceof FunctionCall) {
                Node body = jsLine(module, propertyNode.getValue());
                Node titleData = builder.create(NodeType.STRING).setData(key).commit();
                return builder.set(module)
                        .addLocal(builder.create()
                                .setTitle(titleData)
                                .setBody(body)
                                .getId()
                        ).commit();
            } else {
                Node value = jsLine(module, propertyNode.getValue());
                Node titleData = builder.create(NodeType.STRING).setData(key).commit();
                builder.set(value).setTitle(titleData).commit();
                return builder.set(module)
                        .addLocal(value)
                        .commit();
            }
        }


        if (statement instanceof CallNode) { // TODO NewExpression
            CallNode call = (CallNode) statement;
            Node callNode = jsLine(module, call.getFunction());
            if (call.getArgs().size() > 0) {
                for (jdk.nashorn.internal.ir.Node arg : call.getArgs()) {
                    Node argNode = jsLine(module, arg);
                    builder.set(callNode).addParam(argNode);
                }
            } else {
                builder.set(callNode).addParam(0L);
            }
            builder.commit();
            return callNode;
        }

        if (statement instanceof LiteralNode) {
            LiteralNode literalNode = (LiteralNode) statement;

            if (literalNode instanceof LiteralNode.ArrayLiteralNode) {
                LiteralNode.ArrayLiteralNode arrayLiteralNode = (LiteralNode.ArrayLiteralNode) literalNode;
                Node arr = builder.create(NodeType.ARRAY).commit();
                for (jdk.nashorn.internal.ir.Node item : arrayLiteralNode.getElementExpressions()) {
                    Node itemNode = jsLine(module, item);
                    builder.set(arr).addCell(itemNode);
                }
                return arr;
            }
            byte nodeType = NodeType.BOOL;
            if (literalNode.isNumeric())
                nodeType = NodeType.NUMBER;
            else if (literalNode.isString())
                nodeType = NodeType.STRING;
            Node value = builder.create(nodeType)
                    .setData(literalNode.getString())
                    .commit();
            return builder.create()
                    .setValue(value)
                    .commit();
        }
        return null;
    }

    public Node parse(String sourceString) {
        Options options = new Options("nashorn");
        options.set("anon.functions", true);
        options.set("parse.only", true);
        options.set("scripting", true);
        ErrorManager errors = new ErrorManager();
        Context context = new Context(options, errors, Thread.currentThread().getContextClassLoader());
        Source source = Source.sourceFor("test", sourceString);
        jdk.nashorn.internal.parser.Parser parser = new jdk.nashorn.internal.parser.Parser(context.getEnv(), source, errors);
        // TODO catch parse error
        Node module = builder.create().commit();
        jsLine(module, parser.parse().getBody());
        return module;
    }


}
