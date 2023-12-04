.data
str_0:	.asciiz   "\n"
str_1:	.asciiz   "\n"
str_2:	.asciiz   "\n"
str_3:	.asciiz   "\n"
str_4:	.asciiz   "\n"
str_5:	.asciiz   "\n"
str_6:	.asciiz   "\n"
str_7:	.asciiz   "\n"
str_8:	.asciiz   "\n"
str_9:	.asciiz   "\n"
str_10:	.asciiz   "\n"
str_11:	.asciiz   "\n"
str_12:	.asciiz   "\n"
str_13:	.asciiz   "\n"
str_14:	.asciiz   "\n"
str_15:	.asciiz   "\n"
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
	# call void @putint(i32 %1)
	sw	$a0, -4($sp)
	move	$a0, $t0
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_0
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %2 = sdiv i32 %1, 2
	li	$k1, 2
	div	$t0, $k1
	mflo	$t1
	# call void @putint(i32 %2)
	sw	$a0, -4($sp)
	move	$a0, $t1
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_1
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %3 = sdiv i32 %1, 3
	li	$k1, 3
	div	$t0, $k1
	mflo	$t2
	# call void @putint(i32 %3)
	sw	$a0, -4($sp)
	move	$a0, $t2
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_2
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %4 = sdiv i32 %1, 4
	li	$k1, 4
	div	$t0, $k1
	mflo	$t3
	# call void @putint(i32 %4)
	sw	$a0, -4($sp)
	move	$a0, $t3
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_3
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %5 = sdiv i32 %1, 5
	li	$k1, 5
	div	$t0, $k1
	mflo	$t4
	# call void @putint(i32 %5)
	sw	$a0, -4($sp)
	move	$a0, $t4
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_4
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %6 = sdiv i32 %1, 6
	li	$k1, 6
	div	$t0, $k1
	mflo	$t5
	# call void @putint(i32 %6)
	sw	$a0, -4($sp)
	move	$a0, $t5
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_5
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %7 = sdiv i32 %1, 7
	li	$k1, 7
	div	$t0, $k1
	mflo	$t6
	# call void @putint(i32 %7)
	sw	$a0, -4($sp)
	move	$a0, $t6
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_6
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %8 = sdiv i32 %1, 8
	li	$k1, 8
	div	$t0, $k1
	mflo	$t7
	# call void @putint(i32 %8)
	sw	$a0, -4($sp)
	move	$a0, $t7
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_7
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %9 = sdiv i32 %1, 9
	li	$k0, 954437176
	mult	$t0, $k0
	mflo	$k1
	srl	$s0, $k1, 1
	# call void @putint(i32 %9)
	sw	$a0, -4($sp)
	move	$a0, $s0
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_8
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %10 = sdiv i32 %1, 10
	li	$k1, 10
	div	$t0, $k1
	mflo	$s1
	# call void @putint(i32 %10)
	sw	$a0, -4($sp)
	move	$a0, $s1
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_9
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %11 = sdiv i32 %1, 11
	li	$k1, 11
	div	$t0, $k1
	mflo	$s2
	# call void @putint(i32 %11)
	sw	$a0, -4($sp)
	move	$a0, $s2
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_10
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %12 = sdiv i32 %1, 12
	li	$k1, 12
	div	$t0, $k1
	mflo	$s3
	# call void @putint(i32 %12)
	sw	$a0, -4($sp)
	move	$a0, $s3
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_11
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %13 = sdiv i32 %1, 13
	li	$k1, 13
	div	$t0, $k1
	mflo	$s4
	# call void @putint(i32 %13)
	sw	$a0, -4($sp)
	move	$a0, $s4
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_12
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %14 = sdiv i32 %1, 14
	li	$k1, 14
	div	$t0, $k1
	mflo	$s5
	# call void @putint(i32 %14)
	sw	$a0, -4($sp)
	move	$a0, $s5
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_13
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %15 = sdiv i32 %1, 15
	li	$k1, 15
	div	$t0, $k1
	mflo	$s6
	# call void @putint(i32 %15)
	sw	$a0, -4($sp)
	move	$a0, $s6
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_14
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# %16 = sdiv i32 %1, 200
	li	$k1, 200
	div	$t0, $k1
	mflo	$s7
	# call void @putint(i32 %16)
	sw	$a0, -4($sp)
	move	$a0, $s7
	li	$v0, 1
	syscall
	lw	$a0, -4($sp)
	# call void @putch(i32 10)
	sw	$a0, -4($sp)
	la	$a0, str_15
	li	$v0, 4
	syscall
	lw	$a0, -4($sp)
	# ret i32 0
	move	$v0, $zero
	jr	$ra
end:


