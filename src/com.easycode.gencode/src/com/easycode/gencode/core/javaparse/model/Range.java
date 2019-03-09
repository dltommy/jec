package com.easycode.gencode.core.javaparse.model;

public class Range
{

    int begin = 0;
    int end = 0;
    public Range(int begin,int end)
    {
        this.begin = begin;
        this.end = end;
    }
    public int getBegin()
    {
        return begin;
    }
    public void setBegin(int begin)
    {
        this.begin = begin;
    }
    public int getEnd()
    {
        return end;
    }
    public void setEnd(int end)
    {
        this.end = end;
    }
    

}
