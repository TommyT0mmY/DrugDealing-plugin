package com.github.tommyt0mmy.drugdealing.utility;

public enum CriminalRole
{
    PRODUCER(0),
    DEALER(1);

    private int id;

    CriminalRole(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
