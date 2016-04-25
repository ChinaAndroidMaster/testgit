package com.creditease.checkcars.ui.ppwindow.defaultp;

import java.io.Serializable;

public class DefaultObj implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 3485995793197821239L;

    public String id;
    public String name;

    public DefaultObj()
    {
        super();
    }

    public DefaultObj(String name)
    {
        super();
        this.name = name;
    }

    public DefaultObj(String id, String name)
    {
        super();
        this.id = id;
        this.name = name;
    }

}
