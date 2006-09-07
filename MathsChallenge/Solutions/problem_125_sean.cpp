#include <iostream>
#include <algorithm>
#include <vector>
#include <numeric>
#include <time.h>

using namespace std; 

int f(const int n, const int m) {
    return ((1 + m - n)*(m + 2*m*m - n + 2*m*n + 2*n*n))/6;
}

int TenToPower(int n) {
    if (n == 8) return 100000000;
    if (n == 7) return 10000000;
    if (n == 6) return 1000000; 
    if (n == 5) return 100000;
    if (n == 4) return 10000;
    if (n == 3) return 1000;
    if (n == 2) return 100;
    if (n == 1) return 10;
    return 1;
}

int NoDigits(const int n) {
    if (n >= TenToPower(7)) return 8;
    if (n >= TenToPower(6)) return 7;
    if (n >= TenToPower(5)) return 6;
    if (n >= TenToPower(4)) return 5;
    if (n >= TenToPower(3)) return 4;
    if (n >= TenToPower(2)) return 3; 
    if (n >= TenToPower(1)) return 2;
    return 1;
}

int Digit(int n, int digit) {
    return n/TenToPower(digit - 1) % 10;
}

bool IsPalandrome(int n) {
    for (int i = 1 ; i <= NoDigits(n)/2 ; i++) { 
        if ( Digit(n, i) != Digit(n, NoDigits(n) - i + 1) ) return false;
    }
    return true;
}

int main()
{
    long long answer = 0;
    for (int counter = 0 ; counter < 1 ; counter++) { 
        const int N = 100000000;
        vector<int> hits;
        
        for (int m = 2 ; f(m-1,m) <     N ; m++) 
            for (int n = m-1 ; n > 0 && f(n,m) < N ; n--) 
                if (IsPalandrome(f(n,m))) hits.push_back(f(n,m));
        
        sort(hits.begin(),hits.end());
        vector<int>::iterator end = unique(hits.begin(),hits.end());
        answer = accumulate(hits.begin(), end ,(long long)0);
    }
    cout << answer << endl;
    cout << (double)clock()/CLOCKS_PER_SEC << endl;
    return 0;
}


