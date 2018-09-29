package hit.to.go.database.dao;

import hit.to.go.entity.validate.ValidateCode;

import java.util.Map;

/**
 * Created by 班耀强 on 2018/9/29
 */
public interface ValidateCodeMapper {
    Integer updateValidateCode(Map<String, Object> map);

    Integer insertValidateCode(Map<String, Object> map);

    ValidateCode queryValidateCode(String email);

    Integer becomeInvalid(String email);
}
