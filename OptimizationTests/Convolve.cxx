#include <iostream>
#include <vector>
#include <ctime>

void VanillaFloat(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out);
void VanillaDouble(const std::vector<double>& line, const std::vector<double>& kernel, std::vector<double>& out);
void IntrinsicsFloat(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out);
void IntrinsicsDouble(const std::vector<double>& line, const std::vector<double>& kernel, std::vector<double>& out);

namespace {
    template<typename T>
    const double Benchmark(void (*convolver)(const std::vector<T>& line, const std::vector<T>& kernel, std::vector<T>& out))
    {
        const int LINE_SIZE = 500;
        const int KERNEL_SIZE = 9;
        const int ITERATIONS = 1000000;
        
        std::vector<T> line(LINE_SIZE, T(7));
        std::vector<T> kernel(KERNEL_SIZE, T(2));
        std::vector<T> out(LINE_SIZE);
        
        const clock_t begin = clock();
        for (int i = 0; i < ITERATIONS; ++i) {
            convolver(line, kernel, out);
        }
        const clock_t end = clock();
        if (out[out.size()/2] != KERNEL_SIZE * T(2) * T(7)) {
            throw std::string("Wrong result");
        }
        return static_cast<double>(end - begin) / CLOCKS_PER_SEC;
    }

}

int main(void)
{
    {
        std::cout << "Float:" << std::endl;
        const double vanillaTime = Benchmark(VanillaFloat);
        std::cout << "  Vanilla: " << vanillaTime << "s" << std::endl;
        const double intrinsicsTime = Benchmark(IntrinsicsFloat);
        std::cout << "  Intrinsics: " << intrinsicsTime << "s" << std::endl;
        std::cout << "  " << (vanillaTime / intrinsicsTime) << "x faster" << std::endl;
    }
    {
        std::cout << "Double:" << std::endl;
        const double vanillaTime = Benchmark(VanillaDouble);
        std::cout << "  Vanilla: " << vanillaTime << "s" << std::endl;
        const double intrinsicsTime = Benchmark(IntrinsicsDouble);
        std::cout << "  Intrinsics: " << intrinsicsTime << "s" << std::endl;
        std::cout << "  " << (vanillaTime / intrinsicsTime) << "x faster" << std::endl;
    }
    return EXIT_SUCCESS;
}