<#assign pkgs=",">
package ${sys.pkg.pkgName};
 <#list sys.propList as prop>
 <#if pkgs?index_of(","+prop.propType.pkgName+'.'+prop.propType.clzName+",") == -1>
import ${prop.propType.pkgName}.${prop.propType.clzName};<#assign pkgs=pkgs+prop.propType.pkgName+'.'+prop.propType.clzName+",">
 </#if>
 </#list>
public class ${sys.clzName}
{
 <#list sys.propList as prop>
     protected ${prop.propType.clzName} ${prop.propName} = null;
 </#list>
 <#list sys.propList as prop>
     public void set${(prop.propName)?cap_first}(${prop.propType.clzName} ${prop.propName})
     {
         this.${prop.propName} = ${prop.propName};
     }
     public ${prop.propType.clzName} get${(prop.propName)?cap_first}()
     {
         return this.${prop.propName};
     }
 </#list>
}