# BUAA-Compiler
My SysyCompiler

中间代码为 llvm（支持`phi`指令），中间代码为四元式的可以参考https://github.com/MarSeventh/SysY-Compiler 🥰🥰🥰

目标代码为MIPS

## 仓库的结构

分支的名字就能对应上大概某次作业，值得注意的是，我重构了一些代码，所以分支里面的代码很有可能是**屎山**，应当以`master`分支为基本准则

`AutoTest`是借鉴某位学长的测试工具，稍稍改了一丢丢

两个`finalExam`是因为我用了`git push origin xxx`但是忘了区分大小写了😓，`FinalExam`的应该是期末考试回忆。。。。。

`MIPS-PLUS`是做过优化的版本

## 竞速结果

28/500(大约)

## 优化

优化完成了`Mem2Reg`，死代码删除，函数内联，`GVN`，寄存器分配，基本块整合，窥孔

优化是很容易出bug的，尤其是寄存器分配，所以在考试的时候应当做相应的权衡

**注意：寄存器分配是存在bug的，具体来说就是在消除phi的时候可能会将分配到同一个寄存器的两个变量放到一个基本块中，触发概率很小。事实上，在考试中也没有触发，但是应该注意这一点，消除phi的时候可以考虑加一些操作避免这种情况**

## 好朋友の编译器（写的比我好多了）

https://github.com/MarSeventh/SysY-Compiler

https://github.com/YangYzzzz/2023Autumn-BUAA-CE

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

