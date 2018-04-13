package net.coding.mart.common.constant;

/**
 * Created by chenchao on 16/11/23.
 */

public enum  OrderType {
   
    Deposit("充值"),
    WithDraw("提现"),
    RewardPrepayment("项目预付款"),
    RewardStagePayment("项目阶段付款"),
    DeveloperPayment("项目阶段收款"),
    ServiceFee("服务费"),
    Refund("退款"),
    EventPayment("活动出账"),
    EventDeposit("活动入账"),
    SystemDeduct("系统扣款"),
    SystemRemit("系统打款"),
    ApplyContactDeduct("查看开发者联系信息"),
    ApplyContactRemit("查看开发者联系信息");
    
    public final String alics;
    
    OrderType(String alics) {
        this.alics = alics;
    }

    public static String getActionSymbol(String orderType) {
        OrderType[] types = new OrderType[] {
                Deposit, DeveloperPayment, ServiceFee, Refund, EventDeposit, SystemRemit
        };
        
        for (OrderType item : types) {
            if (item.name().equals(orderType)) {
                return "+";
            }
        }
        
        return "-";
    }

    public static String getAlics(String type) {
        for (OrderType item : OrderType.values()) {
            if (item.name().equals(type)) {
                return item.alics;
            }
        }

        return "";
    }

    public static OrderType name2enum(String name) {
        for (OrderType item : OrderType.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }

        return Deposit;
    }
}
