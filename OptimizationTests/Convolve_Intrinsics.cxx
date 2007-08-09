#include <vector>
#include <string>
#include <emmintrin.h>
#include <xmmintrin.h>
#include <mmintrin.h>

void IntrinsicsFloat(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out)
{
    if (line.size() % 4 != 0) {
        throw std::string("Invalid line size");
    }
    const int indexOfCenter = kernel.size() / 2;
    
    const int UNROLLING = 4;
    const int ELEMENTS_PER_REGISTER = 4;
    
    // Edge cases aren't dealt with in this test
    for (int i = 16; i < static_cast<int>(line.size()-16); i+=(ELEMENTS_PER_REGISTER*UNROLLING)) {
        __m128 accum0 = _mm_setzero_ps();
        __m128 accum1 = _mm_setzero_ps();
        __m128 accum2 = _mm_setzero_ps();
        __m128 accum3 = _mm_setzero_ps();
        
        for (int j = 0; j < static_cast<int>(kernel.size()); ++j) {
            const __m128 k = _mm_load1_ps(&(kernel[j]));
            const int index = i - indexOfCenter + j;

            const __m128 in0 = _mm_loadu_ps(&(line[index+ELEMENTS_PER_REGISTER*0]));
            const __m128 in1 = _mm_loadu_ps(&(line[index+ELEMENTS_PER_REGISTER*1]));
            const __m128 in2 = _mm_loadu_ps(&(line[index+ELEMENTS_PER_REGISTER*2]));
            const __m128 in3 = _mm_loadu_ps(&(line[index+ELEMENTS_PER_REGISTER*3]));

            accum0 = _mm_add_ps(accum0, _mm_mul_ps(in0, k));
            accum1 = _mm_add_ps(accum1, _mm_mul_ps(in1, k));
            accum2 = _mm_add_ps(accum2, _mm_mul_ps(in2, k));
            accum3 = _mm_add_ps(accum3, _mm_mul_ps(in3, k));
        }
        _mm_storeu_ps(&(out[i+ELEMENTS_PER_REGISTER*0]), accum0);
        _mm_storeu_ps(&(out[i+ELEMENTS_PER_REGISTER*1]), accum1);
        _mm_storeu_ps(&(out[i+ELEMENTS_PER_REGISTER*2]), accum2);
        _mm_storeu_ps(&(out[i+ELEMENTS_PER_REGISTER*3]), accum3);
    }
}

void IntrinsicsDouble(const std::vector<double>& line, const std::vector<double>& kernel, std::vector<double>& out)
{
    if (line.size() % 2 != 0) {
        throw std::string("Invalid line size");
    }
    const int indexOfCenter = kernel.size() / 2;
    
    const int UNROLLING = 4;
    const int ELEMENTS_PER_REGISTER = 2;
    
    // Edge cases aren't dealt with in this test
    for (int i = 16; i < static_cast<int>(line.size()-16); i+=(ELEMENTS_PER_REGISTER*UNROLLING)) {
        __m128d accum0 = _mm_setzero_pd();
        __m128d accum1 = _mm_setzero_pd();
        __m128d accum2 = _mm_setzero_pd();
        __m128d accum3 = _mm_setzero_pd();
        
        for (int j = 0; j < static_cast<int>(kernel.size()); ++j) {
            const __m128d k = _mm_load1_pd(&(kernel[j]));
            const int index = i - indexOfCenter + j;

            const __m128d in0 = _mm_loadu_pd(&(line[index+ELEMENTS_PER_REGISTER*0]));
            const __m128d in1 = _mm_loadu_pd(&(line[index+ELEMENTS_PER_REGISTER*1]));
            const __m128d in2 = _mm_loadu_pd(&(line[index+ELEMENTS_PER_REGISTER*2]));
            const __m128d in3 = _mm_loadu_pd(&(line[index+ELEMENTS_PER_REGISTER*3]));

            accum0 = _mm_add_pd(accum0, _mm_mul_pd(in0, k));
            accum1 = _mm_add_pd(accum1, _mm_mul_pd(in1, k));
            accum2 = _mm_add_pd(accum2, _mm_mul_pd(in2, k));
            accum3 = _mm_add_pd(accum3, _mm_mul_pd(in3, k));
        }
        _mm_storeu_pd(&(out[i+ELEMENTS_PER_REGISTER*0]), accum0);
        _mm_storeu_pd(&(out[i+ELEMENTS_PER_REGISTER*1]), accum1);
        _mm_storeu_pd(&(out[i+ELEMENTS_PER_REGISTER*2]), accum2);
        _mm_storeu_pd(&(out[i+ELEMENTS_PER_REGISTER*3]), accum3);
    }
}

