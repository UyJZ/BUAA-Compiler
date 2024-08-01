# BUAA-Compiler
My SysyCompiler

## 仓库的结构

具体看名字就能对应上大概某次作业，值得注意的是，我重构了一些代码，所以分支里面的代码很有可能是**屎山**

`AutoTest`是借鉴某位学长的测试工具，稍稍改了一丢丢

两个`finalExam`是因为我用了`git push origin xxx`但是忘了区分大小写了😓，大写的应该是期末考试回忆。。。。。

`MIPS-PLUS`是做过优化的版本。。。

不过真有人会看我的仓库吗。。。表示怀疑。。。。

## 竞速结果

总排名大概是28名，具体多少人参加不清楚。。。。

## 优化

优化完成了`Mem2Reg`，死代码删除，函数内联，`GVN`，寄存器分配，基本块整合，窥孔

**注意：寄存器分配是存在bug的，具体来说就是在消除phi的时候可能会将分配到同一个寄存器的两个变量放到一个基本块中，触发概率很小。事实上，在考试中也没有触发，但是应该注意这一点，消除phi的时候可以考虑加一些操作避免这种情况**

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

