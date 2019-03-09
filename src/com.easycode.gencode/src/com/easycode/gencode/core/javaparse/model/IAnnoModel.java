package com.easycode.gencode.core.javaparse.model;

public interface IAnnoModel
{
    public static String PROP = "prop";
    public static String CLS = "cls";
    public static String METHOD = "method";
    public String getAnno();
    public Range getAnnoRange();
    public Range getCodeRange();
    public String getType();
}
