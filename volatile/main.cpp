#include <iostream>
#include <pthread.h>

namespace {
    void* func(void* arg)
    {
        bool* const flag = (bool*)arg;

        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int g = 0;
        int h = 0;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int m = 0;
        int n = 0;
        int o = 0;
        int p = 0;

        while((*flag) == false) {
            a++;
            b++;
            c++;
            d++;
            e++;
            f++;
            g++;
            h++;
            i++;
            j++;
            k++;
            l++;
            m++;
            n++;
            o++;
            p++;
        }

        std::cout << "Flag raised, thread exiting.\n";
        return ((int*)0) +
            a + b + c + d + e + f + g + h +
            i + j + k + l + m + n + o + p;
    }
}

int main(void)
{
    pthread_t threadId = 0;
    bool flag = false;

    if (pthread_create(&threadId, NULL, func, &flag) != 0) {
        std::cerr << "pthread_create failed\n";
        return EXIT_FAILURE;
    }

    if (sleep(1) != 0) {
        std::cerr << "sleep 1 failed\n";
        return EXIT_FAILURE;
    }

    flag = true;

    if (sleep(1) != 0) {
        std::cerr << "sleep 2 failed\n";
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}
