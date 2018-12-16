<ftl_head>
{
    'node1':'1',
    'node2':'2',
    'node3':'3'
}
</ftl_head>
  
<#if checkedList?seq_contains("node1")>
   1
</#if>

<#if checkedList?seq_contains("node2")>
   2
</#if>

<#if checkedList?seq_contains("node3")>
   3
</#if>