package com.metabrain.djs.refactored.node;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class NodeBuilder {

    NodeStorage storage = NodeStorage.getInstance();
    Node node;

    public NodeBuilder create() {
        return create(NodeType.VAR);
    }

    public NodeBuilder create(byte type) {
        node = new Node();
        node.type = type;
        return this;
    }

    public NodeBuilder get(Long id) {
        node = storage.get(id);
        return this;
    }

    public NodeBuilder get(Node node) {
        this.node = node;
        return this;
    }

    public Node commit() {
        if (node.id == null)
            storage.add(node);
        else
            storage.set(node.id, node);
        if (!node.isSaved)
            storage.addToTransaction(node);
        return node;
    }

    public Long getId() {
        if (node.id == null)
            commit();
        return node.id;
    }

    public DataStream getData() {
        return node.data;
    }

    public NodeBuilder setData(String string) {
        setData(string.getBytes());
        return this;
    }

    public NodeBuilder setData(byte[] data) {
        setData(new ByteArrayInputStream(data));
        return this;
    }

    public NodeBuilder setData(InputStream stream) {
        node.externalData = stream;
        return this;
    }

    public Long getValue() {
        if (node.value instanceof Long)
            return (Long) node.value;
        else if (node.value instanceof Node)
            return node.id;
        return null;
    }

    public Long getSource() {
        if (node.source instanceof Long)
            return (Long) node.source;
        else if (node.source instanceof Node)
            return node.id;
        return null;
    }

    public Long getTitle() {
        if (node.title instanceof Long)
            return (Long) node.title;
        else if (node.title instanceof Node)
            return node.id;
        return null;
    }

    public Long getSet() {
        if (node.set instanceof Long)
            return (Long) node.set;
        else if (node.set instanceof Node)
            return node.id;
        return null;
    }

    public Long getTrue() {
        if (node._true instanceof Long)
            return (Long) node._true;
        else if (node._true instanceof Node)
            return node.id;
        return null;
    }

    public Long getElse() {
        if (node._else instanceof Long)
            return (Long) node._else;
        else if (node._else instanceof Node)
            return node.id;
        return null;
    }

    public Long getExit() {
        if (node.exit instanceof Long)
            return (Long) node.exit;
        else if (node.exit instanceof Node)
            return node.id;
        return null;
    }

    public Long getWhile() {
        if (node._while instanceof Long)
            return (Long) node._while;
        else if (node._while instanceof Node)
            return node.id;
        return null;
    }

    public Long getIf() {
        if (node._if instanceof Long)
            return (Long) node._if;
        else if (node._if instanceof Node)
            return node.id;
        return null;
    }

    public Long getPrototype() {
        if (node.prototype instanceof Long)
            return (Long) node.prototype;
        else if (node.prototype instanceof Node)
            return node.id;
        return null;
    }

    public Long getBody() {
        if (node.body instanceof Long)
            return (Long) node.body;
        else if (node.body instanceof Node)
            return node.id;
        return null;
    }

    public Node getValueNode() {
        if (node.value instanceof Node)
            return node;
        else if (node.value instanceof Long)
            return (Node) (node.value = storage.get((Long) node.value));
        return null;
    }

    public Node getSourceNode() {
        if (node.source instanceof Node)
            return node;
        else if (node.source instanceof Long)
            return (Node) (node.source = storage.get((Long) node.source));
        return null;
    }

    public Node getTitleNode() {
        if (node.title instanceof Node)
            return node;
        else if (node.title instanceof Long)
            return (Node) (node.title = storage.get((Long) node.title));
        return null;
    }

    public Node getSetNode() {
        if (node.set instanceof Node)
            return node;
        else if (node.set instanceof Long)
            return (Node) (node.set = storage.get((Long) node.set));
        return null;
    }

    public Node getTrueNode() {
        if (node._true instanceof Node)
            return node;
        else if (node._true instanceof Long)
            return (Node) (node._true = storage.get((Long) node._true));
        return null;
    }

    public Node getElseNode() {
        if (node._else instanceof Node)
            return node;
        else if (node._else instanceof Long)
            return (Node) (node._else = storage.get((Long) node._else));
        return null;
    }

    public Node getExitNode() {
        if (node.exit instanceof Node)
            return node;
        else if (node.exit instanceof Long)
            return (Node) (node.exit = storage.get((Long) node.exit));
        return null;
    }

    public Node getWhileNode() {
        if (node._while instanceof Node)
            return node;
        else if (node._while instanceof Long)
            return (Node) (node._while = storage.get((Long) node._while));
        return null;
    }

    public Node getIfNode() {
        if (node._if instanceof Node)
            return node;
        else if (node._if instanceof Long)
            return (Node) (node._if = storage.get((Long) node._if));
        return null;
    }

    public Node getPrototypeNode() {
        if (node.prototype instanceof Node)
            return node;
        else if (node.prototype instanceof Long)
            return (Node) (node.prototype = storage.get((Long) node.prototype));
        return null;
    }

    public Node getBodyNode() {
        if (node.body instanceof Node)
            return node;
        else if (node.body instanceof Long)
            return (Node) (node.body = storage.get((Long) node.body));
        return null;
    }

    public NodeBuilder setValue(Long value) {
        node.value = value;
        return this;
    }

    public NodeBuilder setSource(Long source) {
        node.source = source;
        return this;
    }

    public NodeBuilder setTitle(Long title) {
        node.title = title;
        return this;
    }

    public NodeBuilder setSet(Long set) {
        node.set = set;
        return this;
    }

    public NodeBuilder setTrue(Long _true) {
        node._true = _true;
        return this;
    }

    public NodeBuilder setElse(Long _else) {
        node._else = _else;
        return this;
    }

    public NodeBuilder setExit(Long exit) {
        node.exit = exit;
        return this;
    }

    public NodeBuilder setWhile(Long _while) {
        node._while = _while;
        return this;
    }

    public NodeBuilder setIf(Long _if) {
        node._if = _if;
        return this;
    }

    public NodeBuilder setPrototype(Long prototype) {
        node.prototype = prototype;
        return this;
    }

    public NodeBuilder setBody(Long body) {
        node.body = body;
        return this;
    }

    public NodeBuilder setValue(Node value) {
        node.value = value;
        return this;
    }

    public NodeBuilder setSource(Node source) {
        node.source = source;
        return this;
    }

    public NodeBuilder setTitle(Node title) {
        node.title = title;
        return this;
    }

    public NodeBuilder setSet(Node set) {
        node.set = set;
        return this;
    }

    public NodeBuilder setTrue(Node _true) {
        node._true = _true;
        return this;
    }

    public NodeBuilder setElse(Node _else) {
        node._else = _else;
        return this;
    }

    public NodeBuilder setExit(Node exit) {
        node.exit = exit;
        return this;
    }

    public NodeBuilder setWhile(Node _while) {
        node._while = _while;
        return this;
    }

    public NodeBuilder setIf(Node _if) {
        node._if = _if;
        return this;
    }

    public NodeBuilder setPrototype(Node prototype) {
        node.prototype = prototype;
        return this;
    }

    public NodeBuilder setBody(Node body) {
        node.body = body;
        return this;
    }


    public int getLocalCount() {
        return node.local != null ? node.local.size() : 0;
    }

    public int getParamCount() {
        return node.param != null ? node.param.size() : 0;
    }

    public int getNextCount() {
        return node.next != null ? node.next.size() : 0;
    }

    public int getCellCount() {
        return node.cell != null ? node.cell.size() : 0;
    }

    public int getPropertiesCount() {
        return node.properties != null ? node.properties.size() : 0;
    }

    private Node getListNode(ArrayList<Object> list, int index) {
        if (list != null && list.size() > index) {
            Object object = list.get(index);
            if (object instanceof Node) return (Node) object;
            if (object instanceof Long) return storage.get((Long) object);
        }
        return null;
    }

    public Node getLocal(int index) {
        return getListNode(node.local, index);
    }

    public Node getParam(int index) {
        return getListNode(node.param, index);
    }

    public Node getNext(int index) {
        return getListNode(node.next, index);
    }

    public Node getCell(int index) {
        return getListNode(node.cell, index);
    }

    public Node getProperty(int index) {
        return getListNode(node.properties, index);
    }

    public NodeBuilder addLocal(Long id) {
        if (node.local == null)
            node.local = new ArrayList<>();
        node.local.add(id);
        return this;
    }

    public NodeBuilder addParam(Long id) {
        if (node.param == null)
            node.param = new ArrayList<>();
        node.param.add(id);
        return this;
    }

    public NodeBuilder addNext(Long id) {
        if (node.next == null)
            node.next = new ArrayList<>();
        node.next.add(id);
        return this;
    }

    public NodeBuilder addCell(Long id) {
        if (node.cell == null)
            node.cell = new ArrayList<>();
        node.cell.add(id);
        return this;
    }

    public NodeBuilder addProperty(Long id) {
        if (node.properties == null)
            node.properties = new ArrayList<>();
        node.properties.add(id);
        return this;
    }

    public NodeBuilder addLocal(Node item) {
        if (node.local == null)
            node.local = new ArrayList<>();
        node.local.add(item);
        return this;
    }

    public NodeBuilder addParam(Node item) {
        if (node.param == null)
            node.param = new ArrayList<>();
        node.param.add(item);
        return this;
    }

    public NodeBuilder addNext(Node item) {
        if (node.next == null)
            node.next = new ArrayList<>();
        node.next.add(item);
        return this;
    }

    public NodeBuilder addCell(Node item) {
        if (node.cell == null)
            node.cell = new ArrayList<>();
        node.cell.add(item);
        return this;
    }

    public NodeBuilder addProperty(Node item) {
        if (node.properties == null)
            node.properties = new ArrayList<>();
        node.properties.add(item);
        return this;
    }

    public void removeFromListNode(ArrayList<Object> list, Long nodeId) {
        if (list != null && nodeId != null) {
            Object find = null;
            for (Object item : list) {
                if (item instanceof Long && nodeId.equals(item)
                        || item instanceof Node && nodeId.equals(((Node) item).id)) {
                    find = item;
                    break;
                }
            }
            if (find != null)
                list.remove(find);
        }
    }

    public NodeBuilder removeLocal(Long nodeId) {
        removeFromListNode(node.local, nodeId);
        return this;
    }

    public NodeBuilder removeParam(Long nodeId) {
        removeFromListNode(node.param, nodeId);
        return this;
    }

    public NodeBuilder removeNext(Long nodeId) {
        removeFromListNode(node.next, nodeId);
        return this;
    }

    public NodeBuilder removeCell(Long nodeId) {
        removeFromListNode(node.cell, nodeId);
        return this;
    }

    public NodeBuilder removeProperty(Long nodeId) {
        removeFromListNode(node.properties, nodeId);
        return this;
    }

    public NodeBuilder removeLocal(Node item) {
        if (item != null) removeFromListNode(node.local, item.id);
        return this;
    }

    public NodeBuilder removeParam(Node item) {
        if (item != null) removeFromListNode(node.local, item.id);
        return this;
    }

    public NodeBuilder removeNext(Node item) {
        if (item != null) removeFromListNode(node.local, item.id);
        return this;
    }

    public NodeBuilder removeCell(Node item) {
        if (item != null) removeFromListNode(node.local, item.id);
        return this;
    }

    public NodeBuilder removeProperty(Node item) {
        if (item != null) removeFromListNode(node.local, item.id);
        return this;
    }

    public NodeBuilder setFunctionId(long functionId) {
        node.functionId = functionId;
        return this;
    }

    public Node makeLocal(String name) {
        return null;
    }
}
