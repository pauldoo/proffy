#include <algorithm>
#include <ctime>
#include <iostream>
#include <vector>
#include <cassert>

namespace {
    const short minimum(const std::vector<short>& values)
    {
        return *std::min_element(values.begin(), values.end());
    }
}

int main(void)
{
    std::vector<short> values;
    for (int i = 1024; i <= 128 * 1024 * 1024; i *= 2) {
        values.resize(i);
        short total = 0;
        const clock_t begin = clock();
        for (int j = 0; j < 1000000000; j += i) {
            total += minimum(values);
        }
        const clock_t end = clock();
        assert(total == 0);
        const double time_in_ms = (end - begin) * 1000.0 / CLOCKS_PER_SEC;
        std::cout << i << ": " << time_in_ms << "ms" << std::endl;
    }
    return 0;
}


