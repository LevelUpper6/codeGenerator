package ${pack}.model;

import com.hvisions.common.interfaces.IObjectType;
import com.hvisions.demo.enums.DemoObjectTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "${className}DTO")
public class ${className}DTO extends SysBaseDTO implements IObjectType {

<#list properties as model>

  // ${model.fieldRemarks!}
  @ApiModelProperty("${model.fieldRemarks!}")
  private ${model.fieldType} ${model.fieldName};
</#list>

@Override
public Integer getObjectType() {
return DemoObjectTypeEnum..getCode();  //todo: 需要在 DemoObjectTypeEnum 中添加枚举
}

}

