package com.khoa.ptittools.main.bottomnavigation;

import java.util.ArrayList;
import java.util.List;

public class MyBackStack {

    private List<String> backStack;

    public MyBackStack() {
        backStack = new ArrayList<>();
    }

    public void push(String string) {
        backStack.remove(string);
        backStack.add(string);
    }

    public String pop() {
        backStack.remove(backStack.get(backStack.size() - 1));
        return backStack.get(backStack.size() - 1);
    }

    public boolean isEnd() {
        return backStack.size() <= 1;
    }
}
