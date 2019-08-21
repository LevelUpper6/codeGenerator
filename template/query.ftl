package ${pack}.model;

import com.hvisions.common.interfaces.IObjectType;
import com.hvisions.demo.enums.DemoObjectTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "${className}QueryDTO")
public class ${className}QueryDTO extends PageInfo {

<#list properties as model>

  // ${model.fieldRemarks!}
  @ApiModelProperty("${model.fieldRemarks!}")
  private ${model.fieldType} ${model.fieldName};
</#list>

}

