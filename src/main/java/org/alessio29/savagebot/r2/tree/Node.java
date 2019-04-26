package org.alessio29.savagebot.r2.tree;

public abstract class Node {
    private final String text;

    public Node(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
