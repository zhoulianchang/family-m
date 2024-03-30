package com.zlc.family.common.enums;

import com.zlc.family.common.exception.family.FamilyException;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户状态
 *
 * @author family
 */
public enum BillType {
    /**
     * 餐饮
     */
    REPAST(1, 1, "餐饮"),
    /**
     * 固定支出
     */
    FIXED_CHARGE(2, 1, "固定支出"),
    /**
     * 生活缴费
     */
    LIVING_PAYMENT(3, 1, "生活缴费"),
    /**
     * 娱乐购物
     */
    SHOPPING(4, 1, "娱乐购物"),
    /**
     * 出行费用
     */
    TRAVEL_EXPENSE(5, 1, "出行费用"),
    /**
     * 宝宝支出
     */
    BABY(6, 1, "宝宝支出"),
    /**
     * 其他支出
     */
    OTHER_OUT(7, 1, "其他支出"),
    /**
     * 其他收入
     */
    OTHER_INCOME(8, 2, "其他收入"),
    /**
     * 工资
     */
    SALARY(9, 2, "工资"),
    ;
    private final Integer code;
    /**
     * 类型 1支出 2收入
     */
    private final Integer type;
    private final String name;

    public Integer getCode() {
        return code;
    }

    BillType(Integer code, Integer type, String name) {
        this.code = code;
        this.type = type;
        this.name = name;
    }

    /**
     * 支出类型的code码集合
     */
    public final static List<Integer> OUT_CODES;
    /**
     * 收入类型的code码集合
     */
    public final static List<Integer> IN_CODES;
    public final static BillType[] VALUES = BillType.values();

    static {
        OUT_CODES = new ArrayList<>();
        IN_CODES = new ArrayList<>();
        for (BillType billType : BillType.values()) {
            if (billType.type.equals(1)) {
                OUT_CODES.add(billType.code);
            } else if (billType.type.equals(2)) {
                IN_CODES.add(billType.code);
            }
        }
    }

    public static String getNameByCode(Integer code) {
        for (BillType billType : VALUES) {
            if (billType.code == code) {
                return billType.name;
            }
        }
        return null;
    }
}
