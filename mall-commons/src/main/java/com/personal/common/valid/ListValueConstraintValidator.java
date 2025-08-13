package com.personal.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

/**
 * @ClassName ListValueConstraintValidator
 * @Author liupanpan
 * @Date 2025/8/13
 * @Description 对应的校验注解的校验器
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private HashSet<Integer> set = new HashSet<>();
    // 初始化
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.val();
        for (int v : vals) {
            set.add(v);
        }
    }

    /**
     * 判断校验是否成功的方法
     * @param integer 客户端传递的对应的属性的值 判断value是否在0 ， 1 中
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
