/**
  How many integers from 2 to 10^8 are sums of consecutive squares and palindromic?
  */

#include <iostream>

namespace
{
    const int limit = 100000000; // 10^8
    const int base = 10;

    const bool check_if_sum_of_consecutive_squares(const int num)
    {
        int begin = 1, end = 2;
        int val = begin * begin + end * end;
        while(end*end < num) {
            if (val == num) {
                //std::cout << num << " is the sum of consecutive squares of " << begin << " to " << end << std::endl;
                return true;
            }
            if (val < num) {
                end++;
                val += end * end;
            }
            if (val > num) {
                val -= begin * begin;
                begin++;
                if (begin == end) {
                    end++;
                    val += end * end;
                }
            }
        }
        return false;
    }
    
    // Construct an odd length palindrome from 'num':
    // Take the number "123" and return "12321"
    const int construct_odd_palindrome(const int num)
    {
        int rem = num / base;
        int val = 0;
        int multiplier = 1;
        while (rem > 0) {
            int digit = rem % base;
            val = val * base + digit;
            rem = rem / base;
            multiplier *= base;
        }
        return num * multiplier + val;
    }

    // Construct an even length palindrome from 'num':
    // Take the number "123" and return "123321"
    const int construct_even_palindrome(const int num)
    {
        int rem = num;
        int val = 0;
        int multiplier = 1;
        while (rem > 0) {
            int digit = rem % base;
            val = val * base + digit;
            rem = rem / base;
            multiplier *= base;
        }
        return num * multiplier + val;
    }

    const unsigned int odd_palindromes(void)
    {
        unsigned int total = 0;
        for (int i = 1; ; i++) {
            int odd = construct_odd_palindrome(i);
            if (odd <= limit) {
                if (check_if_sum_of_consecutive_squares(odd)) {
                    total += odd;
                }
            } else {
                break;
            }
        }
        return total;
    }
    
    const unsigned int even_palindromes(void)
    {
        unsigned int total = 0;
        for (int i = 1; ; i++) {
            int even = construct_even_palindrome(i);
            if (even <= limit) {
                if (check_if_sum_of_consecutive_squares(even)) {
                    total += even;
                }
            } else {
                break;
            }
        }
        return total;
    }
}

int main(void)
{
    std::ios::sync_with_stdio(false);
    std::cout << (odd_palindromes() + even_palindromes()) << std::endl;
    return 0;
}

