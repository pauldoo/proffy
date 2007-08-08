#include <iostream>
#include <vector>
#include <ctime>

void Intrinsics(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out);
void Vanilla(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out);

namespace {
    typedef void(*Convolver)(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out);

    const double Benchmark(Convolver convolver)
    {
        const int LINE_SIZE = 200;
        const int KERNEL_SIZE = 9;
        const int ITERATIONS = 1000000;
        
        std::vector<float> line(LINE_SIZE, 7.0f);
        std::vector<float> kernel(KERNEL_SIZE, 2.0f);
        std::vector<float> out(LINE_SIZE);
        
        const clock_t begin = clock();
        for (int i = 0; i < ITERATIONS; ++i) {
            convolver(line, kernel, out);
        }
        const clock_t end = clock();
        if (out[out.size()/2] != KERNEL_SIZE * 2.0f * 7.0f) {
            throw std::string("Wrong result");
        }
        return static_cast<double>(end - begin) / CLOCKS_PER_SEC;
    }

}

int main(void)
{
    const double vanillaTime = Benchmark(Vanilla);
    std::cout << "Vanilla: " << vanillaTime << "s" << std::endl;
    const double intrinsicsTime = Benchmark(Intrinsics);
    std::cout << "Intrinsics: " << intrinsicsTime << "s" << std::endl;
    std::cout << (vanillaTime / intrinsicsTime) << "x faster" << std::endl;
    return EXIT_SUCCESS;
}