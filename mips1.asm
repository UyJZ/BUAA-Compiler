.data
global_MOD:	.word 	13331
global_TEST0:		.word 	0, 1, 2, 3, 4, 
global_TEST1:		.word 	1, 2
	.word 	3, 4

global_num:	
	.space 400
str_0:	.asciiz   "20373569 the mafia~\ntestConst: "
str_1:	.asciiz   " "
str_2:	.asciiz   " "
str_3:	.asciiz   "\n"
str_4:	.asciiz   " "
str_5:	.asciiz   "\ntestExp: "
str_6:	.asciiz   "\n"
.text
	jal	entry
	j	end
entry:
func_main:
main_0:
	# call void @putch(i32 50)
	sw	$a0, -4($sp)
	la	$a0, str_0
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# call void @putint(i32 4)
	sw	$a0, -4($sp)
	li	$a0, 4
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 32)
	sw	$a0, -4($sp)
	la	$a0, str_1
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# call void @putint(i32 4)
	sw	$a0, -4($sp)
	li	$a0, 4
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 32)
	sw	$a0, -4($sp)
	la	$a0, str_2
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# call void @putint(i32 5)
	sw	$a0, -4($sp)
	li	$a0, 5
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_3
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %1 = call i32 @getint()
	li	$v0, 5
	syscall
	move	$t0, $v0
	# move  %2, 1
	li	$t1, 1
	# br label %3
	j	main_1

main_1:
	# %4 = icmp sle i32 %2, %1
	sle	$t2, $t1, $t0
	# br i1 %4, label %5, label %9
	bne	$zero, $t2, main_2
	beq	$zero, $t2, main_3

main_2:
	# %6 = call i32 @getint()
	li	$v0, 5
	syscall
	move	$t2, $v0
	# %7 = getelementptr [100 x i32], [100 x i32]* @num, i32 0, i32 %2
	li	$k1, 4
	mult	$t1, $k1
	mflo	$k1
	la	$k0, global_num
	addu	$t3, $k0, $k1
	# store i32 %6, i32* %7
	sw	$t2, 0($t3)
	# %8 = add i32 %2, 1
	addi	$t4, $t1, 1
	# move  %2, %8
	move	$t1, $t4
	# br label %3
	j	main_1

main_3:
	# %10 = getelementptr [100 x i32], [100 x i32]* @num, i32 0, i32 0
	la	$k0, global_num
	addu	$t2, $k0, $zero
	# %11 = alloca i32*
	addi	$t1, $sp, -4
	# store i32* %10, i32** %11
	sw	$t2, 0($t1)
	# move  %12, 1
	li	$t2, 1
	# br label %13
	j	main_4

main_4:
	# %14 = icmp sle i32 %12, %1
	sle	$t3, $t2, $t0
	# br i1 %14, label %15, label %209
	bne	$zero, $t3, main_5
	beq	$zero, $t3, main_49

main_5:
	# %16 = load i32* , i32** %11
	lw	$t3, 0($t1)
	# %17 = getelementptr i32, i32* %16, i32 %12
	li	$k0, 4
	mult	$t2, $k0
	mflo	$k1
	addu	$t4, $t3, $k1
	# %18 = load i32 , i32* %17
	lw	$t5, 0($t4)
	# %19 = alloca [2 x i32]
	addi	$t3, $sp, -12
	# %20 = getelementptr [2 x i32], [2 x i32]* %19, i32 0, i32 0
	addu	$t4, $t3, $zero
	# store i32 1, i32* %20
	li	$k0, 1
	sw	$k0, 0($t4)
	# %21 = getelementptr [2 x i32], [2 x i32]* %19, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$t6, $t3, $k1
	# store i32 1, i32* %21
	li	$k0, 1
	sw	$k0, 0($t6)
	# %22 = alloca [2 x [2 x i32]]
	addi	$t7, $sp, -28
	# %23 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %22, i32 0, i32 0
	addu	$s0, $t7, $zero
	# %24 = getelementptr [2 x i32], [2 x i32]* %23, i32 0, i32 0
	addu	$s1, $s0, $zero
	# store i32 1, i32* %24
	li	$k0, 1
	sw	$k0, 0($s1)
	# %25 = getelementptr [2 x i32], [2 x i32]* %23, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s2, $s0, $k1
	# store i32 0, i32* %25
	sw	$zero, 0($s2)
	# %26 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %22, i32 0, i32 1
	li	$k0, 1
	li	$k1, 8
	mult	$k0, $k1
	mflo	$k1
	addu	$s3, $t7, $k1
	# %27 = getelementptr [2 x i32], [2 x i32]* %26, i32 0, i32 0
	addu	$s4, $s3, $zero
	# store i32 0, i32* %27
	sw	$zero, 0($s4)
	# %28 = getelementptr [2 x i32], [2 x i32]* %26, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s5, $s3, $k1
	# store i32 1, i32* %28
	li	$k0, 1
	sw	$k0, 0($s5)
	# %29 = alloca [2 x [2 x i32]]
	addi	$s6, $sp, -44
	# %30 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 0
	addu	$s7, $s6, $zero
	# %31 = getelementptr [2 x i32], [2 x i32]* %30, i32 0, i32 0
	addu	$t8, $s7, $zero
	# store i32 1, i32* %31
	li	$k0, 1
	sw	$k0, 0($t8)
	# %32 = getelementptr [2 x i32], [2 x i32]* %30, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$t9, $s7, $k1
	# store i32 1, i32* %32
	li	$k0, 1
	sw	$k0, 0($t9)
	# %33 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %29, i32 0, i32 1
	li	$k0, 1
	li	$k1, 8
	mult	$k0, $k1
	mflo	$k1
	addu	$t4, $s6, $k1
	# %34 = getelementptr [2 x i32], [2 x i32]* %33, i32 0, i32 0
	addu	$t6, $t4, $zero
	# store i32 1, i32* %34
	li	$k0, 1
	sw	$k0, 0($t6)
	# %35 = getelementptr [2 x i32], [2 x i32]* %33, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s1, $t4, $k1
	# store i32 0, i32* %35
	sw	$zero, 0($s1)
	# %36 = sub i32 %18, 1
	subiu	$s2, $t5, 1
	# move  %37, %36
	move	$t4, $s2
	# br label %38
	j	main_6

main_6:
	# %39 = icmp ne i32 %37, 0
	sne	$t5, $t4, $zero
	# br i1 %39, label %40, label %177
	bne	$zero, $t5, main_7
	beq	$zero, $t5, main_41

main_7:
	# %41 = srem i32 %37, 2
	li	$k1, 2
	div	$t4, $k1
	mfhi	$t5
	# %42 = icmp ne i32 %41, 0
	sne	$t6, $t5, $zero
	# br i1 %42, label %43, label %109
	bne	$zero, $t6, main_8
	beq	$zero, $t6, main_24

main_8:
	# %44 = alloca [2 x i32]*
	addi	$t5, $sp, -48
	# store [2 x i32]* %23, [2 x i32]** %44
	sw	$s0, 0($t5)
	# %45 = alloca [2 x i32]*
	addi	$t6, $sp, -52
	# store [2 x i32]* %30, [2 x i32]** %45
	sw	$s7, 0($t6)
	# %46 = alloca [2 x i32]*
	addi	$s1, $sp, -56
	# store [2 x i32]* %23, [2 x i32]** %46
	sw	$s0, 0($s1)
	# %47 = alloca [2 x [2 x i32]]
	addi	$s3, $sp, -72
	# %48 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %47, i32 0, i32 0
	addu	$s4, $s3, $zero
	# %49 = getelementptr [2 x i32], [2 x i32]* %48, i32 0, i32 0
	addu	$s5, $s4, $zero
	# store i32 0, i32* %49
	sw	$zero, 0($s5)
	# %50 = getelementptr [2 x i32], [2 x i32]* %48, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s6, $s4, $k1
	# store i32 0, i32* %50
	sw	$zero, 0($s6)
	# %51 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %47, i32 0, i32 1
	li	$k0, 1
	li	$k1, 8
	mult	$k0, $k1
	mflo	$k1
	addu	$t8, $s3, $k1
	# %52 = getelementptr [2 x i32], [2 x i32]* %51, i32 0, i32 0
	addu	$t9, $t8, $zero
	# store i32 0, i32* %52
	sw	$zero, 0($t9)
	# %53 = getelementptr [2 x i32], [2 x i32]* %51, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s5, $t8, $k1
	# store i32 0, i32* %53
	sw	$zero, 0($s5)
	# move  %54, 0
	move	$s4, $zero
	# move  %55, 0
	move	$s6, $zero
	# move  %56, 0
	move	$s5, $zero
	# br label %57
	j	main_9

main_9:
	# %58 = icmp slt i32 %55, 2
	li	$k1, 2
	slt	$t8, $s6, $k1
	# br i1 %58, label %59, label %90
	bne	$zero, $t8, main_10
	beq	$zero, $t8, main_17

main_10:
	# move  %60, %54
	move	$t8, $s4
	# move  %61, 0
	sw	$zero, -76($sp)
	# move  %62, %56
	move	$t9, $s5
	# br label %63
	j	main_11

main_11:
	# %64 = icmp slt i32 %61, 2
	lw	$k0, -76($sp)
	li	$k1, 2
	slt	$k0, $k0, $k1
	sw	$k0, -80($sp)
	# br i1 %64, label %65, label %88
	lw	$k0, -80($sp)
	bne	$zero, $k0, main_12
	beq	$zero, $k0, main_16

main_12:
	# move  %66, %60
	move	$t9, $t8
	# move  %67, 0
	sw	$zero, -84($sp)
	# br label %68
	j	main_13

main_13:
	# %69 = icmp slt i32 %67, 2
	lw	$k0, -84($sp)
	li	$k1, 2
	slt	$k0, $k0, $k1
	sw	$k0, -88($sp)
	# br i1 %69, label %70, label %86
	lw	$k0, -88($sp)
	bne	$zero, $k0, main_14
	beq	$zero, $k0, main_15

main_14:
	# %71 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %47, i32 0, i32 %55
	li	$k1, 8
	mult	$s6, $k1
	mflo	$k1
	addu	$k0, $s3, $k1
	sw	$k0, -92($sp)
	# %72 = getelementptr [2 x i32], [2 x i32]* %71, i32 0, i32 %61
	lw	$k0, -76($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	lw	$k0, -92($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -96($sp)
	# %73 = load i32 , i32* %72
	lw	$k0, -96($sp)
	lw	$k1, 0($k0)
	sw	$k1, -100($sp)
	# %74 = load [2 x i32]* , [2 x i32]** %44
	lw	$k0, 0($t5)
	sw	$k0, -104($sp)
	# %75 = getelementptr [2 x i32], [2 x i32]* %74, i32 %55
	li	$k0, 8
	mult	$s6, $k0
	mflo	$k1
	lw	$k0, -104($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -108($sp)
	# %76 = getelementptr [2 x i32], [2 x i32]* %75, i32 0, i32 %67
	lw	$k0, -84($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	lw	$k0, -108($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -112($sp)
	# %77 = load i32 , i32* %76
	lw	$k0, -112($sp)
	lw	$k1, 0($k0)
	sw	$k1, -116($sp)
	# %78 = load [2 x i32]* , [2 x i32]** %45
	lw	$k0, 0($t6)
	sw	$k0, -120($sp)
	# %79 = getelementptr [2 x i32], [2 x i32]* %78, i32 %67
	li	$k0, 8
	lw	$k1, -84($sp)
	mult	$k1, $k0
	mflo	$k1
	lw	$k0, -120($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -124($sp)
	# %80 = getelementptr [2 x i32], [2 x i32]* %79, i32 0, i32 %61
	lw	$k0, -76($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	lw	$k0, -124($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -128($sp)
	# %81 = load i32 , i32* %80
	lw	$k0, -128($sp)
	lw	$k1, 0($k0)
	sw	$k1, -132($sp)
	# %82 = mul i32 %77, %81
	lw	$k0, -116($sp)
	lw	$k1, -132($sp)
	mult	$k0, $k1
	mflo	$k0
	sw	$k0, -136($sp)
	# %83 = add i32 %73, %82
	lw	$k0, -100($sp)
	lw	$k1, -136($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -140($sp)
	# %84 = srem i32 %83, 13331
	lw	$k0, -140($sp)
	li	$k1, 13331
	div	$k0, $k1
	mfhi	$k0
	sw	$k0, -144($sp)
	# store i32 %84, i32* %72
	lw	$k0, -144($sp)
	lw	$k1, -96($sp)
	sw	$k0, 0($k1)
	# %85 = add i32 %67, 1
	lw	$k0, -84($sp)
	addi	$k0, $k0, 1
	sw	$k0, -148($sp)
	# move  %66, %83
	lw	$t9, -140($sp)
	# move  %67, %85
	lw	$k0, -148($sp)
	sw	$k0, -152($sp)
	# br label %68
	j	main_13

main_15:
	# %87 = add i32 %61, 1
	lw	$k0, -76($sp)
	addi	$k0, $k0, 1
	sw	$k0, -156($sp)
	# move  %60, %66
	move	$t8, $t9
	# move  %61, %87
	lw	$k0, -156($sp)
	sw	$k0, -160($sp)
	# move  %62, %67
	lw	$t9, -152($sp)
	# br label %63
	j	main_11

main_16:
	# %89 = add i32 %55, 1
	addi	$k0, $s6, 1
	sw	$k0, -164($sp)
	# move  %54, %60
	move	$s4, $t8
	# move  %55, %89
	lw	$s6, -164($sp)
	# move  %56, %62
	move	$s5, $t9
	# br label %57
	j	main_9

main_17:
	# move  %91, 0
	move	$t5, $zero
	# br label %92
	j	main_18

main_18:
	# %93 = icmp slt i32 %91, 2
	li	$k1, 2
	slt	$t6, $t5, $k1
	# br i1 %93, label %94, label %108
	bne	$zero, $t6, main_19
	beq	$zero, $t6, main_23

main_19:
	# move  %95, 0
	move	$t6, $zero
	# br label %96
	j	main_20

main_20:
	# %97 = icmp slt i32 %95, 2
	li	$k1, 2
	slt	$s4, $t6, $k1
	# br i1 %97, label %98, label %106
	bne	$zero, $s4, main_21
	beq	$zero, $s4, main_22

main_21:
	# %99 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %47, i32 0, i32 %91
	li	$k1, 8
	mult	$t5, $k1
	mflo	$k1
	addu	$s4, $s3, $k1
	# %100 = getelementptr [2 x i32], [2 x i32]* %99, i32 0, i32 %95
	li	$k1, 4
	mult	$t6, $k1
	mflo	$k1
	addu	$s5, $s4, $k1
	# %101 = load i32 , i32* %100
	lw	$s6, 0($s5)
	# %102 = load [2 x i32]* , [2 x i32]** %46
	lw	$s4, 0($s1)
	# %103 = getelementptr [2 x i32], [2 x i32]* %102, i32 %91
	li	$k0, 8
	mult	$t5, $k0
	mflo	$k1
	addu	$s5, $s4, $k1
	# %104 = getelementptr [2 x i32], [2 x i32]* %103, i32 0, i32 %95
	li	$k1, 4
	mult	$t6, $k1
	mflo	$k1
	addu	$s4, $s5, $k1
	# store i32 %101, i32* %104
	sw	$s6, 0($s4)
	# %105 = add i32 %95, 1
	addi	$s5, $t6, 1
	# move  %95, %105
	move	$t6, $s5
	# br label %96
	j	main_20

main_22:
	# %107 = add i32 %91, 1
	addi	$s4, $t5, 1
	# move  %91, %107
	move	$t5, $s4
	# br label %92
	j	main_18

main_23:
	# br label %109
	j	main_24

main_24:
	# %110 = alloca [2 x i32]*
	addi	$t5, $sp, -168
	# store [2 x i32]* %30, [2 x i32]** %110
	sw	$s7, 0($t5)
	# %111 = alloca [2 x i32]*
	addi	$t6, $sp, -172
	# store [2 x i32]* %30, [2 x i32]** %111
	sw	$s7, 0($t6)
	# %112 = alloca [2 x i32]*
	addi	$s1, $sp, -176
	# store [2 x i32]* %30, [2 x i32]** %112
	sw	$s7, 0($s1)
	# %113 = alloca [2 x [2 x i32]]
	addi	$s3, $sp, -192
	# %114 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %113, i32 0, i32 0
	addu	$s6, $s3, $zero
	# %115 = getelementptr [2 x i32], [2 x i32]* %114, i32 0, i32 0
	addu	$k0, $s6, $zero
	sw	$k0, -196($sp)
	# store i32 0, i32* %115
	lw	$k1, -196($sp)
	sw	$zero, 0($k1)
	# %116 = getelementptr [2 x i32], [2 x i32]* %114, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s6, $s6, $k1
	# store i32 0, i32* %116
	sw	$zero, 0($s6)
	# %117 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %113, i32 0, i32 1
	li	$k0, 1
	li	$k1, 8
	mult	$k0, $k1
	mflo	$k1
	addu	$s6, $s3, $k1
	# %118 = getelementptr [2 x i32], [2 x i32]* %117, i32 0, i32 0
	addu	$k0, $s6, $zero
	sw	$k0, -200($sp)
	# store i32 0, i32* %118
	lw	$k1, -200($sp)
	sw	$zero, 0($k1)
	# %119 = getelementptr [2 x i32], [2 x i32]* %117, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s6, $s6, $k1
	# store i32 0, i32* %119
	sw	$zero, 0($s6)
	# move  %120, 0
	move	$s6, $zero
	# move  %121, 0
	sw	$zero, -204($sp)
	# move  %122, 0
	sw	$zero, -208($sp)
	# br label %123
	j	main_25

main_25:
	# %124 = icmp slt i32 %121, 2
	lw	$k0, -204($sp)
	li	$k1, 2
	slt	$k0, $k0, $k1
	sw	$k0, -212($sp)
	# br i1 %124, label %125, label %156
	lw	$k0, -212($sp)
	bne	$zero, $k0, main_26
	beq	$zero, $k0, main_33

main_26:
	# move  %126, %120
	sw	$s6, -216($sp)
	# move  %127, 0
	sw	$zero, -220($sp)
	# move  %128, %122
	lw	$k0, -208($sp)
	sw	$k0, -224($sp)
	# br label %129
	j	main_27

main_27:
	# %130 = icmp slt i32 %127, 2
	lw	$k0, -220($sp)
	li	$k1, 2
	slt	$k0, $k0, $k1
	sw	$k0, -228($sp)
	# br i1 %130, label %131, label %154
	lw	$k0, -228($sp)
	bne	$zero, $k0, main_28
	beq	$zero, $k0, main_32

main_28:
	# move  %132, %126
	lw	$k0, -216($sp)
	sw	$k0, -232($sp)
	# move  %133, 0
	sw	$zero, -236($sp)
	# br label %134
	j	main_29

main_29:
	# %135 = icmp slt i32 %133, 2
	lw	$k0, -236($sp)
	li	$k1, 2
	slt	$k0, $k0, $k1
	sw	$k0, -240($sp)
	# br i1 %135, label %136, label %152
	lw	$k0, -240($sp)
	bne	$zero, $k0, main_30
	beq	$zero, $k0, main_31

main_30:
	# %137 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %113, i32 0, i32 %121
	lw	$k0, -204($sp)
	li	$k1, 8
	mult	$k0, $k1
	mflo	$k1
	addu	$k0, $s3, $k1
	sw	$k0, -244($sp)
	# %138 = getelementptr [2 x i32], [2 x i32]* %137, i32 0, i32 %127
	lw	$k0, -220($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	lw	$k0, -244($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -248($sp)
	# %139 = load i32 , i32* %138
	lw	$k0, -248($sp)
	lw	$k1, 0($k0)
	sw	$k1, -252($sp)
	# %140 = load [2 x i32]* , [2 x i32]** %110
	lw	$k0, 0($t5)
	sw	$k0, -256($sp)
	# %141 = getelementptr [2 x i32], [2 x i32]* %140, i32 %121
	li	$k0, 8
	lw	$k1, -204($sp)
	mult	$k1, $k0
	mflo	$k1
	lw	$k0, -256($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -260($sp)
	# %142 = getelementptr [2 x i32], [2 x i32]* %141, i32 0, i32 %133
	lw	$k0, -236($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	lw	$k0, -260($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -264($sp)
	# %143 = load i32 , i32* %142
	lw	$k0, -264($sp)
	lw	$k1, 0($k0)
	sw	$k1, -268($sp)
	# %144 = load [2 x i32]* , [2 x i32]** %111
	lw	$k0, 0($t6)
	sw	$k0, -272($sp)
	# %145 = getelementptr [2 x i32], [2 x i32]* %144, i32 %133
	li	$k0, 8
	lw	$k1, -236($sp)
	mult	$k1, $k0
	mflo	$k1
	lw	$k0, -272($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -276($sp)
	# %146 = getelementptr [2 x i32], [2 x i32]* %145, i32 0, i32 %127
	lw	$k0, -220($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	lw	$k0, -276($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -280($sp)
	# %147 = load i32 , i32* %146
	lw	$k0, -280($sp)
	lw	$k1, 0($k0)
	sw	$k1, -284($sp)
	# %148 = mul i32 %143, %147
	lw	$k0, -268($sp)
	lw	$k1, -284($sp)
	mult	$k0, $k1
	mflo	$k0
	sw	$k0, -288($sp)
	# %149 = add i32 %139, %148
	lw	$k0, -252($sp)
	lw	$k1, -288($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -292($sp)
	# %150 = srem i32 %149, 13331
	lw	$k0, -292($sp)
	li	$k1, 13331
	div	$k0, $k1
	mfhi	$k0
	sw	$k0, -296($sp)
	# store i32 %150, i32* %138
	lw	$k0, -296($sp)
	lw	$k1, -248($sp)
	sw	$k0, 0($k1)
	# %151 = add i32 %133, 1
	lw	$k0, -236($sp)
	addi	$k0, $k0, 1
	sw	$k0, -300($sp)
	# move  %132, %149
	lw	$k0, -292($sp)
	sw	$k0, -304($sp)
	# move  %133, %151
	lw	$k0, -300($sp)
	sw	$k0, -308($sp)
	# br label %134
	j	main_29

main_31:
	# %153 = add i32 %127, 1
	lw	$k0, -220($sp)
	addi	$k0, $k0, 1
	sw	$k0, -312($sp)
	# move  %126, %132
	lw	$k0, -304($sp)
	sw	$k0, -316($sp)
	# move  %127, %153
	lw	$k0, -312($sp)
	sw	$k0, -320($sp)
	# move  %128, %133
	lw	$k0, -308($sp)
	sw	$k0, -324($sp)
	# br label %129
	j	main_27

main_32:
	# %155 = add i32 %121, 1
	lw	$k0, -204($sp)
	addi	$k0, $k0, 1
	sw	$k0, -328($sp)
	# move  %120, %126
	lw	$s6, -316($sp)
	# move  %121, %155
	lw	$k0, -328($sp)
	sw	$k0, -332($sp)
	# move  %122, %128
	lw	$k0, -324($sp)
	sw	$k0, -336($sp)
	# br label %123
	j	main_25

main_33:
	# move  %157, 0
	move	$t5, $zero
	# br label %158
	j	main_34

main_34:
	# %159 = icmp slt i32 %157, 2
	li	$k1, 2
	slt	$t6, $t5, $k1
	# br i1 %159, label %160, label %174
	bne	$zero, $t6, main_35
	beq	$zero, $t6, main_39

main_35:
	# move  %161, 0
	move	$t6, $zero
	# br label %162
	j	main_36

main_36:
	# %163 = icmp slt i32 %161, 2
	li	$k1, 2
	slt	$s6, $t6, $k1
	# br i1 %163, label %164, label %172
	bne	$zero, $s6, main_37
	beq	$zero, $s6, main_38

main_37:
	# %165 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %113, i32 0, i32 %157
	li	$k1, 8
	mult	$t5, $k1
	mflo	$k1
	addu	$s6, $s3, $k1
	# %166 = getelementptr [2 x i32], [2 x i32]* %165, i32 0, i32 %161
	li	$k1, 4
	mult	$t6, $k1
	mflo	$k1
	addu	$s6, $s6, $k1
	# %167 = load i32 , i32* %166
	lw	$s6, 0($s6)
	# %168 = load [2 x i32]* , [2 x i32]** %112
	lw	$k0, 0($s1)
	sw	$k0, -340($sp)
	# %169 = getelementptr [2 x i32], [2 x i32]* %168, i32 %157
	li	$k0, 8
	mult	$t5, $k0
	mflo	$k1
	lw	$k0, -340($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -344($sp)
	# %170 = getelementptr [2 x i32], [2 x i32]* %169, i32 0, i32 %161
	li	$k1, 4
	mult	$t6, $k1
	mflo	$k1
	lw	$k0, -344($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -348($sp)
	# store i32 %167, i32* %170
	lw	$k1, -348($sp)
	sw	$s6, 0($k1)
	# %171 = add i32 %161, 1
	addi	$s6, $t6, 1
	# move  %161, %171
	move	$t6, $s6
	# br label %162
	j	main_36

main_38:
	# %173 = add i32 %157, 1
	addi	$k0, $t5, 1
	sw	$k0, -352($sp)
	# move  %157, %173
	lw	$t5, -352($sp)
	# br label %158
	j	main_34

main_39:
	# %175 = sdiv i32 %37, 2
	li	$k1, 2
	div	$t4, $k1
	mflo	$t5
	# br label %176
	j	main_40

main_40:
	# move  %37, %175
	move	$t4, $t5
	# br label %38
	j	main_6

main_41:
	# %178 = alloca [2 x i32]
	addi	$t6, $sp, -360
	# %179 = getelementptr [2 x i32], [2 x i32]* %178, i32 0, i32 0
	addu	$s0, $t6, $zero
	# store i32 0, i32* %179
	sw	$zero, 0($s0)
	# %180 = getelementptr [2 x i32], [2 x i32]* %178, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	addu	$s1, $t6, $k1
	# store i32 0, i32* %180
	sw	$zero, 0($s1)
	# move  %181, 0
	move	$t4, $zero
	# move  %182, 0
	move	$s1, $zero
	# br label %183
	j	main_42

main_42:
	# %184 = icmp slt i32 %182, 2
	li	$k1, 2
	slt	$s2, $s1, $k1
	# br i1 %184, label %185, label %204
	bne	$zero, $s2, main_43
	beq	$zero, $s2, main_47

main_43:
	# move  %186, %181
	move	$s2, $t4
	# move  %187, 0
	move	$s3, $zero
	# br label %188
	j	main_44

main_44:
	# %189 = icmp slt i32 %187, 2
	li	$k1, 2
	slt	$s7, $s3, $k1
	# br i1 %189, label %190, label %202
	bne	$zero, $s7, main_45
	beq	$zero, $s7, main_46

main_45:
	# %191 = getelementptr [2 x i32], [2 x i32]* %178, i32 0, i32 %182
	li	$k1, 4
	mult	$s1, $k1
	mflo	$k1
	addu	$s7, $t6, $k1
	# %192 = load i32 , i32* %191
	lw	$k0, 0($s7)
	sw	$k0, -364($sp)
	# %193 = getelementptr [2 x i32], [2 x i32]* %19, i32 0, i32 %187
	li	$k1, 4
	mult	$s3, $k1
	mflo	$k1
	addu	$k0, $t3, $k1
	sw	$k0, -368($sp)
	# %194 = load i32 , i32* %193
	lw	$k0, -368($sp)
	lw	$k1, 0($k0)
	sw	$k1, -372($sp)
	# %195 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %22, i32 0, i32 %187
	li	$k1, 8
	mult	$s3, $k1
	mflo	$k1
	addu	$k0, $t7, $k1
	sw	$k0, -376($sp)
	# %196 = getelementptr [2 x i32], [2 x i32]* %195, i32 0, i32 %182
	li	$k1, 4
	mult	$s1, $k1
	mflo	$k1
	lw	$k0, -376($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -380($sp)
	# %197 = load i32 , i32* %196
	lw	$k0, -380($sp)
	lw	$k1, 0($k0)
	sw	$k1, -384($sp)
	# %198 = mul i32 %194, %197
	lw	$k0, -372($sp)
	lw	$k1, -384($sp)
	mult	$k0, $k1
	mflo	$k0
	sw	$k0, -388($sp)
	# %199 = add i32 %192, %198
	lw	$k0, -364($sp)
	lw	$k1, -388($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -392($sp)
	# %200 = srem i32 %199, 13331
	lw	$k0, -392($sp)
	li	$k1, 13331
	div	$k0, $k1
	mfhi	$k0
	sw	$k0, -396($sp)
	# store i32 %200, i32* %191
	lw	$k0, -396($sp)
	sw	$k0, 0($s7)
	# %201 = add i32 %187, 1
	addi	$s7, $s3, 1
	# move  %186, %199
	lw	$s2, -392($sp)
	# move  %187, %201
	move	$s3, $s7
	# br label %188
	j	main_44

main_46:
	# %203 = add i32 %182, 1
	addi	$k0, $s1, 1
	sw	$k0, -400($sp)
	# move  %181, %186
	move	$t4, $s2
	# move  %182, %203
	lw	$s1, -400($sp)
	# br label %183
	j	main_42

main_47:
	# %205 = load i32 , i32* %179
	lw	$t3, 0($s0)
	# move  %206, %205
	move	$t4, $t3
	# call void @putint(i32 %206)
	sw	$a0, -404($sp)
	move	$a0, $t4
	li	$v0, 1
	syscall
	lw	$a0, -404($sp)
	# call void @putch(i32 32)
	sw	$a0, -404($sp)
	la	$a0, str_4
	li	$v0, 4
	syscall
	lw	$a0, -404($sp)
	# %207 = add i32 %12, 1
	addi	$t6, $t2, 1
	# br label %208
	j	main_48

main_48:
	# move  %12, %207
	move	$t2, $t6
	# br label %13
	j	main_4

main_49:
	# %210 = getelementptr [100 x i32], [100 x i32]* @num, i32 0, i32 %1
	li	$k1, 4
	mult	$t0, $k1
	mflo	$k1
	la	$k0, global_num
	addu	$t1, $k0, $k1
	# %211 = load i32 , i32* %210
	lw	$t2, 0($t1)
	# %212 = add i32 %211, 5
	addi	$t3, $t2, 5
	# %213 = load i32 , i32* %210
	lw	$t4, 0($t1)
	# %214 = mul i32 %213, 2
	li	$k1, 2
	mult	$t4, $k1
	mflo	$t5
	# %215 = sdiv i32 %214, 99
	li	$k1, 99
	div	$t5, $k1
	mflo	$t6
	# %216 = getelementptr [100 x i32], [100 x i32]* @num, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_num
	addu	$t7, $k0, $k1
	# %217 = load i32 , i32* %216
	lw	$s0, 0($t7)
	# %218 = load i32 , i32* %210
	lw	$s1, 0($t1)
	# %219 = mul i32 %217, %218
	mult	$s0, $s1
	mflo	$s2
	# %220 = srem i32 %219, 13331
	li	$k1, 13331
	div	$s2, $k1
	mfhi	$s3
	# %221 = add i32 %215, %220
	addu	$s4, $t6, $s3
	# %222 = sub i32 %221, 1
	subiu	$s5, $s4, 1
	# %223 = sub i32 0, %222
	subu	$s6, $zero, $s5
	# %224 = sub i32 %212, %223
	subu	$s7, $t3, $s6
	# call void @putch(i32 10)
	sw	$a0, -404($sp)
	la	$a0, str_5
	li	$v0, 4
	syscall
	lw	$a0, -404($sp)
	# call void @putint(i32 %224)
	sw	$a0, -404($sp)
	move	$a0, $s7
	li	$v0, 1
	syscall
	lw	$a0, -404($sp)
	# call void @putch(i32 10)
	sw	$a0, -404($sp)
	la	$a0, str_6
	li	$v0, 4
	syscall
	lw	$a0, -404($sp)
	# ret i32 0
	move	$v0, $zero
	jr	$ra
end:


