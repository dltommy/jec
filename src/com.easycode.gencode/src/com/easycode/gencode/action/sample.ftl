<ftl_head>
{
    'node_name':'说明',
    'node1':'关于<ftl_head>',
    'node2':
    {
        'node_name':'测试',
        'node2_1':'节点2_1',
        'node2_2':'节点2_2',
        'node2_3':'节点2_3'
    },
    'node3':'读取class属性值',
    'node4':'json对象解析',

    'node5':'freemarker语法说明',
    'node6':'文件生成'

}
</ftl_head>
<ftl_doc>样例</ftl_doc>
<#if checkedList?seq_contains("node1")>
用json串描述一个节点
对于有子节点的节点，可以用node_name指明父节点的名称:
eg:
    'node2':
    {
        'node_name':'测试',
        'node2_1':'节点2_1',
        'node2_2':'节点2_2'
    },
  等同于    
    '测试':
    {
        'node2_1':'节点2_1',
        'node2_2':'节点2_2'
    },

</#if>

<#if checkedList?seq_contains("node2_1")>
选中节点"node2_1"
</#if>

<#if checkedList?seq_contains("node2_2")>
选中节点"node2_2"
<ftl_doc>关于node2_2节点的文档说明</ftl_doc>
</#if>

<#if checkedList?seq_contains("node2_3")>
选中节点"node2_3"
<ftl_doc>关于node2_3节点的文档说明</ftl_doc>
</#if>

<#if checkedList?seq_contains("node3")>
   参考预设参数，读取数据，预设参数以sys做前缀，
eg:sys.propList, sys.clzName
  <#list sys.propList as e>
<#lt>${e_index+1} 类别：${e.propType.clzName},属性名：${e.propName}<#if e_has_next>,</#if> 
  </#list>
</#if> 

<#if checkedList?seq_contains("node4")>
  json对象(eg:{key1:val1,key2:val2,key3:val3})会被自动解析成hashMap对象，
  如下遍历json对象的所有值：

 <#list sys.propList as e>
    <#assign testMap = e.propType>
     <#list testMap?keys as key> 
       ${key}:${testMap[key]} 
   </#list>

 </#list>
</#if>
<#if checkedList?seq_contains("node5")>
注释方式${r"<#--注释-->"}.
eg:<#--注释内容-->
</#if>
<#if checkedList?seq_contains("node6")>
 path属性相对于eclipse工作台上项目名称路径.
   <ftl_file path="GenConfig/helloworld.txt">
     hello world!
   </ftl_file>
</#if>
