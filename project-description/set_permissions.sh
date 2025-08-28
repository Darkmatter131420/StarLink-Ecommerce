#!/bin/bash

# 定义需要设置权限的目录数组
declare -a DIRS=(
    "./elasticsearch/plugins/"
    "./elasticsearch/data/"
    "./mysql/init/"
    "./mysql/conf/"
    "./nacos/plugins/"
    "./nacos/data/"
    "./nacos/configs/"
    "./redis/data/"
    "./seata/seata/"
    "./sentinel/"
)

# 日志文件路径
LOG_FILE="./permission_changes_$(date +%Y%m%d).log"

# 权限值 (可根据需要修改)
PERMISSIONS=777

# 开始日志记录
echo "===== 权限修改操作开始 $(date) =====" | tee -a "$LOG_FILE"

# 计数器
SUCCESS_COUNT=0
FAIL_COUNT=0

# 遍历所有目录
for DIR in "${DIRS[@]}"; do
    # 检查目录是否存在
    if [ ! -d "$DIR" ]; then
        echo "[错误] 目录不存在: $DIR" | tee -a "$LOG_FILE"
        ((FAIL_COUNT++))
        continue
    fi
    
    echo "正在处理: $DIR" | tee -a "$LOG_FILE"
    
    # 修改权限
    if chmod -R "$PERMISSIONS" "$DIR"; then
        echo "成功设置 $DIR 权限为 $PERMISSIONS" | tee -a "$LOG_FILE"
        ((SUCCESS_COUNT++))
    else
        echo "[错误] 无法设置 $DIR 权限" | tee -a "$LOG_FILE"
        ((FAIL_COUNT++))
    fi
done

# 操作结果汇总
echo "===== 操作结果 =====" | tee -a "$LOG_FILE"
echo "成功: $SUCCESS_COUNT" | tee -a "$LOG_FILE"
echo "失败: $FAIL_COUNT" | tee -a "$LOG_FILE"
echo "===== 操作完成 $(date) =====" | tee -a "$LOG_FILE"

# 根据结果返回适当的退出状态
if [ "$FAIL_COUNT" -gt 0 ]; then
    exit 1
else
    exit 0
fi