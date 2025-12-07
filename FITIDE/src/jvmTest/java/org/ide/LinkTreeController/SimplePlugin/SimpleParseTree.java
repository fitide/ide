package org.ide.LinkTreeController.SimplePlugin;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.ide.PluginController.PluginInterface.Position;
import org.ide.PluginController.PluginInterface.Tag;

import java.util.ArrayList;
import java.util.List;

public class SimpleParseTree implements ParseTree {
    Position fullPos;
    Position namePos;
    Position typePos;
    String name;
    String  type;
    ParseTree parent = null;
    List<ParseTree> body = new ArrayList<>();
    Tag valueType;
    Tag typeOfUsage;


    SimpleParseTree(String name, String type, Tag valueType, Tag typeOfUsage, Position namePos,
                    Position fullPos, Position typePos, List<ParseTree> body) {
        this.name = name;
        this.type = type;
        this.valueType = valueType;
        this.typeOfUsage = typeOfUsage;
        this.body = body;
        this.fullPos = fullPos;
        this.namePos = namePos;
        this.typePos = typePos;
    }

    @Override
    public ParseTree getParent() {
        return parent;
    }

    @Override
    public Object getPayload() {
        return new Pair<>(valueType, typeOfUsage);
    }

    @Override
    public ParseTree getChild(int i) {
        return body.get(i);
    }

    @Override
    public int getChildCount() {
        return body.size();
    }


    /**
     * @return type
     */
    @Override
    public String toStringTree() {
        return type;
    }

    @Override
    public void setParent(RuleContext parent) {
        this.parent = parent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
        return null;
    }

    /**
     * @return name
     */
    @Override
    public String getText() {
        return name;
    }

    @Override
    public String toStringTree(Parser parser) {
        return "";
    }

    @Override
    public Interval getSourceInterval() {
        return null;
    }

    public static class SimpleParseTreeBuilder {
        Position fullPos;
        Position namePos;
        Position typePos;
        String name;
        String  type;
        List<ParseTree> body = new ArrayList<>();
        Tag valueType;
        Tag typeOfUsage;

        public SimpleParseTreeBuilder() {};


        public ParseTree build() {
            return new SimpleParseTree(name, type, valueType, typeOfUsage, namePos, fullPos, typePos, body);
        }

        public SimpleParseTreeBuilder setFullPos(Position fullPos) {
            this.fullPos = fullPos;
            return this;
        }

        public SimpleParseTreeBuilder setNamePos(Position namePos) {
            this.namePos = namePos;
            return this;
        }

        public SimpleParseTreeBuilder setTypePos(Position typePos) {
            this.typePos = typePos;
            return this;
        }

        public SimpleParseTreeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SimpleParseTreeBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public SimpleParseTreeBuilder setBody(List<ParseTree> body) {
            this.body = body;
            return this;
        }

        public SimpleParseTreeBuilder setValueType(Tag valueType) {
            this.valueType = valueType;
            return this;
        }

        public SimpleParseTreeBuilder setTypeOfUsage(Tag typeOfUsage) {
            this.typeOfUsage = typeOfUsage;
            return this;
        }
    }
}
