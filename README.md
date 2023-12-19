# BUAA-Compiler
My SysyCompiler

## 竞速结果

总排名大概是28名，具体多少人参加不清楚。。。。

## 优化

优化完成了`Mem2Reg`，死代码删除，函数内联，`GVN`，寄存器分配，基本块整合，窥孔

## git 规范
```bash
# 拉取最新dev分支
git checkout dev
git pull origin

# 签出开发(或修复)分支
git checkout -b feature/xxx (or fix/xxx)

# 提交修改
git add .
git commit -m "[feat](xxx): message" (or "[fix](xxx): message")

# 解决与dev分支的冲突
git checkout dev
git pull origin
git checkout feature/xxx (or fix/xxx)
git rebase dev
git add .
git rebase --continue

# 提交开发/修复分支
git push origin
(pull request on github.com)

# 删除开发/修复分支
git checkout dev
git pull origin
git branch -d feature/xxx (or fix/xxx)
```

