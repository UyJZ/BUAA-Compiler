
        #include <stdio.h>
        #include <stdlib.h>
        int getint() {
            int input;
            scanf("%d", &input);
            return input;
        }
        
int isPrime(int m) {
    int i;
    i = 2;
    for (;i < m;) {
        if (m % i == 0)
            return 0;
        i = i +  1;
    } 
    return 1;
}


void check_group(int t[]) {
    int i;
    i = 0;
    for (;i < 2;) {
        int tmp;
        tmp = t[i];
        tmp = isPrime(tmp);
        printf("%d\n", tmp);
        i = i + 1;
    }

    
}

int test(int t[][2]) {
    if (!t[0][1]) {
        ;
    }
    return 0;
}

int main() {
    int num;
    num = 30;
    printf("%d\n", isPrime(num));
    num = 31;
    printf("%d\n", isPrime(num));
    int tmp[3][2] = {{111, 2222},{3,   4},{5,   6}};
    check_group(tmp[0]);
    check_group(tmp[1]);
    check_group(tmp[2]);

    int t;
    t = 1;
    for (;t > 0;) {
        if (t < 10)
            break;
        t = t + 1;
    }
    printf("t:%d\n", t);

    return 0;
}
