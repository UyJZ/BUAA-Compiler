.data
global_N:	.word 	10
global_a:		.word 	0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
str_0:	.asciiz   ", "
str_1:	.asciiz   "\n"
str_2:	.asciiz   ", "
str_3:	.asciiz   ", "
str_4:	.asciiz   "\n"
.text
	jal	entry
	j	end
func_fib:
fib_0:
	# %2 = icmp eq i32 %0, 1
	li	$k1, 1
	seq	$t0, $a0, $k1
	# br i1 %2, label %3, label %4
	bne	$zero, $t0, fib_1
	beq	$zero, $t0, fib_2

fib_1:
	# ret i32 1
	li	$v0, 1
	jr	$ra

fib_2:
	# %5 = icmp eq i32 %0, 2
	li	$k1, 2
	seq	$t1, $a0, $k1
	# br i1 %5, label %6, label %7
	bne	$zero, $t1, fib_3
	beq	$zero, $t1, fib_4

fib_3:
	# ret i32 2
	li	$v0, 2
	jr	$ra

fib_4:
	# %8 = sub i32 %0, 1
	subiu	$t2, $a0, 1
	# %9 = call i32 @fib(i32 %8)
	sw	$t0, -4($sp)
	sw	$t1, -8($sp)
	sw	$t2, -12($sp)
	sw	$t3, -16($sp)
	sw	$a0, -20($sp)
	lw	$a0, -12($sp)
	sw	$ra, -24($sp)
	sw	$sp, -28($sp)
	addi	$sp, $sp, -28
	jal	func_fib
	lw	$t0, 24($sp)
	lw	$t1, 20($sp)
	lw	$t2, 16($sp)
	lw	$t3, 12($sp)
	lw	$a0, 8($sp)
	lw	$ra, 4($sp)
	lw	$sp, 0($sp)
	move	$t3, $v0
	# %10 = sub i32 %0, 2
	subiu	$t4, $a0, 2
	# %11 = call i32 @fib(i32 %10)
	sw	$t0, -32($sp)
	sw	$t1, -36($sp)
	sw	$t2, -40($sp)
	sw	$t3, -44($sp)
	sw	$t4, -48($sp)
	sw	$t5, -52($sp)
	sw	$a0, -56($sp)
	lw	$a0, -48($sp)
	sw	$ra, -60($sp)
	sw	$sp, -64($sp)
	addi	$sp, $sp, -64
	jal	func_fib
	lw	$t0, 32($sp)
	lw	$t1, 28($sp)
	lw	$t2, 24($sp)
	lw	$t3, 20($sp)
	lw	$t4, 16($sp)
	lw	$t5, 12($sp)
	lw	$a0, 8($sp)
	lw	$ra, 4($sp)
	lw	$sp, 0($sp)
	move	$t5, $v0
	# %12 = add i32 %9, %11
	addu	$t6, $t3, $t5
	# ret i32 %12
	move	$v0, $t6
	jr	$ra


entry:
func_main:
main_0:
	# %1 = call i32 @getint()
	li	$v0, 5
	syscall
	move	$t0, $v0
	# %2 = call i32 @getint()
	li	$v0, 5
	syscall
	move	$t1, $v0
	# %3 = mul i32 %1, %2
	mult	$t0, $t1
	mflo	$t2
	# %4 = sub i32 0, %3
	subu	$t3, $zero, $t2
	# %5 = call i32 @fib(i32 4)
	sw	$t0, -4($sp)
	sw	$t1, -8($sp)
	sw	$t2, -12($sp)
	sw	$t3, -16($sp)
	sw	$t4, -20($sp)
	li	$a0, 4
	sw	$ra, -24($sp)
	sw	$sp, -28($sp)
	addi	$sp, $sp, -28
	jal	func_fib
	lw	$t0, 24($sp)
	lw	$t1, 20($sp)
	lw	$t2, 16($sp)
	lw	$t3, 12($sp)
	lw	$t4, 8($sp)
	lw	$ra, 4($sp)
	lw	$sp, 0($sp)
	move	$t4, $v0
	# %6 = mul i32 %4, %5
	mult	$t3, $t4
	mflo	$t5
	# %7 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 1
	li	$k0, 1
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	move	$t6, $k0
	# %8 = load i32 , i32* %7
	lw	$t7, 0($t6)
	# %9 = add i32 %6, %8
	addu	$s0, $t5, $t7
	# %10 = mul i32 %9, 5
	li	$k1, 5
	mult	$s0, $k1
	mflo	$s1
	# %11 = call i32 @fib(i32 5)
	sw	$t0, -32($sp)
	sw	$t1, -36($sp)
	sw	$t2, -40($sp)
	sw	$t3, -44($sp)
	sw	$t4, -48($sp)
	sw	$t5, -52($sp)
	sw	$t6, -56($sp)
	sw	$t7, -60($sp)
	sw	$s0, -64($sp)
	sw	$s1, -68($sp)
	sw	$s2, -72($sp)
	li	$a0, 5
	sw	$ra, -76($sp)
	sw	$sp, -80($sp)
	addi	$sp, $sp, -80
	jal	func_fib
	lw	$t0, 48($sp)
	lw	$t1, 44($sp)
	lw	$t2, 40($sp)
	lw	$t3, 36($sp)
	lw	$t4, 32($sp)
	lw	$t5, 28($sp)
	lw	$t6, 24($sp)
	lw	$t7, 20($sp)
	lw	$s0, 16($sp)
	lw	$s1, 12($sp)
	lw	$s2, 8($sp)
	lw	$ra, 4($sp)
	lw	$sp, 0($sp)
	move	$s2, $v0
	# %12 = add i32 %11, 2
	addi	$s3, $s2, 2
	# %13 = call i32 @fib(i32 %12)
	sw	$t0, -84($sp)
	sw	$t1, -88($sp)
	sw	$t2, -92($sp)
	sw	$t3, -96($sp)
	sw	$t4, -100($sp)
	sw	$t5, -104($sp)
	sw	$t6, -108($sp)
	sw	$t7, -112($sp)
	sw	$s0, -116($sp)
	sw	$s1, -120($sp)
	sw	$s2, -124($sp)
	sw	$s3, -128($sp)
	sw	$s4, -132($sp)
	lw	$a0, -128($sp)
	sw	$ra, -136($sp)
	sw	$sp, -140($sp)
	addi	$sp, $sp, -140
	jal	func_fib
	lw	$t0, 56($sp)
	lw	$t1, 52($sp)
	lw	$t2, 48($sp)
	lw	$t3, 44($sp)
	lw	$t4, 40($sp)
	lw	$t5, 36($sp)
	lw	$t6, 32($sp)
	lw	$t7, 28($sp)
	lw	$s0, 24($sp)
	lw	$s1, 20($sp)
	lw	$s2, 16($sp)
	lw	$s3, 12($sp)
	lw	$s4, 8($sp)
	lw	$ra, 4($sp)
	lw	$sp, 0($sp)
	move	$s4, $v0
	# %14 = sub i32 1197, %13
	subiu	$s5, $s4, 1197
	subu	$s5, $zero, $s5
	# %15 = add i32 1, 2
	li	$s6, 3
	# %16 = sub i32 %15, 9186
	subiu	$s7, $s6, 9186
	# %17 = sub i32 %16, 908
	subiu	$t8, $s7, 908
	# %18 = add i32 %14, %17
	addu	$t9, $s5, $t8
	# move  %19, %10
	sw	$s1, -144($sp)
	# br label %20
	j	main_1

main_1:
	# %21 = icmp slt i32 %19, 100
	lw	$k0, -144($sp)
	li	$k1, 100
	slt	$k0, $k0, $k1
	sw	$k0, -148($sp)
	# br i1 %21, label %22, label %54
	lw	$k0, -148($sp)
	bne	$zero, $k0, main_2
	beq	$zero, $k0, main_3

main_2:
	# %23 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 0
	li	$k1, 4
	mult	$zero, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -152($sp)
	# %24 = load i32 , i32* %23
	lw	$k0, -152($sp)
	lw	$k1, 0($k0)
	sw	$k1, -156($sp)
	# %25 = mul i32 -6, -6
	li	$k0, -6
	li	$k1, -6
	mult	$k0, $k1
	mflo	$k0
	sw	$k0, -160($sp)
	# %26 = add i32 %24, %25
	lw	$k0, -156($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -164($sp)
	# store i32 %26, i32* %23
	lw	$k0, -164($sp)
	lw	$k1, -152($sp)
	sw	$k0, 0($k1)
	# %27 = load i32 , i32* %7
	lw	$k0, 0($t6)
	sw	$k0, -168($sp)
	# %28 = add i32 %27, %25
	lw	$k0, -168($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -172($sp)
	# store i32 %28, i32* %7
	lw	$k0, -172($sp)
	sw	$k0, 0($t6)
	# %29 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 2
	li	$k0, 2
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -176($sp)
	# %30 = load i32 , i32* %29
	lw	$k0, -176($sp)
	lw	$k1, 0($k0)
	sw	$k1, -180($sp)
	# %31 = add i32 %30, %25
	lw	$k0, -180($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -184($sp)
	# store i32 %31, i32* %29
	lw	$k0, -184($sp)
	lw	$k1, -176($sp)
	sw	$k0, 0($k1)
	# %32 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 3
	li	$k0, 3
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -188($sp)
	# %33 = load i32 , i32* %32
	lw	$k0, -188($sp)
	lw	$k1, 0($k0)
	sw	$k1, -192($sp)
	# %34 = add i32 %33, %25
	lw	$k0, -192($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -196($sp)
	# store i32 %34, i32* %32
	lw	$k0, -196($sp)
	lw	$k1, -188($sp)
	sw	$k0, 0($k1)
	# %35 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 4
	li	$k0, 4
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -200($sp)
	# %36 = load i32 , i32* %35
	lw	$k0, -200($sp)
	lw	$k1, 0($k0)
	sw	$k1, -204($sp)
	# %37 = add i32 %36, %25
	lw	$k0, -204($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -208($sp)
	# store i32 %37, i32* %35
	lw	$k0, -208($sp)
	lw	$k1, -200($sp)
	sw	$k0, 0($k1)
	# %38 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 5
	li	$k0, 5
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -212($sp)
	# %39 = load i32 , i32* %38
	lw	$k0, -212($sp)
	lw	$k1, 0($k0)
	sw	$k1, -216($sp)
	# %40 = add i32 %39, %25
	lw	$k0, -216($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -220($sp)
	# store i32 %40, i32* %38
	lw	$k0, -220($sp)
	lw	$k1, -212($sp)
	sw	$k0, 0($k1)
	# %41 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 6
	li	$k0, 6
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -224($sp)
	# %42 = load i32 , i32* %41
	lw	$k0, -224($sp)
	lw	$k1, 0($k0)
	sw	$k1, -228($sp)
	# %43 = add i32 %42, %25
	lw	$k0, -228($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -232($sp)
	# store i32 %43, i32* %41
	lw	$k0, -232($sp)
	lw	$k1, -224($sp)
	sw	$k0, 0($k1)
	# %44 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 7
	li	$k0, 7
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -236($sp)
	# %45 = load i32 , i32* %44
	lw	$k0, -236($sp)
	lw	$k1, 0($k0)
	sw	$k1, -240($sp)
	# %46 = add i32 %45, %25
	lw	$k0, -240($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -244($sp)
	# store i32 %46, i32* %44
	lw	$k0, -244($sp)
	lw	$k1, -236($sp)
	sw	$k0, 0($k1)
	# %47 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 8
	li	$k0, 8
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -248($sp)
	# %48 = load i32 , i32* %47
	lw	$k0, -248($sp)
	lw	$k1, 0($k0)
	sw	$k1, -252($sp)
	# %49 = add i32 %48, %25
	lw	$k0, -252($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -256($sp)
	# store i32 %49, i32* %47
	lw	$k0, -256($sp)
	lw	$k1, -248($sp)
	sw	$k0, 0($k1)
	# %50 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 9
	li	$k0, 9
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -260($sp)
	# %51 = load i32 , i32* %50
	lw	$k0, -260($sp)
	lw	$k1, 0($k0)
	sw	$k1, -264($sp)
	# %52 = add i32 %51, %25
	lw	$k0, -264($sp)
	lw	$k1, -160($sp)
	addu	$k0, $k0, $k1
	sw	$k0, -268($sp)
	# store i32 %52, i32* %50
	lw	$k0, -268($sp)
	lw	$k1, -260($sp)
	sw	$k0, 0($k1)
	# %53 = add i32 %19, 1
	lw	$k0, -144($sp)
	addi	$k0, $k0, 1
	sw	$k0, -272($sp)
	# move  %19, %53
	lw	$k0, -272($sp)
	sw	$k0, -144($sp)
	# br label %20
	j	main_1

main_3:
	# move  %55, 0
	sw	$zero, -276($sp)
	# br label %56
	j	main_4

main_4:
	# %57 = icmp slt i32 %55, 10
	lw	$k0, -276($sp)
	li	$k1, 10
	slt	$k0, $k0, $k1
	sw	$k0, -280($sp)
	# br i1 %57, label %58, label %62
	lw	$k0, -280($sp)
	bne	$zero, $k0, main_5
	beq	$zero, $k0, main_6

main_5:
	# %59 = getelementptr [10 x i32], [10 x i32]* @a, i32 0, i32 %55
	lw	$k0, -276($sp)
	li	$k1, 4
	mult	$k0, $k1
	mflo	$k1
	la	$k0, global_a
	addu	$k0, $k0, $k1
	sw	$k0, -284($sp)
	# %60 = load i32 , i32* %59
	lw	$k0, -284($sp)
	lw	$k1, 0($k0)
	sw	$k1, -288($sp)
	# call void @putint(i32 %60)
	sw	$a0, -292($sp)
	lw	$a0, -288($sp)
	li	$v0, 1
	syscall
	lw	$a0, -292($sp)
	# call void @putch(i32 44)
	sw	$a0, -292($sp)
	la	$a0, str_0
	li	$v0, 4
	syscall
	lw	$a0, -292($sp)
	# %61 = add i32 %55, 1
	lw	$k0, -276($sp)
	addi	$k0, $k0, 1
	sw	$k0, -292($sp)
	# move  %55, %61
	lw	$k0, -292($sp)
	sw	$k0, -276($sp)
	# br label %56
	j	main_4

main_6:
	# call void @putch(i32 10)
	sw	$a0, -296($sp)
	la	$a0, str_1
	li	$v0, 4
	syscall
	lw	$a0, -296($sp)
	# call void @putint(i32 %55)
	sw	$a0, -296($sp)
	lw	$a0, -276($sp)
	li	$v0, 1
	syscall
	lw	$a0, -296($sp)
	# call void @putch(i32 44)
	sw	$a0, -296($sp)
	la	$a0, str_2
	li	$v0, 4
	syscall
	lw	$a0, -296($sp)
	# call void @putint(i32 %18)
	sw	$a0, -296($sp)
	move	$a0, $t9
	li	$v0, 1
	syscall
	lw	$a0, -296($sp)
	# call void @putch(i32 44)
	sw	$a0, -296($sp)
	la	$a0, str_3
	li	$v0, 4
	syscall
	lw	$a0, -296($sp)
	# call void @putint(i32 -6)
	sw	$a0, -296($sp)
	li	$a0, -6
	li	$v0, 1
	syscall
	lw	$a0, -296($sp)
	# call void @putch(i32 10)
	sw	$a0, -296($sp)
	la	$a0, str_4
	li	$v0, 4
	syscall
	lw	$a0, -296($sp)
	# ret i32 0
	move	$v0, $zero
	jr	$ra
end:


