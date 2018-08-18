1.读取预设参数的值
<#list sys.propList as prop>
    ${prop_index+1} ${prop.uiTitle}:${prop.propName}<#if prop_has_next>,</#if>
</#list>

2.读取用户自定义的值
  ${user.参数名}
  
3.读取用户ID
  ${config.userId}
  
  
 
  设计单元:
 A:通用工具(com.easycode.common)               
 B:模板管理(com.easycode.templatemgr)          
   
 C:编译对象抽象层(com.easycode.javaparse)      
    com.easycode.javaparse.compile 
    com.easycode.javaparse.anno
 D:UML图      (com.easycode.uml)                  
 E:代码生成(com.easycode.gencode)              
          程序单元：com.easycode.gencode.action
          程序单元：com.easycode.gencode.ui
 F:表反向POJO对象工具                                                                     
   com.easycode.dbtopojo
   
   
 J:JSP编辑工具
   com.easycode.jspedit
   
 H:资源
 com.easycode.resource
 

 
 
 
 
 