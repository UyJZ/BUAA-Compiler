# BUAA-Compiler
My SysyCompiler
本分支是基于2022年编译器中考试进行模拟练习，其内容
1. 词法中增加了16进制数。
2. 语法中增加了形如`repeat {stmt} until '(' Cond ')' ';'`的文法

2023年期中考试则是
1. 词法中新增了`double`型浮点数
2. 文法中新增了`do {stmt} while '(' Cond ')' ';'`

尊的很简单，笔者框架很烂，冗余代码多，不适合用以上机考试，但是仍然可以在30分钟左右搞定期中考试。
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

