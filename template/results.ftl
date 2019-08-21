import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Mapper
@Component
public interface ${className}Mapper extends BaseMapper<${className}> {

@Select("SELECT * FROM ${tableName}  ${r'${ew.customSqlSegment}'}") // todo: 需要给表起一个合适的别名
@Results({
@Result(id = true,column = "id",property = "id"),
@Result(column = "createTime",property = "createTime"),
@Result(column = "updateTime",property = "updateTime"),
@Result(column = "creatorId",property = "creatorId"),
@Result(column = "updaterId",property = "updaterId"),
@Result(column = "siteNum",property = "siteNum"),
@Result(column = "deleted",property = "deleted"),
<#list properties as model>
  @Result(column = "${model.columnName}", property ="${model.fieldName}"), // ${model.fieldRemarks}
</#list>
//todo: 最后一行需要去掉","
})
//todo: 方法名需要修改
IPage<${className}> getSomething(Page<${className}> page, @Param(Constants.WRAPPER) Wrapper<${className}> wrapper);
}
