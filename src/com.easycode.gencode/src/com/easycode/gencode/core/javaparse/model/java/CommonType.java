package com.easycode.gencode.core.javaparse.model.java;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CommonType implements Serializable
{
    //private boolean withStatic = false;
   // private boolean withFinal = false;
    //private boolean withAbstract = false;
    
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

    public boolean equals(Object o)
    {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
/*
    public boolean isWithStatic()
    {
        return withStatic;
    }

    public void setWithStatic(boolean withStatic)
    {
        this.withStatic = withStatic;
    }

    public boolean isWithFinal()
    {
        return withFinal;
    }

    public void setWithFinal(boolean withFinal)
    {
        this.withFinal = withFinal;
    }

    public boolean isWithAbstract()
    {
        return withAbstract;
    }

    public void setWithAbstract(boolean withAbstract)
    {
        this.withAbstract = withAbstract;
    }
  */
}
