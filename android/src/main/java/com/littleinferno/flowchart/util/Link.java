package com.littleinferno.flowchart.util;

import java.util.List;

public class Link {
    private List<Link> list;
    private Fun fun;

    public Link(List<Link> list, Fun fun) {
        this.list = list;
        this.fun = fun;
    }

    public void call() {
        if (isConnect())
            fun.call();
    }

    public void disconnect() {
        if (list != null)
            list.remove(this);
        list = null;
        fun = null;
    }

    public boolean isConnect() {
        return fun != null;
    }
}
