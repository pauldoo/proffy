#include <vector>
#include <string>
#include <emmintrin.h>
#include <xmmintrin.h>
#include <mmintrin.h>

void Intrinsics(const std::vector<float>& line, const std::vector<float>& kernel, std::vector<float>& out)
{
    if (line.size() % 4 != 0) {
        throw std::string("Invalid line size");
    }
    const int indexOfCenter = kernel.size() / 2;
    
    const int UNROLLING = 4;
    
    for (int i = 8; i < static_cast<int>(line.size()-8); i+=(4*UNROLLING)) {
        __m128 accum0 = _mm_setzero_ps();
        __m128 accum1 = _mm_setzero_ps();
        __m128 accum2 = _mm_setzero_ps();
        __m128 accum3 = _mm_setzero_ps();
        //__m128 accum4 = _mm_setzero_ps();
        //__m128 accum5 = _mm_setzero_ps();
        //__m128 accum6 = _mm_setzero_ps();
        //__m128 accum7 = _mm_setzero_ps();
        
        for (int j = 0; j < static_cast<int>(kernel.size()); ++j) {
            const __m128 k = _mm_load1_ps(&(kernel[j]));
            const int index = i - indexOfCenter + j;

            const __m128 in0 = _mm_loadu_ps(&(line[index+4*0]));
            const __m128 in1 = _mm_loadu_ps(&(line[index+4*1]));
            const __m128 in2 = _mm_loadu_ps(&(line[index+4*2]));
            const __m128 in3 = _mm_loadu_ps(&(line[index+4*3]));
            //const __m128 in4 = _mm_loadu_ps(&(line[index+4*4]));
            //const __m128 in5 = _mm_loadu_ps(&(line[index+4*5]));
            //const __m128 in6 = _mm_loadu_ps(&(line[index+4*6]));
            //const __m128 in7 = _mm_loadu_ps(&(line[index+4*7]));

            accum0 = _mm_add_ps(accum0, _mm_mul_ps(in0, k));
            accum1 = _mm_add_ps(accum1, _mm_mul_ps(in1, k));
            accum2 = _mm_add_ps(accum2, _mm_mul_ps(in2, k));
            accum3 = _mm_add_ps(accum3, _mm_mul_ps(in3, k));
            //accum4 = _mm_add_ps(accum4, _mm_mul_ps(in4, k));
            //accum5 = _mm_add_ps(accum5, _mm_mul_ps(in5, k));
            //accum6 = _mm_add_ps(accum6, _mm_mul_ps(in6, k));
            //accum7 = _mm_add_ps(accum7, _mm_mul_ps(in7, k));
        }
        _mm_store_ps(&(out[i+4*0]), accum0);
        _mm_store_ps(&(out[i+4*1]), accum1);
        _mm_store_ps(&(out[i+4*2]), accum2);
        _mm_store_ps(&(out[i+4*3]), accum3);
        //_mm_store_ps(&(out[i+4*4]), accum4);
        //_mm_store_ps(&(out[i+4*5]), accum5);
        //_mm_store_ps(&(out[i+4*6]), accum6);
        //_mm_store_ps(&(out[i+4*7]), accum7);
    }
}

