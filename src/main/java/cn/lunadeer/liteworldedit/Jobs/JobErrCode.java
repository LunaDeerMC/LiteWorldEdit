package cn.lunadeer.liteworldedit.Jobs;

public enum JobErrCode {
    OK(0, ""), // 无错误

    // ------------ 以下结果应当直接取消任务 ------------
    NO_PICKAXE(101, "没有下界合金镐"),
    NOT_ENOUGH_DURATION(102, "所有下界合金镐耐久均不足10"),
    NOT_ENOUGH_ITEMS(103, "物品不足"),
    OUT_OF_RANGE(104, "超出操作距离"),
    NO_SPONGE(105, "没有海绵，无法排水"),
    NOT_ENOUGH_MONEY(106, "没有足够的货币"),
    NO_ECONOMY_PROVIDER(107, "无法获取货币信息"),

    // ------------ 以下结果应当跳过任务 ------------
    NO_PERMISSION(201, "跳过没有权限操作的方块"),
    NOT_AIR_BLOCK(202, "跳过非空气方块"),
    NO_BREAKABLE(203, "跳过不可破坏方块"),
    NOT_LIQUID(204, "跳过非液体方块"),
    ;

    private final int value;
    private final String message;

    JobErrCode(int i, String message) {
        this.value = i;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean canContinue() {
        return value >= 200;
    }
}
