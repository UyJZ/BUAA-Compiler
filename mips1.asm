.data
global_Mod:	.word 	10007
global_Map:		.word 	1, 1
	.word 	1, 0

global_Bound:		.word 	2, 2, 
global_T:	
	.space 4
global_f:		.word 	1, 0, 
global_ori:		.word 	1, 0
	.space 8
str_0:	.asciiz   "f"
str_1:	.asciiz   ": "
str_2:	.asciiz   "\n"
str_3:	.asciiz   "ori00:"
str_4:	.asciiz   "\n"
.text
	jal	entry
	j	end
entry:
func_main:
main_0:
	# %1 = call i32 @getint()
	li	$v0, 5
	syscall
	move	$t0, $v0
	# store i32 %1, i32* @T
	la	$k1, global_T
	sw	$t0, 0($k1)
	# %2 = alloca [2 x [2 x i32]]
	addi	$t1, $sp, -16
	# %3 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %2, i32 0, i32 0
	move	$t2, $t1
	# %4 = alloca [2 x i32]*
	addi	$t0, $sp, -20
	# store [2 x i32]* %3, [2 x i32]** %4
	sw	$t2, 0($t0)
	# %5 = load [2 x i32]* , [2 x i32]** %4
	lw	$t1, 0($t0)
	# %6 = getelementptr [2 x i32], [2 x i32]* %5, i32 0
	move	$t3, $t1
	# %7 = getelementptr [2 x i32], [2 x i32]* %6, i32 0, i32 0
	move	$t4, $t3
	# store i32 1, i32* %7
	li	$k0, 1
	sw	$k0, 0($t4)
	# %8 = load [2 x i32]* , [2 x i32]** %4
	lw	$t5, 0($t0)
	# %9 = getelementptr [2 x i32], [2 x i32]* %8, i32 0
	move	$t6, $t5
	# %10 = getelementptr [2 x i32], [2 x i32]* %9, i32 0, i32 1
	addiu	$t7, $t6, 4
	# store i32 1, i32* %10
	li	$k0, 1
	sw	$k0, 0($t7)
	# %11 = load [2 x i32]* , [2 x i32]** %4
	lw	$s0, 0($t0)
	# %12 = getelementptr [2 x i32], [2 x i32]* %11, i32 1
	addiu	$s1, $s0, 8
	# %13 = getelementptr [2 x i32], [2 x i32]* %12, i32 0, i32 0
	move	$s2, $s1
	# store i32 1, i32* %13
	li	$k0, 1
	sw	$k0, 0($s2)
	# %14 = load [2 x i32]* , [2 x i32]** %4
	lw	$s3, 0($t0)
	# %15 = getelementptr [2 x i32], [2 x i32]* %14, i32 1
	addiu	$s4, $s3, 8
	# %16 = getelementptr [2 x i32], [2 x i32]* %15, i32 0, i32 1
	addiu	$s5, $s4, 4
	# store i32 0, i32* %16
	sw	$zero, 0($s5)
	# br label %17

main_1:
	# br label %18

main_2:
	# %19 = load i32 , i32* @T
	la	$k0, global_T
	lw	$t0, 0($k0)
	# %20 = icmp ne i32 %19, 0
	sne	$t1, $t0, $zero
	# br i1 %20, label %21, label %152
	beq	$zero, $t1, main_38

main_3:
	# %22 = load i32 , i32* @T
	la	$k0, global_T
	lw	$t0, 0($k0)
	# %23 = sdiv i32 %22, 2
	li	$k1, 2
	div	$t0, $k1
	mflo	$t1
	# %24 = mul i32 %23, 2
	li	$k1, 2
	mult	$t1, $k1
	mflo	$t3
	# %25 = load i32 , i32* @T
	la	$k0, global_T
	lw	$t4, 0($k0)
	# %26 = icmp ne i32 %24, %25
	sne	$t5, $t3, $t4
	# br i1 %26, label %27, label %88
	beq	$zero, $t5, main_20

main_4:
	# %28 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* @ori, i32 0, i32 0
	la	$k0, global_ori
	move	$t0, $k0
	# %29 = alloca [2 x i32]*
	addi	$t1, $sp, -24
	# store [2 x i32]* %28, [2 x i32]** %29
	sw	$t0, 0($t1)
	# %30 = alloca [2 x i32]*
	addi	$t3, $sp, -28
	# store [2 x i32]* %3, [2 x i32]** %30
	sw	$t2, 0($t3)
	# %31 = alloca [2 x [2 x i32]]
	addi	$t4, $sp, -44
	# move  %32, 0
	move	$t0, $zero
	# move  %33, 0
	move	$t6, $zero
	# move  %34, 0
	move	$t5, $zero
	# br label %35

main_5:
	# %36 = icmp slt i32 %33, 2
	li	$k1, 2
	slt	$t7, $t6, $k1
	# br i1 %36, label %37, label %68
	beq	$zero, $t7, main_13

main_6:
	# move  %38, %32
	move	$t7, $t0
	# move  %39, 0
	move	$s1, $zero
	# move  %40, %34
	move	$s0, $t5
	# br label %41

main_7:
	# %42 = icmp slt i32 %39, 2
	li	$k1, 2
	slt	$s2, $s1, $k1
	# br i1 %42, label %43, label %66
	beq	$zero, $s2, main_12

main_8:
	# move  %44, 0
	move	$t7, $zero
	# move  %45, 0
	move	$s0, $zero
	# br label %46

main_9:
	# %47 = icmp slt i32 %45, 2
	li	$k1, 2
	slt	$s2, $s0, $k1
	# br i1 %47, label %48, label %62
	beq	$zero, $s2, main_11

main_10:
	# %49 = load [2 x i32]* , [2 x i32]** %29
	lw	$s2, 0($t1)
	# %50 = getelementptr [2 x i32], [2 x i32]* %49, i32 %33
	li	$k0, 8
	mult	$t6, $k0
	mflo	$k1
	addu	$s3, $s2, $k1
	# %51 = getelementptr [2 x i32], [2 x i32]* %50, i32 0, i32 %45
	li	$k1, 4
	mult	$s0, $k1
	mflo	$k1
	addu	$s4, $s3, $k1
	# %52 = load i32 , i32* %51
	lw	$s5, 0($s4)
	# %53 = load [2 x i32]* , [2 x i32]** %30
	lw	$s6, 0($t3)
	# %54 = getelementptr [2 x i32], [2 x i32]* %53, i32 %45
	li	$k0, 8
	mult	$s0, $k0
	mflo	$k1
	addu	$s7, $s6, $k1
	# %55 = getelementptr [2 x i32], [2 x i32]* %54, i32 0, i32 %39
	li	$k1, 4
	mult	$s1, $k1
	mflo	$k1
	addu	$t8, $s7, $k1
	# %56 = load i32 , i32* %55
	lw	$t9, 0($t8)
	# %57 = mul i32 %52, %56
	mult	$s5, $t9
	mflo	$s2
	# %58 = srem i32 %57, 10007
	li	$k1, 10007
	div	$s2, $k1
	mfhi	$s3
	# %59 = add i32 %44, %58
	addu	$s4, $t7, $s3
	# %60 = srem i32 %59, 10007
	li	$k1, 10007
	div	$s4, $k1
	mfhi	$s6
	# %61 = add i32 %45, 1
	addiu	$s7, $s0, 1
	# move  %44, %60
	move	$t7, $s6
	# move  %45, %61
	move	$s0, $s7
	# br label %46
	j	main_9

main_11:
	# %63 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %31, i32 0, i32 %33
	li	$k1, 8
	mult	$t6, $k1
	mflo	$k1
	addu	$s2, $t4, $k1
	# %64 = getelementptr [2 x i32], [2 x i32]* %63, i32 0, i32 %39
	li	$k1, 4
	mult	$s1, $k1
	mflo	$k1
	addu	$s3, $s2, $k1
	# store i32 %44, i32* %64
	sw	$t7, 0($s3)
	# %65 = add i32 %39, 1
	addiu	$s4, $s1, 1
	# move  %38, %44
	# move  %39, %65
	move	$s1, $s4
	# move  %40, %45
	# br label %41
	j	main_7

main_12:
	# %67 = add i32 %33, 1
	addiu	$s2, $t6, 1
	# move  %32, %38
	move	$t0, $t7
	# move  %33, %67
	move	$t6, $s2
	# move  %34, %40
	move	$t5, $s0
	# br label %35
	j	main_5

main_13:
	# move  %69, 0
	move	$t0, $zero
	# move  %70, 0
	move	$t3, $zero
	# br label %71

main_14:
	# %72 = icmp slt i32 %70, 2
	li	$k1, 2
	slt	$t5, $t3, $k1
	# br i1 %72, label %73, label %87
	beq	$zero, $t5, main_19

main_15:
	# move  %74, %69
	move	$t5, $t0
	# br label %75

main_16:
	# %76 = icmp slt i32 %74, 2
	li	$k1, 2
	slt	$t6, $t5, $k1
	# br i1 %76, label %77, label %85
	beq	$zero, $t6, main_18

main_17:
	# %78 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %31, i32 0, i32 %70
	li	$k1, 8
	mult	$t3, $k1
	mflo	$k1
	addu	$t6, $t4, $k1
	# %79 = getelementptr [2 x i32], [2 x i32]* %78, i32 0, i32 %74
	li	$k1, 4
	mult	$t5, $k1
	mflo	$k1
	addu	$s1, $t6, $k1
	# %80 = load i32 , i32* %79
	lw	$s3, 0($s1)
	# %81 = load [2 x i32]* , [2 x i32]** %29
	lw	$s5, 0($t1)
	# %82 = getelementptr [2 x i32], [2 x i32]* %81, i32 %70
	li	$k0, 8
	mult	$t3, $k0
	mflo	$k1
	addu	$t8, $s5, $k1
	# %83 = getelementptr [2 x i32], [2 x i32]* %82, i32 0, i32 %74
	li	$k1, 4
	mult	$t5, $k1
	mflo	$k1
	addu	$t9, $t8, $k1
	# store i32 %80, i32* %83
	sw	$s3, 0($t9)
	# %84 = add i32 %74, 1
	addiu	$t6, $t5, 1
	# move  %74, %84
	move	$t5, $t6
	# br label %75
	j	main_16

main_18:
	# %86 = add i32 %70, 1
	addiu	$s1, $t3, 1
	# move  %69, %74
	move	$t0, $t5
	# move  %70, %86
	move	$t3, $s1
	# br label %71
	j	main_14

main_19:
	# br label %89
	j	main_21

main_20:
	# br label %89

main_21:
	# %90 = load i32 , i32* @T
	la	$k0, global_T
	lw	$t0, 0($k0)
	# %91 = sdiv i32 %90, 2
	li	$k1, 2
	div	$t0, $k1
	mflo	$t1
	# store i32 %91, i32* @T
	la	$k1, global_T
	sw	$t1, 0($k1)
	# %92 = alloca [2 x i32]*
	addi	$t0, $sp, -48
	# store [2 x i32]* %3, [2 x i32]** %92
	sw	$t2, 0($t0)
	# %93 = alloca [2 x i32]*
	addi	$t1, $sp, -52
	# store [2 x i32]* %3, [2 x i32]** %93
	sw	$t2, 0($t1)
	# %94 = alloca [2 x [2 x i32]]
	addi	$t3, $sp, -68
	# move  %95, 0
	move	$t4, $zero
	# move  %96, 0
	move	$s5, $zero
	# move  %97, 0
	move	$s3, $zero
	# br label %98

main_22:
	# %99 = icmp slt i32 %96, 2
	li	$k1, 2
	slt	$t8, $s5, $k1
	# br i1 %99, label %100, label %131
	beq	$zero, $t8, main_30

main_23:
	# move  %101, %95
	move	$t8, $t4
	# move  %102, 0
	sw	$zero, -72($sp)
	# move  %103, %97
	move	$t9, $s3
	# br label %104

main_24:
	# %105 = icmp slt i32 %102, 2
	lw	$k0, -72($sp)
	li	$k1, 2
	slt	$k0, $k0, $k1
	sw	$k0, -76($sp)
	# br i1 %105, label %106, label %129
	lw	$k0, -76($sp)
	beq	$zero, $k0, main_29

main_25:
	# move  %107, 0
	move	$t8, $zero
	# move  %108, 0
	move	$t9, $zero
	# br label %109

main_26:
	# %110 = icmp slt i32 %108, 2
	li	$k1, 2
	slt	$k0, $t9, $k1
	sw	$k0, -80($sp)
	# br i1 %110, label %111, label %125
	lw	$k0, -80($sp)
	beq	$zero, $k0, main_28

main_27:
	# %112 = load [2 x i32]* , [2 x i32]** %92
	lw	$k0, 0($t0)
	sw	$k0, -84($sp)
	# %113 = getelementptr [2 x i32], [2 x i32]* %112, i32 %96
	li	$k0, 8
	mult	$s5, $k0
	mflo	$k1
	lw	$k0, -84($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -88($sp)
	# %114 = getelementptr [2 x i32], [2 x i32]* %113, i32 0, i32 %108
	li	$k1, 4
	mult	$t9, $k1
	mflo	$k1
	lw	$k0, -88($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -92($sp)
	# %115 = load i32 , i32* %114
	lw	$k0, -92($sp)
	lw	$k1, 0($k0)
	sw	$k1, -96($sp)
	# %116 = load [2 x i32]* , [2 x i32]** %93
	lw	$k0, 0($t1)
	sw	$k0, -100($sp)
	# %117 = getelementptr [2 x i32], [2 x i32]* %116, i32 %108
	li	$k0, 8
	mult	$t9, $k0
	mflo	$k1
	lw	$k0, -100($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -104($sp)
	# %118 = getelementptr [2 x i32], [2 x i32]* %117, i32 0, i32 %102
	lw	$k1, -72($sp)
	li	$k1, 4
	mult	$k1, $k1
	mflo	$k1
	lw	$k0, -104($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -108($sp)
	# %119 = load i32 , i32* %118
	lw	$k0, -108($sp)
	lw	$k1, 0($k0)
	sw	$k1, -112($sp)
	# %120 = mul i32 %115, %119
	lw	$k0, -96($sp)
	lw	$k1, -112($sp)
	mult	$k0, $k1
	mflo	$k0
	sw	$k0, -116($sp)
	# %121 = srem i32 %120, 10007
	lw	$k0, -116($sp)
	li	$k1, 10007
	div	$k0, $k1
	mfhi	$k0
	sw	$k0, -120($sp)
	# %122 = add i32 %107, %121
	lw	$k1, -120($sp)
	addu	$k0, $t8, $k1
	sw	$k0, -124($sp)
	# %123 = srem i32 %122, 10007
	lw	$k0, -124($sp)
	li	$k1, 10007
	div	$k0, $k1
	mfhi	$k0
	sw	$k0, -128($sp)
	# %124 = add i32 %108, 1
	addiu	$k0, $t9, 1
	sw	$k0, -132($sp)
	# move  %107, %123
	lw	$t8, -128($sp)
	# move  %108, %124
	lw	$t9, -132($sp)
	# br label %109
	j	main_26

main_28:
	# %126 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %94, i32 0, i32 %96
	li	$k1, 8
	mult	$s5, $k1
	mflo	$k1
	addu	$k0, $t3, $k1
	sw	$k0, -136($sp)
	# %127 = getelementptr [2 x i32], [2 x i32]* %126, i32 0, i32 %102
	lw	$k1, -72($sp)
	li	$k1, 4
	mult	$k1, $k1
	mflo	$k1
	lw	$k0, -136($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -140($sp)
	# store i32 %107, i32* %127
	lw	$k1, -140($sp)
	sw	$t8, 0($k1)
	# %128 = add i32 %102, 1
	lw	$k0, -72($sp)
	addiu	$k0, $k0, 1
	sw	$k0, -144($sp)
	# move  %101, %107
	# move  %102, %128
	lw	$k0, -144($sp)
	sw	$k0, -72($sp)
	# move  %103, %108
	# br label %104
	j	main_24

main_29:
	# %130 = add i32 %96, 1
	addiu	$k0, $s5, 1
	sw	$k0, -148($sp)
	# move  %95, %101
	move	$t4, $t8
	# move  %96, %130
	lw	$s5, -148($sp)
	# move  %97, %103
	move	$s3, $t9
	# br label %98
	j	main_22

main_30:
	# move  %132, 0
	move	$t1, $zero
	# move  %133, 0
	move	$t4, $zero
	# br label %134

main_31:
	# %135 = icmp slt i32 %133, 2
	li	$k1, 2
	slt	$s3, $t4, $k1
	# br i1 %135, label %136, label %150
	beq	$zero, $s3, main_36

main_32:
	# move  %137, %132
	move	$s3, $t1
	# br label %138

main_33:
	# %139 = icmp slt i32 %137, 2
	li	$k1, 2
	slt	$s5, $s3, $k1
	# br i1 %139, label %140, label %148
	beq	$zero, $s5, main_35

main_34:
	# %141 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* %94, i32 0, i32 %133
	li	$k1, 8
	mult	$t4, $k1
	mflo	$k1
	addu	$s5, $t3, $k1
	# %142 = getelementptr [2 x i32], [2 x i32]* %141, i32 0, i32 %137
	li	$k1, 4
	mult	$s3, $k1
	mflo	$k1
	addu	$s5, $s5, $k1
	# %143 = load i32 , i32* %142
	lw	$s5, 0($s5)
	# %144 = load [2 x i32]* , [2 x i32]** %92
	lw	$k0, 0($t0)
	sw	$k0, -152($sp)
	# %145 = getelementptr [2 x i32], [2 x i32]* %144, i32 %133
	li	$k0, 8
	mult	$t4, $k0
	mflo	$k1
	lw	$k0, -152($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -156($sp)
	# %146 = getelementptr [2 x i32], [2 x i32]* %145, i32 0, i32 %137
	li	$k1, 4
	mult	$s3, $k1
	mflo	$k1
	lw	$k0, -156($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -160($sp)
	# store i32 %143, i32* %146
	lw	$k1, -160($sp)
	sw	$s5, 0($k1)
	# %147 = add i32 %137, 1
	addiu	$s5, $s3, 1
	# move  %137, %147
	move	$s3, $s5
	# br label %138
	j	main_33

main_35:
	# %149 = add i32 %133, 1
	addiu	$k0, $t4, 1
	sw	$k0, -164($sp)
	# move  %132, %137
	move	$t1, $s3
	# move  %133, %149
	lw	$t4, -164($sp)
	# br label %134
	j	main_31

main_36:
	# br label %151

main_37:
	# br label %18
	j	main_2

main_38:
	# move  %153, 0
	move	$t0, $zero
	# br label %154

main_39:
	# %155 = icmp slt i32 %153, 2
	li	$k1, 2
	slt	$t1, $t0, $k1
	# br i1 %155, label %156, label %176
	beq	$zero, $t1, main_45

main_40:
	# %157 = getelementptr [2 x i32], [2 x i32]* @f, i32 0, i32 %153
	li	$k1, 4
	mult	$t0, $k1
	mflo	$k1
	la	$k0, global_f
	addu	$t1, $k0, $k1
	# %158 = load i32 , i32* %157
	lw	$t2, 0($t1)
	# %159 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* @ori, i32 0, i32 %153
	li	$k1, 8
	mult	$t0, $k1
	mflo	$k1
	la	$k0, global_ori
	addu	$t3, $k0, $k1
	# %160 = getelementptr [2 x i32], [2 x i32]* %159, i32 0, i32 0
	move	$t4, $t3
	# %161 = alloca i32*
	addi	$t1, $sp, -168
	# store i32* %160, i32** %161
	sw	$t4, 0($t1)
	# move  %162, 0
	move	$t3, $zero
	# move  %163, 0
	move	$t4, $zero
	# br label %164

main_41:
	# %165 = icmp slt i32 %163, 2
	li	$k1, 2
	slt	$t5, $t4, $k1
	# br i1 %165, label %166, label %172
	beq	$zero, $t5, main_43

main_42:
	# %167 = load i32* , i32** %161
	lw	$t5, 0($t1)
	# %168 = getelementptr i32, i32* %167, i32 %163
	li	$k0, 4
	mult	$t4, $k0
	mflo	$k1
	addu	$t6, $t5, $k1
	# %169 = load i32 , i32* %168
	lw	$t7, 0($t6)
	# %170 = add i32 %162, %169
	addu	$s0, $t3, $t7
	# %171 = add i32 %163, 1
	addiu	$s1, $t4, 1
	# move  %162, %170
	move	$t3, $s0
	# move  %163, %171
	move	$t4, $s1
	# br label %164
	j	main_41

main_43:
	# move  %173, %162
	move	$t1, $t3
	# call void @putch(i32 102)
	sw	$a0, -172($sp)
	la	$a0, str_0
	li	$v0, 4
	syscall
	lw	$a0, -172($sp)
	# call void @putint(i32 %158)
	sw	$a0, -172($sp)
	move	$a0, $t2
	li	$v0, 1
	syscall
	lw	$a0, -172($sp)
	# call void @putch(i32 58)
	sw	$a0, -172($sp)
	la	$a0, str_1
	li	$v0, 4
	syscall
	lw	$a0, -172($sp)
	# call void @putint(i32 %173)
	sw	$a0, -172($sp)
	move	$a0, $t1
	li	$v0, 1
	syscall
	lw	$a0, -172($sp)
	# call void @putch(i32 10)
	sw	$a0, -172($sp)
	la	$a0, str_2
	li	$v0, 4
	syscall
	lw	$a0, -172($sp)
	# %174 = add i32 %153, 1
	addiu	$t4, $t0, 1
	# br label %175

main_44:
	# move  %153, %174
	move	$t0, $t4
	# br label %154
	j	main_39

main_45:
	# %177 = getelementptr [2 x [2 x i32]], [2 x [2 x i32]]* @ori, i32 0, i32 0
	la	$k0, global_ori
	move	$t1, $k0
	# %178 = getelementptr [2 x i32], [2 x i32]* %177, i32 0, i32 0
	move	$t2, $t1
	# %179 = load i32 , i32* %178
	lw	$t3, 0($t2)
	# call void @putch(i32 111)
	sw	$a0, -172($sp)
	la	$a0, str_3
	li	$v0, 4
	syscall
	lw	$a0, -172($sp)
	# call void @putint(i32 %179)
	sw	$a0, -172($sp)
	move	$a0, $t3
	li	$v0, 1
	syscall
	lw	$a0, -172($sp)
	# call void @putch(i32 10)
	sw	$a0, -172($sp)
	la	$a0, str_4
	li	$v0, 4
	syscall
	lw	$a0, -172($sp)
	# ret i32 0
	move	$v0, $zero
	jr	$ra
end:


