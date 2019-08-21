package ${pack}.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = 0 or deleted IS NULL")
@SQLDelete(sql = "update ${tableName} set deleted = 1,update_time = now() where id = ?")
@Table(name = "${tableName}")
public class ${className} extends SysBase {

<#list properties as model>

  // ${model.fieldRemarks!}
  private ${model.fieldType} ${model.fieldName};
</#list>

}

